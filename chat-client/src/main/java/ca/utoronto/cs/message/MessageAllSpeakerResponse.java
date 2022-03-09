package ca.utoronto.cs.message;

import ca.utoronto.cs.entity.Message;

import java.util.List;

public class MessageAllSpeakerResponse extends Response{
    /**
     *return a MESSAGE_ALL_SPEAKER_RESPONSE, type of MessageType
     * @return a MESSAGE_ALL_SPEAKER_RESPONSE, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.MESSAGE_ALL_SPEAKER_RESPONSE;
    }


    public List<Message> msgLst;
}
