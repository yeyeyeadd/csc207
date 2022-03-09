package ca.utoronto.cs;

import ca.utoronto.cs.message.RequestHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class RequestHeaderToStringEncoder extends MessageToMessageEncoder<RequestHeader> {
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void encode(ChannelHandlerContext ctx, RequestHeader resp,
						  List<Object> list) throws JsonProcessingException {
		list.add(mapper.writeValueAsString(resp) + "\n");
	}
}
