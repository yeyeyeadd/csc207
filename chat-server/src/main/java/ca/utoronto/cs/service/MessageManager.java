package ca.utoronto.cs.service;

import ca.utoronto.cs.dao.IMessageDao;
import ca.utoronto.cs.dao.MessageDao;
import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.entity.Message;
import ca.utoronto.cs.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MessageManager {
    private final Logger logger = LogManager.getLogger();
    private IMessageDao messageDao = null;

    public void setMessageDao(IMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    /**
     * create a message by following info.
     * @param sender user who send message
     * @param receiver user who receive message
     * @param message content
     * @return a message
     */
    public Message createMessage(User sender, User receiver, String message){
        Message msg = new Message();
        msg.setFromUserId(sender.getId());
        msg.setToUserId(receiver.getId());
        msg.setContent(message);
        msg.setTime(new Date());
        messageDao.save(msg);
        return msg;
    }

    /**
     * create a message for all user who is in the event.
     * @param event a event you want to send message
     * @param sender the speaker
     * @param receiver all user in the event
     * @param message content
     * @return a message
     */
    public Message createMessage(Event event, User sender, User receiver, String message){
        Message msg = new Message();
        msg.setFromUserId(sender.getId());
        msg.setToUserId(receiver.getId());
        msg.setContent(message);
        msg.setTime(new Date());
        msg.setEventId(event.getId());
        messageDao.save(msg);
        return msg;
    }

    /**
     * return a list of message for the user.
     * @param userId user.
     * @return a list of message.
     */
    public List<Message> getMessagesByUserId(Integer userId){
        return messageDao.getAll().stream()
                .filter(message -> (message.getFromUserId() == userId || message.getToUserId() == userId))
                .collect(Collectors.toList());
    }

    /**
     * return a list of message from the event
     * @param event giving event
     * @return a list of message from the event
     */
    public List<Message> getMessagesByEvent(Event event) {
        return messageDao.getByEventId(event.getId());
    }
}

