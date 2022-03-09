package ca.utoronto.cs;

import ca.utoronto.cs.message.ResponseHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ResponseHeaderToStringEncoder extends MessageToMessageEncoder<ResponseHeader> {
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void encode(ChannelHandlerContext ctx, ResponseHeader resp,
						  List<Object> list) throws JsonProcessingException {
		list.add(mapper.writeValueAsString(resp) + "\n");
	}
}
