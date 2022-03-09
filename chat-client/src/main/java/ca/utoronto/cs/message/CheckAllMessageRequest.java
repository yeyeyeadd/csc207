package ca.utoronto.cs.message;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class CheckAllMessageRequest extends Request{
    /**
     * return a CHECK_ALL_MESSAGE_REQUEST, type of MessageType
     * @return a CHECK_ALL_MESSAGE_REQUEST, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.CHECK_ALL_MESSAGE_REQUEST;
    }
}
