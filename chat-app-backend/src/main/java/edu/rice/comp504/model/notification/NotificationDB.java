package edu.rice.comp504.model.notification;

import java.util.ArrayList;

public class NotificationDB implements INotificationDB{
    private ArrayList<ANotification> allNotifications;

    public NotificationDB() {
        allNotifications = new ArrayList<ANotification>();
    }

    public ArrayList<ANotification> getAllNotifications() {
        return this.allNotifications;
    }

    /**
     * Get the notification instance with given notification id.
     * @return A notification instance.
     */
    public ANotification getNotification(long notificationId) {
        for (int i = 0; i < allNotifications.size(); i++) {
            if (allNotifications.get(i).getNotificationId() == notificationId) {
                return allNotifications.get(i);
            }
        }
        return null;
    }

    public void addNotification(ANotification notification) {
        this.allNotifications.add(notification);
    }

}
