package edu.rice.comp504.model.notification;

public class NotificationFac implements INotificationFac{
    private static NotificationFac notificationFacIns = null;
    private static long nextNotificationId = 1;

    /**
     * Get notification factory instance.
     * @return notification factory instance.
     */
    public static NotificationFac getInstance() {
        if (notificationFacIns == null) {
            notificationFacIns = new NotificationFac();
        }
        return notificationFacIns;
    }

    /**
     * Create a notification instance.
     * @param type notification type.
     * @param text notification text
     * @param content notification content
     * @param senderId notification senderId
     * @param chatroomId notification chatroomId, use for invite notification
     */
    public ANotification makeNotification(String type, String text, String content, long senderId, long chatroomId) {
        ANotification notification = null;
        if (type.equals("invite")) {
            notification = new InvNotification(nextNotificationId, text, content, senderId, chatroomId);
            nextNotificationId += 1;
        } else if (type.equals("system")) {
            notification = new SysNotification(nextNotificationId, text, content, -1, -1);
            nextNotificationId += 1;
        }
        return notification;
    }
}
