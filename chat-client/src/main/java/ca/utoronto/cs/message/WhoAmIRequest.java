package ca.utoronto.cs.message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class WhoAmIRequest extends Request{
    @Override
    protected MessageType getType() {
        return MessageType.WHO_AM_I_REQUEST;
    }
}
