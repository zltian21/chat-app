package edu.rice.comp504.model.message;

import java.sql.Timestamp;

public interface IMessageFac {

    public AMessage makeMessage(String type, String text, String content, long senderId, long receiverId, Timestamp sentAt, boolean isSeen);
}
