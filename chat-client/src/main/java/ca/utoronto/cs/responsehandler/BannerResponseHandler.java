package ca.utoronto.cs.responsehandler;

import ca.utoronto.cs.message.BannerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BannerResponseHandler extends ResponseHandler<BannerResponse> {
	private final Logger logger = LogManager.getLogger();

	public BannerResponseHandler(Class<BannerResponse> cls) {
		super(cls);
	}

	/**
	 *To show the response of BannerRequest
	 * @param ctx ctx
	 * @param resp giving content
	 */
	@Override
	public void _handle(ChannelHandlerContext ctx, BannerResponse resp) {
		System.out.println(String.format("Received banner: %s", resp.message));
	}
}
