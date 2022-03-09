package ca.utoronto.cs.message;

import java.util.List;

public class ChangeSpeakerRequest extends Request{
    /**
     * return a CHANGE_SPEAKER_REQUEST, type of MessageType
     * @return a CHANGE_SPEAKER_REQUEST, type of MessageType
     */
    @Override
    protected MessageType getType() {
        return MessageType.CHANGE_SPEAKER_REQUEST;
    }

    public int eventId;
    public List<String> speakerName;
}
