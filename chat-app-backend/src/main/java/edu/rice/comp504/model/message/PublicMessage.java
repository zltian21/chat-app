package edu.rice.comp504.model.message;

import java.sql.Timestamp;

public class PublicMessage extends AMessage{
    /**
     * Constructor for message model.
     *
     * @param type     The type of message --> public/private/notification
     * @param id       The id of message.
     * @param text     The text of message.
     * @param content  The content of message for Emoji.
     * @param senderId The id of user who sent it.
     * @param sentAt   Time at which message was sent.
     * @param isSeen   Is message read by other user.
     */
    public PublicMessage(String type, long id, String text, String content, long senderId, Timestamp sentAt, boolean isSeen) {
        super(type, id, text, content, senderId, sentAt, isSeen);
    }
}
