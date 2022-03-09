package ca.utoronto.cs;

import ca.utoronto.cs.message.RequestHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class StringToMessageHeaderDecoder extends MessageToMessageDecoder<String> {
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void decode(ChannelHandlerContext ctx,
						  String s, List<Object> list) throws JsonProcessingException {
		list.add(mapper.readValue(s, RequestHeader.class));
	}
}
