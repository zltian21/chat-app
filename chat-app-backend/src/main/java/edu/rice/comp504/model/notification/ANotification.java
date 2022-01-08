package edu.rice.comp504.model.notification;

public abstract class ANotification {
    public enum Status {
        ACCEPTED,
        DECLINED,
        NO_STATUS
    }


    private final String type;  //  invite or system
    private final long id;
    private final String text;
    private final String content; //For Emojis
    private final long senderId; // User instance mapped to its Id
    protected Status status;

    /**
     * Notification constructor.
     */
    public ANotification(String type, long id, String text, String content, long senderId) {
        this.type = type;
        this.id = id;
        this.text = text;
        this.content = content;
        this.senderId = senderId;
        this.status = Status.NO_STATUS;
    }

    public long getNotificationId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public String getType() {
        return this.type;
    }

    public Status getStatus() {
        return this.status;
    }
}
