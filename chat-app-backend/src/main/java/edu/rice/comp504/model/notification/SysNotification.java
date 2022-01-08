package edu.rice.comp504.model.notification;

public class SysNotification extends ANotification{
    public SysNotification(long id, String text, String content, long senderId, long chatroomId) {
        super("system", id, text, content, senderId);
    }
}
