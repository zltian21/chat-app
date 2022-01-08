package edu.rice.comp504.model.message;

import java.sql.Timestamp;

public abstract class AMessage {
    private final String type;
    private final long id;
    private String text;
    private final String content; //For Emojis
    private final long senderId; // User instance mapped to its Id
    private final Timestamp sentAt; // Not String TimeStamp
    private final boolean isSeen; // Update to isReceived (design Decision)

    /**
     * Constructor for message model.
     *
     * @param type     The type of message --> public/private
     * @param id       The id of message.
     * @param text     The text of message.
     * @param content  The content of message for Emoji.
     * @param senderId The id of user who sent it.
     * @param sentAt   Time at which message was sent.
     * @param isSeen   Is message read by other user.
     */

    public AMessage(String type, long id, String text, String content, long senderId, Timestamp sentAt, boolean isSeen) {
        this.type = type;
        this.id = id;
        this.text = text;
        this.content = content;
        this.senderId = senderId;
        this.sentAt = sentAt;
        this.isSeen = isSeen;
    }

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContent() {
        return content;
    }

    public long getSenderId() {
        return senderId;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public boolean isSeen() {
        return isSeen;
    }
}
