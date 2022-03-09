package ca.utoronto.cs.message;

public class MessageAllSpeakerRequest extends Request{
    /**
     *return a MESSAGE_ALL_SPEAKER_REQUEST, type of MessageType
     * @return a MESSAGE_ALL_SPEAKER_REQUEST, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.MESSAGE_ALL_SPEAKER_REQUEST;
    }

    public String message;
}
