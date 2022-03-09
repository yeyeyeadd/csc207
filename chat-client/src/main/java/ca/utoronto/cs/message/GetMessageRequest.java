package ca.utoronto.cs.message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class GetMessageRequest extends Request{
    @Override
    protected MessageType getType() {
        return MessageType.GET_MESSAGE_REQUEST;
    }
    public String type;
}
