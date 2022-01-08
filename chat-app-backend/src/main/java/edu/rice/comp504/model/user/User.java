package edu.rice.comp504.model.user;

import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.notification.ANotification;
import edu.rice.comp504.model.notification.InvNotification;
import edu.rice.comp504.model.notification.NotificationDB;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private final long id;
    private String name;
    private String schoolName;
    private String interests;
    private String userName;
    private HashMap<ChatRoom,Timestamp> chatRooms; // This will hold the chatrooms with last timeStamp ==>HashMap<Chatroom,TimeStamp>
    private String imageUrl;
    private int age;
    private int hateSpeechCount;
    private String password;
    private NotificationDB notificationDB;

    /**
     * Constructor for user model.
     *
     * @param id         id of user.
     * @param schoolName School name of user.
     * @param interests  Interest of user.
     * @param age        Age of User (Should be above 18)
     * @param password   User Password
     * @param userName   unique username
     * @param imageUrl   Image of user.
     */

    public User(long id, String schoolName, String interests, String userName, String imageUrl, int age, String password) {
        this.id = id;
        this.schoolName = schoolName;
        this.interests = interests;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.age = age;
        this.password = password;
        this.chatRooms = new HashMap<ChatRoom,Timestamp>();
        this.hateSpeechCount = 0;
        this.notificationDB = new NotificationDB();
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSchoolName() {
        return this.schoolName;
    }

    public String getInterests() {
        return this.interests;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public int getHateSpeechCount() {
        return this.hateSpeechCount;
    }

    public int getAge() {
        return this.age;
    }

    public void leaveChatRoom(ChatRoom chatRoom) {
        this.chatRooms.remove(chatRoom);
    }

    public void joinRoom(ChatRoom chatroom, Timestamp timestamp) {
        chatRooms.put(chatroom, timestamp);
    }

    public long getId() {
        return id;
    }

    public HashMap<ChatRoom,Timestamp> getChatRooms() {
        return chatRooms;
    }

    public Timestamp getJoinTime(ChatRoom chatRoom) {
        return chatRooms.get(chatRoom);
    }

    public boolean isBanned() {
        return (this.hateSpeechCount >= 10);
    }

    public NotificationDB getNotificationDB() {
        return this.notificationDB;
    }

    public void addNotification(ANotification notification) {
        this.notificationDB.addNotification(notification);
    }

    /**
     * check if the user can be invited to certain chatroomId
     * @param chatroomId invited room id
     * @return if the user can be invited.
     */
    public boolean canBeInvited(long chatroomId) {
        ArrayList<ANotification> notifications = notificationDB.getAllNotifications();
        for (int i = 0; i < notifications.size(); i++) {
            ANotification notification = notifications.get(i);
            if (notification.getType().equals("invite")) {
                if (((InvNotification)notification).getChatroomId() == chatroomId && notification.getStatus() == ANotification.Status.NO_STATUS) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addHateSpeechCount() {
        this.hateSpeechCount += 1;
    }

}
