package edu.rice.comp504.model.notification;

public interface INotificationFac {
    public ANotification makeNotification(String type, String text, String content, long senderId, long chatroomId);
}
