package edu.rice.comp504.model.notification;

import edu.rice.comp504.model.message.AMessage;

import java.util.ArrayList;

public interface INotificationDB {
    public ArrayList<ANotification> getAllNotifications();

    public ANotification getNotification(long notificationId);

    public void addNotification(ANotification notification);
}
