package edu.rice.comp504.model.notification;

public class InvNotification extends ANotification{

    private final long chatroomId;

    public InvNotification(long id, String text, String content, long senderId, long chatroomId) {
        super("invite", id, text, content, senderId);
        this.chatroomId = chatroomId;
    }

    /**
     * Set the status of the notification.
     * @param status notification status.
     */
    public void setStatus(String status) {
        if (status.equals("accept")) {
            this.status = Status.ACCEPTED;
        } else if (status.equals("declined")) {
            this.status = Status.DECLINED;
        }
    }

    public Status getStatus() {
        return this.status;
    }

    public long getChatroomId() {
        return this.chatroomId;
    }
}
