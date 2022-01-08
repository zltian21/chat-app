package edu.rice.comp504.model.message;

import java.sql.Timestamp;

public class SystemMessage extends AMessage{


    /**
     * Constructor for message model.
     *
     * @param type     The type of message --> public/private/system
     * @param id       The id of message.
     * @param text     The text of messages.
     * @param content  The content of message for Emoji.
     * @param senderId senderId   /// -1 for system messages.
     * @param sentAt   Time at which message was sent.
     * @param isSeen   Is message read by other user.
     *
     */
    public SystemMessage(String type, long id, String text, String content, long senderId, Timestamp sentAt, boolean isSeen) {
        super(type, id, text,content,senderId, sentAt, isSeen);
    }
}
