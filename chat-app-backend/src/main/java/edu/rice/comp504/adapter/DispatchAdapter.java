package edu.rice.comp504.adapter;

import edu.rice.comp504.model.ChatApp;
import edu.rice.comp504.model.Response;
import edu.rice.comp504.model.user.UserDB;

public class DispatchAdapter {

    private ChatApp chatApp;

    public DispatchAdapter() {
        chatApp = ChatApp.getInstance();
    }

    public Response login(String username, String password) {
        return chatApp.login(username, password);
    }

    // May change return type to a Response object
    public Response register(String username, String password, int age, String imageUrl, String interests,
                            String school) {
        return chatApp.register(username, password, age, imageUrl, interests, school);
    }

    public Response logout(long userId) {
        return chatApp.logout(userId);
    }

    public Response getPublicChatroom(long userId) {
        return chatApp.getPublicChatroom(userId);
    }

    public Response getMyChatroom(long userId) {
        return chatApp.getMyChatroom(userId);
    }

    public Response getUserNotification(long userId) {
        return chatApp.getUserNotification(userId);
    }

    public Response createRoom(String roomName, int roomSize, boolean isPrivate, long userId) {
        return chatApp.createRoom(roomName, roomSize, isPrivate, userId);
    }

    public Response getListofUser(long chatroomId, long userId) {
        return chatApp.getListofUser(chatroomId, userId);
    }

    public Response getListofBlockedUser(long chatroomId, long userId) {
        return chatApp.getListofBlockedUser(chatroomId, userId);
    }

    public Response getChatRoom(long chatroomId, long userId) {
        return chatApp.getChatRoom(chatroomId, userId);
    }

    public Response leaveChatRoom(long chatroomId, long userId) {
        return chatApp.leaveChatRoom(chatroomId, userId, "");
    }

    public Response joinRoom(long chatroomId, long userId) {
        return chatApp.joinRoom(chatroomId, userId);
    }

    public Response opInvitation(long notificationId, boolean accept, long userId) {
        return chatApp.opInvitation(notificationId, accept, userId);
    }

    public Response removeUser(long userId, long removedUserId, long chatroomId) {
        return chatApp.removeUser(userId, removedUserId, chatroomId);
    }

    public Response blockUser(long userId, long blockUserId, long chatroomId) {
        return chatApp.blockUser(userId, blockUserId, chatroomId);
    }

    public Response getListofUserToInvite(long userId, long chatroomId) {
        return chatApp.getListofUserToInvite(userId, chatroomId);
    }

    public Response sendInvite(long userId, long receiverId, long chatroomId) {
        return chatApp.sendInvite(userId, receiverId, chatroomId);
    }

    public Response unblockUser(long userId, long unblockUserId, long chatroomId) {
        return chatApp.unblockUser(userId, unblockUserId, chatroomId);
    }

    public Response editMessage(long userId, long messageId, long chatroomId, String content) {
        return chatApp.editMessage(userId, messageId, chatroomId, content);
    }

    public Response removeMessage(long userId, long messageId, long chatroomId) {
        return chatApp.removeMessage(userId, messageId, chatroomId);
    }

    public Response leaveAllChatroom(long userId) {
        return chatApp.leaveAllChatroom(userId);
    }

    public Response getUserInfo(long userId) {
        return chatApp.getUserInfo(userId);
    }

    public boolean isBanned(long userId) {
        return chatApp.isBanned(userId);
    }

    public boolean userExist(long userId) {
        return chatApp.userExist(userId);
    }
}
