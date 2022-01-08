package edu.rice.comp504.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.chatroom.ChatRoomDB;
import edu.rice.comp504.model.chatroom.ChatRoomFac;
import edu.rice.comp504.model.chatroom.IChatRoomDB;
import edu.rice.comp504.model.message.AMessage;
import edu.rice.comp504.model.message.IMessageDB;
import edu.rice.comp504.model.message.IMessageFac;
import edu.rice.comp504.model.message.MessageFac;
import edu.rice.comp504.model.notification.*;
import edu.rice.comp504.model.user.IUserDB;
import edu.rice.comp504.model.user.User;
import edu.rice.comp504.model.user.UserDB;
import edu.rice.comp504.model.user.UserFac;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatApp {
    private static ChatApp chatAppIns = null;
    private static UserFac userFac;
    private static UserDB userDB;
    private static ChatRoomFac chatRoomFac;
    private static ChatRoomDB chatRoomDB;
    private static IMessageFac messageFac;
    private static IMessageDB messageDB;
    private static INotificationFac notificationFac;
    private static SessionDB sessionDB;
    private static String hateSpeech = "hate speech";

    /**
     * ChatApp constructor.
     */
    private ChatApp() {
        userFac = UserFac.getInstance();
        userDB = UserDB.getInstance();
        chatRoomFac = ChatRoomFac.getInstance();
        chatRoomDB = ChatRoomDB.getInstance();
        notificationFac = NotificationFac.getInstance();
        messageFac = MessageFac.getInstance();
        sessionDB = new SessionDB();
    }

    /**
     * Get ChatApp instance.
     * @return ChatApp instance
     */
    public static ChatApp getInstance() {
        if (chatAppIns == null) {
            chatAppIns = new ChatApp();
        }
        return chatAppIns;
    }

    /**
     * User login with username and password.
     * @param username username
     * @param password password
     */
    public Response login(String username, String password) {
        Response response;
        if (SessionDB.isEmpty()) {
            if (!userDB.loginCheck(username, password)) {
                response = new Response(1, "User name or password incorrect", null);
            } else {
                long userId = userDB.getUserIdByName(username);
                response = this.getUserInfo(userId);
            }
        } else {
            if (SessionDB.sessionExist(username)) {
                response = new Response(1, "user already login", null);
            } else if (!userDB.loginCheck(username, password)) {
                response = new Response(1, "User name or password incorrect", null);
            } else {
                long userId = userDB.getUserIdByName(username);
                response = this.getUserInfo(userId);
            }
        }
        return response;
    }

    /**
     * User logout.
     * @param userId userId
     */
    public Response logout(long userId) {
        if (SessionDB.isEmpty()) {
            return new Response(0, "success", null);
        } else {
            if (SessionDB.sessionExist(userDB.getUser(userId).getUserName())) {
                SessionDB.removeSessionByUsername(userDB.getUser(userId).getUserName());
            }
            return new Response(0, "success", null);
        }
    }

    /**
     * Register a new user.
     * @param username username
     * @param password password
     * @param age age
     * @param imageUrl avatar url
     * @param interests user interests
     * @param school user school
     */
    public Response register(String username, String password, int age, String imageUrl, String interests,
                            String school) {
        if (userDB.usernameExist(username)) {
            return new Response(1, "username already exist", null);
        }
        User newUser = userFac.makeUser(school, interests, username, imageUrl, age, password);
        userDB.addUser(newUser);

        return this.getUserInfo(newUser.getId());
    }

    /**
     * Get all the public chatroom a user hasn't joined.
     * @param userId userId
     */
    public Response getPublicChatroom(long userId) {
        User user = userDB.getUser(userId);
        HashMap<ChatRoom, Timestamp> myChatroom = user.getChatRooms();
        ArrayList<ChatRoom> publicChatroomList = chatRoomDB.getPublicChatroom();

        Response response;
        JsonArray ja = new JsonArray();
        JsonObject jo;
        for (int i = 0; i < publicChatroomList.size(); i++) {
            if (!myChatroom.containsKey(publicChatroomList.get(i))) {
                jo = new JsonObject();
                jo.addProperty("chatRoomId", publicChatroomList.get(i).getId());
                jo.addProperty("chatRoomName", publicChatroomList.get(i).getRoomName());
                jo.addProperty("isPrivate", publicChatroomList.get(i).isPrivate());
                ja.add(jo);
            }
        }
        response = new Response(0, "success", ja);
        return response;
    }

    /**
     * Get the chatroom a user has joined.
     * @param userId userId
     */
    public Response getMyChatroom(long userId) {
        User user = userDB.getUser(userId);
        HashMap<ChatRoom, Timestamp> myChatroom = user.getChatRooms();

        Response response;
        JsonArray ja = new JsonArray();
        JsonObject jo;
        for (ChatRoom r: myChatroom.keySet()) {
            jo = new JsonObject();
            jo.addProperty("chatRoomId", r.getId());
            jo.addProperty("chatRoomName", r.getRoomName());
            jo.addProperty("isPrivate", r.isPrivate());
            ja.add(jo);
        }
        response = new Response(0, "success", ja);
        return response;
    }

    /**
     * Get the notification list of user with given userId.
     * @param userId userId
     */
    public Response getUserNotification(long userId) {
        User user = userDB.getUser(userId);
        ArrayList<ANotification> allNotifications = user.getNotificationDB().getAllNotifications();
        JsonArray ja = new JsonArray();
        JsonObject jo;
        for (int i = allNotifications.size() - 1; i >= 0; i--) {
            jo = new JsonObject();
            jo.addProperty("id", allNotifications.get(i).getNotificationId());
            jo.addProperty("content", allNotifications.get(i).getText());
            jo.addProperty("type", allNotifications.get(i).getType());
            jo.addProperty("status", allNotifications.get(i).getStatus().ordinal());
            ja.add(jo);
        }
        return new Response(0, "success", ja);
    }

    /**
     * User create a room.
     * @param userId userId
     * @param roomSize room size
     * @param isPrivate whether the room is private or not
     * @param roomName room name
     */
    public Response createRoom(String roomName, int roomSize, boolean isPrivate, long userId) {
        User admin = userDB.getUser(userId);
        if (chatRoomDB.roomNameExist(roomName)) {
            return new Response(1, "roomName already exist", null);
        }
        ChatRoom newRoom = chatRoomFac.makeChatRoom(roomName, isPrivate, admin, roomSize);
        chatRoomDB.addChatRoom(newRoom);
        Date date = new Date();
        admin.joinRoom(newRoom, new Timestamp(date.getTime()));
        return new Response(0,"success", null);
    }

    /**
     * Get the list of unblocked user.
     * @param userId userId
     * @param chatroomId chatroomId
     */
    public Response getListofUser(long chatroomId, long userId) {
        ChatRoom chatRoom = chatRoomDB.getChatRoom(chatroomId);
        if (chatRoom == null) {
            return new Response(1, "chatroom not exist", null);
        }
        if (!chatRoom.userIsJoined(userId)) {
            return new Response(1, "user do not join this chatroom", null);
        }
        ArrayList<User> users = chatRoom.getUsers();
        JsonArray ja = new JsonArray();
        JsonObject jo;
        for (int i = 0; i < users.size(); i++) {
            jo = new JsonObject();
            jo.addProperty("userId", users.get(i).getId());
            jo.addProperty("userName", users.get(i).getUserName());
            jo.addProperty("userAvatar", users.get(i).getImageUrl());
            ja.add(jo);
        }
        return new Response(0, "success", ja);
    }

    /**
     * Get the list of blocked user.
     * @param userId userId
     * @param chatroomId chatroomId
     */
    public Response getListofBlockedUser(long chatroomId, long userId) {
        ChatRoom chatRoom = chatRoomDB.getChatRoom(chatroomId);
        if (chatRoom == null) {
            return new Response(1, "chatroom not exist", null);
        }
        if (!chatRoom.userIsJoined(userId)) {
            return new Response(1, "user do not join this chatroom", null);
        }
        ArrayList<User> blockedUsers = chatRoom.getBlockedUsers();
        JsonArray ja = new JsonArray();
        JsonObject jo;
        for (int i = 0; i < blockedUsers.size(); i++) {
            jo = new JsonObject();
            jo.addProperty("userId", blockedUsers.get(i).getId());
            jo.addProperty("userName", blockedUsers.get(i).getUserName());
            jo.addProperty("userAvatar", blockedUsers.get(i).getImageUrl());
            ja.add(jo);
        }
        return new Response(0, "success", ja);
    }

    /**
     * Get the chatroom info and message list for user with given userId.
     * @param userId userId
     * @param chatroomId chatroomId
     */
    public Response getChatRoom(long chatroomId, long userId) {
        ChatRoom chatRoom = chatRoomDB.getChatRoom(chatroomId);
        if (chatRoom == null) {
            return new Response(1, "chatroom not exist", null);
        }
        if (chatRoom.userIsJoined(userId)) {
            JsonArray ja = new JsonArray();
            JsonObject jo = new JsonObject();
            jo.addProperty("chatRoomId", chatRoom.getId());
            jo.addProperty("chatRoomName", chatRoom.getRoomName());
            jo.addProperty("isPrivate", chatRoom.isPrivate());
            jo.addProperty("isAdmin", chatRoom.userIsAdmin(userId));
            jo.addProperty("isBlocked", chatRoom.userIsBlocked(userId));
            JsonArray messageList = chatRoom.getUserMessageList(userId, userDB.getUser(userId).getJoinTime(chatRoom));
            jo.add("messages", messageList);
            ja.add(jo);
            return new Response(0, "success", ja);
        } else {
            return new Response(1, "user do not join the chatroom", null);
        }
    }

    /**
     * User leave a room with some reason.
     * @param userId userId
     * @param leaveReason leave reason
     * @param chatroomId chatroomId
     */
    public Response leaveChatRoom(long chatroomId, long userId, String leaveReason) {
        ChatRoom chatRoom = chatRoomDB.getChatRoom(chatroomId);
        if (chatRoom == null) {
            return new Response(1, "chatroom not exist", null);
        }
        if (chatRoom.userIsJoined(userId)) {
            if (chatRoom.userIsAdmin(userId)) {
                // admin leave the room,
                String message = "admin left chatroom [" + chatRoom.getRoomName() + "]";
                ANotification notification = notificationFac.makeNotification("system", message, "", -1, -1);
                for (User u: chatRoom.getUsers()) {
                    u.leaveChatRoom(chatRoom);
                    if (u.getId() != userId) {
                        u.addNotification(notification);
                    }
                }
                for (User u: chatRoom.getBlockedUsers()) {
                    u.leaveChatRoom(chatRoom);
                    u.addNotification(notification);
                }
                chatRoomDB.removeChatRoom(chatroomId);
            } else {
                User user = userDB.getUser(userId);
                user.leaveChatRoom(chatRoom);
                chatRoom.removeUser(userId);
                // create new message
                Date date = new Date();
                if (leaveReason.equals("")) {
                    leaveReason = "User" + user.getUserName() + " leave the room";
                }
                AMessage aMessage = messageFac.makeMessage("system", leaveReason, "", -1, -1, new Timestamp(date.getTime()), true);
                chatRoom.addNewMessage(aMessage);

                chatRoom.broadcastSystem(aMessage, sessionDB);
            }
            return new Response(0, "success", null);
        } else {
            return new Response(1, "user do not join the chatroom", null);
        }
    }

    /**
     * User join a room.
     * @param userId userId
     * @param chatroomId chatroomId
     */
    public Response joinRoom(long chatroomId, long userId) {
        User user = userDB.getUser(userId);
        ChatRoom chatroom = chatRoomDB.getChatRoom(chatroomId);
        if (chatroom == null) {
            return new Response(1, "chatroom not exist", null);
        }
        if (chatroom.userIsJoined(userId)) {
            return new Response(1, "user already join the chatroom", null);
        } else if (chatroom.getSize() <= chatroom.getAllUsersNumber()) {
            return new Response(1, "this chatroom is already full", null);
        } else {
            chatroom.addUser(user);
            Date date = new Date();
            user.joinRoom(chatroom, new Timestamp(date.getTime()));
            // send a system message
            date = new Date();
            String msg = "User " + user.getUserName() + " joined the room.";
            AMessage aMessage = messageFac.makeMessage("system", msg, "", -1, -1, new Timestamp(date.getTime()), true);
            chatroom.addNewMessage(aMessage);

            chatroom.broadcastSystem(aMessage, sessionDB);

            return new Response(0, "success", null);
        }
    }

    /**
     * User accept/decline an invitation.
     * @param userId userId
     * @param accept accept or declined the invitation
     * @param notificationId notificationId
     */
    public Response opInvitation(long notificationId, boolean accept, long userId) {
        ANotification notification = userDB.getUser(userId).getNotificationDB().getNotification(notificationId);
        if (notification == null) {
            return new Response(1, "can not find this invitation", null);
        } else if (!notification.getType().equals("invite") || notification.getStatus() != ANotification.Status.NO_STATUS) {
            return new Response(1, "invitation status error", null);
        } else {
            if (accept) {
                ((InvNotification)notification).setStatus("accept");
                return this.joinRoom(((InvNotification)notification).getChatroomId(), userId);
            } else {
                ((InvNotification)notification).setStatus("declined");
                return new Response(0, "success", null);
            }
        }
    }

    /**
     * Admin removed a user.
     * @param userId userId
     * @param removedUserId removed userId
     * @param chatroomId chatroomId
     */
    public Response removeUser(long userId, long removedUserId, long chatroomId) {
        ChatRoom chatroom = chatRoomDB.getChatRoom(chatroomId);
        if (chatroom == null) {
            return new Response(1, "chatroom not exist", null);
        }
        if (chatroom.userIsAdmin(userId)) {
            User removedUser = userDB.getUser(removedUserId);
            String reasonString = "User " + removedUser.getUserName() + " was removed by Admin";
            Response result = this.leaveChatRoom(chatroomId, removedUserId, reasonString);
            if (result.getErrCode() != 0) {
                return result;
            }

            String msg = "You have been removed from chatroom [" + chatroom.getRoomName() + "] by Admin";
            ANotification notification = notificationFac.makeNotification("system", msg, "", -1, -1);
            removedUser.addNotification(notification);
            return new Response(0, "success", null);
        } else {
            return new Response(1, "user is not admin", null);
        }
    }

    /**
     * Admin blocked a user.
     * @param userId admin userId
     * @param blockUserId blocked userId
     * @param chatroomId chatroomId
     */
    public Response blockUser(long userId, long blockUserId, long chatroomId) {
        ChatRoom chatroom = chatRoomDB.getChatRoom(chatroomId);
        if (chatroom == null) {
            return new Response(1, "chatroom not exist", null);
        }
        if (!chatroom.userIsAdmin(userId)) {
            return new Response(1, "user is not admin", null);
        } else if (!chatroom.userIsJoined(blockUserId)) {
            return new Response(1, "blocked user does not join the room", null);
        } else {
            chatroom.addUserToBlockList(blockUserId);
            String msg = "You have been blocked in chatroom [" + chatroom.getRoomName() + "] by the admin";
            ANotification notification = notificationFac.makeNotification("system", msg, "", -1, -1);
            userDB.getUser(blockUserId).addNotification(notification);
            // add system message
            Date date = new Date();
            msg = "User " + userDB.getUser(blockUserId).getUserName() + " has been blocked by admin";
            AMessage aMessage = messageFac.makeMessage("system", msg, "", -1, -1, new Timestamp(date.getTime()), true);
            chatroom.addNewMessage(aMessage);

            chatroom.broadcastSystem(aMessage, sessionDB);
            return new Response(0, "success", null);
        }
    }

    /**
     * Get all the user that can be invited to the private room.
     * @param userId userId
     * @param chatroomId chatroomId
     */
    public Response getListofUserToInvite(long userId, long chatroomId) {
        ChatRoom chatroom = chatRoomDB.getChatRoom(chatroomId);
        if (chatroom == null) {
            return new Response(1, "chatroom not exist", null);
        }
        if (!chatroom.userIsAdmin(userId)) {
            return new Response(1, "user is not admin", null);
        } else {
            ArrayList<User> allUser = userDB.getAllUser();
            JsonArray ja = new JsonArray();
            JsonObject jo;
            for (int i = 0; i < allUser.size(); i++) {
                if (!chatroom.userIsJoined(allUser.get(i).getId())) {
                    jo = new JsonObject();
                    jo.addProperty("userId", allUser.get(i).getId());
                    jo.addProperty("userName", allUser.get(i).getUserName());
                    jo.addProperty("userAvatar", allUser.get(i).getImageUrl());
                    ja.add(jo);
                }
            }
            return new Response(0, "success", ja);
        }
    }

    /**
     * User send an invitation to other user to join the private room.
     * @param userId userId
     * @param receiverId the user who is invited
     * @param chatroomId chatroomId
     */
    public Response sendInvite(long userId, long receiverId, long chatroomId) {
        ChatRoom chatroom = chatRoomDB.getChatRoom(chatroomId);
        if (chatroom == null) {
            return new Response(1, "chatroom not exist", null);
        } else if (!chatroom.isPrivate()) {
            return new Response(1, "only private chatroom can send invitation", null);
        } else if (!chatroom.userIsAdmin(userId)) {
            return new Response(1, "user is not admin", null);
        } else if (chatroom.userIsJoined(receiverId)) {
            return new Response(1, "invited user is already in the chatroom", null);
        } else if (!userDB.getUser(receiverId).canBeInvited(chatroomId)) {
            return new Response(1, "user already been invited", null);
        } else {
            String msg = "You have been invited to join chatroom [" + chatroom.getRoomName() + "]";
            ANotification notification = notificationFac.makeNotification("invite", msg, "", userId, chatroomId);
            userDB.getUser(receiverId).addNotification(notification);
            return new Response(0, "success", null);
        }
    }

    /**
     * Admin unblock a user.
     * @param userId admin userId
     * @param unblockUserId unblocked userId
     * @param chatroomId chatroomId
     */
    public Response unblockUser(long userId, long unblockUserId, long chatroomId) {
        ChatRoom chatroom = chatRoomDB.getChatRoom(chatroomId);
        if (chatroom == null) {
            return new Response(1, "chatroom not exist", null);
        }
        if (!chatroom.userIsAdmin(userId)) {
            return new Response(1, "user is not admin", null);
        } else if (!chatroom.userIsJoined(unblockUserId)) {
            return new Response(1, "unblocked user does not join the room", null);
        } else {
            chatroom.removeUserFromBlockList(unblockUserId);
            String msg = "You have been unblocked in chatroom [" + chatroom.getRoomName() + "] by the admin";
            ANotification notification = notificationFac.makeNotification("system", msg, "", -1, -1);
            userDB.getUser(unblockUserId).addNotification(notification);
            // TODO add system message
            Date date = new Date();
            msg = "User " + userDB.getUser(unblockUserId).getUserName() + " has been unblocked by admin";
            AMessage aMessage = messageFac.makeMessage("system", msg, "", -1, -1, new Timestamp(date.getTime()), true);
            chatroom.addNewMessage(aMessage);

            chatroom.broadcastSystem(aMessage, sessionDB);
            return new Response(0, "success", null);
        }
    }

    /**
     * User with given userId edit a message.
     * @param userId userId
     * @param messageId edited messageId
     * @param chatroomId chatroomId
     * @param content edited content
     */
    public Response editMessage(long userId, long messageId, long chatroomId, String content) {
        ChatRoom chatRoom = chatRoomDB.getChatRoom(chatroomId);
        if (chatRoom == null) {
            return new Response(1, "chatroom not exist", null);
        }

        AMessage message = chatRoom.getMessage(messageId);
        if (message == null) {
            return new Response(1, "can not find this message", null);
        } else if (message.getSenderId() != userId) {
            return new Response(1, "this message does not belong to the user", null);
        } else {
            // detect hate speech
            if (content.toLowerCase().contains(hateSpeech.toLowerCase())) {
                userDB.getUser(userId).addHateSpeechCount();
                content = content.replace(hateSpeech, "****");
            }
            message.setText(content);
            chatRoom.broadcastEdit(message, sessionDB);
            return new Response(0, "success", null);
        }
    }

    /**
     * User with given userId remove a message.
     * @param userId userId
     * @param messageId removed messageId
     * @param chatroomId chatroomId
     */
    public Response removeMessage(long userId, long messageId, long chatroomId) {
        ChatRoom chatRoom = chatRoomDB.getChatRoom(chatroomId);
        if (chatRoom == null) {
            return new Response(1, "chatroom not exist", null);
        }

        AMessage message = chatRoom.getMessage(messageId);
        if (message == null) {
            return new Response(1, "can not find this message", null);
        } else if (message.getSenderId() != userId && !chatRoom.userIsAdmin(userId)) {
            return new Response(1, "user can not remove this message", null);
        } else {
            chatRoom.broadcastDelete(message, sessionDB);
            chatRoom.deleteMessage(messageId);
            return new Response(0, "success", null);
        }
    }

    /**
     * user with given userId leave all the chatroom.
     * @param userId userId
     */
    public Response leaveAllChatroom(long userId) {
        User user = userDB.getUser(userId);
        ArrayList<ChatRoom> chatroomsList = new ArrayList<ChatRoom>();
        HashMap<ChatRoom,Timestamp> chatrooms = user.getChatRooms();
        for (ChatRoom c:chatrooms.keySet()) {
            chatroomsList.add(c);
        }
        for (int i = 0; i < chatroomsList.size(); i++) {
            this.leaveChatRoom(chatroomsList.get(i).getId(), userId, "User leave chatroom");
        }
        return new Response(0, "success", null);
    }

    /**
     * Get user info with given userId.
     * @param userId userId
     */
    public Response getUserInfo(long userId) {
        User user = userDB.getUser(userId);
        Response response;
        if (user != null) {
            JsonObject jo = new JsonObject();
            jo.addProperty("id", user.getId());
            jo.addProperty("username", user.getUserName());
            jo.addProperty("imageUrl", user.getImageUrl());
            jo.addProperty("age", user.getAge());
            jo.addProperty("interests", user.getInterests());
            jo.addProperty("hateSpeechCount", user.getHateSpeechCount());
            jo.addProperty("schoolName",user.getSchoolName());
            JsonArray ja = new JsonArray();
            ja.add(jo);
            response = new Response(0, "success", ja);
        } else {
            response = new Response(1, "userId not found", null);
        }
        return response;
    }

    /**
     * Check if user is banned with given userId.
     * @param userId userId
     */
    public boolean isBanned(long userId) {
        User user = userDB.getUser(userId);
        return user.isBanned();
    }

    /**
     * Check if user exist with given userId.
     * @param userId userId
     */
    public boolean userExist(long userId) {
        if (userDB.getUser(userId) == null) {
            return false;
        }
        return true;
    }

    /**
     * Action when server receive a socket message from client.
     * @param message message
     */
    public void receivedMsg(String message) {
        JsonObject convertedObject = new Gson().fromJson(message, JsonObject.class);
        boolean isPrivate = convertedObject.get("isPrivate").getAsBoolean();
        String type = isPrivate ? "private" : "public";
        String content = convertedObject.get("content").getAsString();
        long senderId = convertedObject.get("senderId").getAsLong();
        long receiverId = convertedObject.get("receiverId").getAsLong();
        long roomId = convertedObject.get("roomId").getAsLong();

        ChatRoom chatRoom = chatRoomDB.getChatRoom(roomId);
        if (chatRoom == null) {
            return;
        }
        if (!chatRoom.userIsJoined(senderId)) {
            return;
        }
        if (chatRoom.userIsBlocked(senderId)) {
            return;
        }
        // hate speech detect
        if (content.toLowerCase().contains(hateSpeech.toLowerCase())) {
            userDB.getUser(senderId).addHateSpeechCount();
            content = content.replace(hateSpeech, "****");
        }
        // create new message
        Date date = new Date();
        AMessage aMessage = messageFac.makeMessage(type, content, "", senderId, receiverId, new Timestamp(date.getTime()), true);
        chatRoom.addNewMessage(aMessage);

        // broadcast message
        if (type.equals("public")) {
            chatRoom.broadcastPublic(aMessage, sessionDB);
        } else if (type.equals("private")) {
            if (!chatRoom.userIsJoined(receiverId)) {
                return;
            }
            chatRoom.broadcastPrivate(aMessage, sessionDB);
        }
    }

}
