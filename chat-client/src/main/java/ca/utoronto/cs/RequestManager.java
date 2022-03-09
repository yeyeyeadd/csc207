package ca.utoronto.cs;

import ca.utoronto.cs.exception.GenericResponseException;
import ca.utoronto.cs.message.BannerResponse;
import ca.utoronto.cs.message.Request;
import ca.utoronto.cs.message.Response;
import ca.utoronto.cs.message.ResponseHeader;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class RequestManager {
	private final Logger logger = LogManager.getLogger();
	private final Map<String, CompletableFuture> requests = new ConcurrentHashMap<>();
	private final Map<Class, Subject> subscribers = new ConcurrentHashMap<>();
	private Channel channel;

	public void connect(String host, int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap()
				.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChatClientInitialiser(this));
		try {
			channel = bootstrap.connect(host, port).sync().channel();
		}
		catch (InterruptedException e) {
			group.shutdownGracefully();
			throw e;
		}
	}

	public <T extends Response> CompletableFuture<T> sendAsync(Request req) {
		CompletableFuture<T> future = new CompletableFuture<>();
		String reqId = this.generateRequestId();
		this.requests.put(reqId, future);
		channel.writeAndFlush(req.putHeader(reqId));
		return future;
	}

	public <T extends Response> void callback(String reqId, T resp) {
		if (!this.requests.containsKey(reqId)) {
			logger.warn("Request id unknown. Not processing for callback");
			return;
		}
		this.requests.get(reqId).complete(resp);
		this.requests.remove(reqId);
	}

	public <T extends Response> Observable<T> subscribe(Class<T> cls) {
		logger.debug("response class is subscribed", cls);
		if (!this.subscribers.containsKey(cls)) {
			Subject<T> subject = PublishSubject.create();
			this.subscribers.put(cls, subject);
		}
		return Observable.wrap(this.subscribers.get(cls));
	}

	public <T extends Response> void reject(String reqId, T resp) {
		if (!this.requests.containsKey(reqId)) {
			logger.warn("Request id unknown. Not processing for reject");
			return;
		}
		this.requests.get(reqId).completeExceptionally(new GenericResponseException(resp));
		this.requests.remove(reqId);
	}

	public <T extends Response> void handle(T resp, ResponseHeader header) {
		if (header.status) {
			callback(header.requestId, resp);
			if (this.subscribers.containsKey(resp.getClass())) {
				logger.debug("response has subscriber");
				this.subscribers.get(resp.getClass()).onNext(resp);
			}
		}
		else {
			reject(header.requestId, resp);
			if (this.subscribers.containsKey(resp.getClass())) {
				logger.debug("error response has subscriber");
				this.subscribers.get(resp.getClass()).onError(new GenericResponseException(resp));
			}
		}
	}

	private String generateRequestId() {
		return UUID.randomUUID().toString();
	}
}
