package edu.rice.comp504.model.chatroom;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.SessionDB;
import edu.rice.comp504.model.SocketResponse;
import edu.rice.comp504.model.message.AMessage;
import edu.rice.comp504.model.message.MessageDB;
import edu.rice.comp504.model.message.PrivateMessage;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

public abstract class ChatRoom {

    private final long id;
    private final String roomName;
    private final boolean isPrivate;
    private final User admin;
    private final int roomSize;
    protected MessageDB messages;
    protected ArrayList<User> blockedUsers;
    protected ArrayList<User> users;

    /**
     * Constructor for ChatRoom.
     *
     * @param id       The id of chatRoom.
     * @param isPrivate True if chatRoom is private else false.
     * @param admin    The admin of chatRoom.
     * @param roomSize     The size of chatRoom.
     */


    public ChatRoom(long id, String roomName, boolean isPrivate, User admin, int roomSize) {
        this.id = id;
        this.roomName = roomName;
        this.isPrivate = isPrivate;
        this.admin = admin;
        this.roomSize = roomSize;
        this.messages = new MessageDB();
        this.blockedUsers = new ArrayList<User>();
        this.users = new ArrayList<User>();
        users.add(admin);
    }

    public String getRoomName() {
        return roomName;
    }

    public long getId() {
        return id;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public User getAdmin() {
        return admin;
    }

    public int getSize() {
        return roomSize;
    }

    public int getAllUsersNumber() {
        return users.size() + blockedUsers.size();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<User> getBlockedUsers() {
        return blockedUsers;
    }

    /**
     * Remove user.
     *
     * @param userId id of user to be removed.
     */
    public void removeUser(long userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                users.remove(i);
                return;
            }
        }
        for (int i = 0; i < blockedUsers.size(); i++) {
            if (blockedUsers.get(i).getId() == userId) {
                blockedUsers.remove(i);
                return;
            }
        }
    }

    /**
     * Block user from chatRoom.
     *
     * @param userId id of user to be blocked.
     */
    public void addUserToBlockList(long userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                blockedUsers.add(users.get(i));
                users.remove(i);
                break;
            }
        }
    }

    /**
     * Unblock user from chatRoom.
     *
     * @param userId id of user to be unblocked.
     */
    public void removeUserFromBlockList(long userId) {
        for (int i = 0; i < blockedUsers.size(); i++) {
            if (blockedUsers.get(i).getId() == userId) {
                users.add(blockedUsers.get(i));
                blockedUsers.remove(i);
                break;
            }
        }
    }

    /** Add a message in the Chat Room.
     * @param message The message object.
     */
    public void addNewMessage(AMessage message) {
        messages.addMessage(message);
    }

    /** Recall message from chat
     * @param messageId The messageId of message.
     */
    public void deleteMessage(long messageId) {
        messages.removeMessage(messageId);
    }

    public AMessage getMessage(long messageId) {
        return messages.getMessage(messageId);
    }

    /** check if the user join in this chatRoom
     * @param userId The userId of user.
     *
     * @return Whether the user join the room
     */
    public boolean userIsJoined(long userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                return true;
            }
        }
        for (int i = 0; i < blockedUsers.size(); i++) {
            if (blockedUsers.get(i).getId() == userId) {
                return true;
            }
        }
        return false;
    }

    /** check if the user is admin in this chatRoom
     * @param userId The userId of user.
     *
     * @return Whether the user is admin in this chatRoom
     */
    public boolean userIsAdmin(long userId) {
        return (this.admin.getId() == userId);
    }

    /**
     * Check if the user with given userId is blocked.
     * @param userId userId
     * @return if the user is blocked.
     */
    public boolean userIsBlocked(long userId) {
        for (int i = 0; i < blockedUsers.size(); i++) {
            if (blockedUsers.get(i).getId() == userId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a user into chatroom user list.
     * @param user user instance
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Get the messageList of the user.
     * @param userId userId
     * @param timestamp the time the user join the room
     * @return message list
     */
    public JsonArray getUserMessageList(long userId, Timestamp timestamp) {
        ArrayList<AMessage> allMessages = messages.getAllMessages();
        JsonArray ja = new JsonArray();
        for (int i = 0; i < allMessages.size(); i++) {
            AMessage curMsg = allMessages.get(i);
            if (curMsg.getSentAt().after(timestamp)) {
                if (curMsg.getType().equals("private")) {
                    PrivateMessage curPrivateMsg = (PrivateMessage) curMsg;
                    if (curPrivateMsg.getSenderId() == userId || curPrivateMsg.getReceiverId() == userId) {
                        JsonObject jo = new JsonObject();
                        jo.addProperty("messageId", curPrivateMsg.getId());
                        jo.addProperty("type", "new");
                        jo.addProperty("isPrivate", true);
                        jo.addProperty("content", curPrivateMsg.getText());
                        JsonObject senderJson = new JsonObject();
                        senderJson.addProperty("userId", this.getUserInfo(curPrivateMsg.getSenderId()).getId());
                        senderJson.addProperty("userName", this.getUserInfo(curPrivateMsg.getSenderId()).getUserName());
                        senderJson.addProperty("userAvatar", this.getUserInfo(curPrivateMsg.getSenderId()).getImageUrl());
                        jo.add("sender", senderJson);
                        JsonObject receiverJson = new JsonObject();
                        receiverJson.addProperty("userId", this.getUserInfo(curPrivateMsg.getReceiverId()).getId());
                        receiverJson.addProperty("userName", this.getUserInfo(curPrivateMsg.getReceiverId()).getUserName());
                        receiverJson.addProperty("userAvatar", this.getUserInfo(curPrivateMsg.getReceiverId()).getImageUrl());
                        jo.add("receiver", receiverJson);
                        ja.add(jo);
                    }
                } else {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("messageId", curMsg.getId());
                    if (curMsg.getType().equals("public")) {
                        jo.addProperty("type", "new");
                    } else {
                        jo.addProperty("type", "system");
                    }
                    jo.addProperty("isPrivate", false);
                    jo.addProperty("content", curMsg.getText());
                    if (curMsg.getType().equals("public")) {
                        JsonObject senderJson = new JsonObject();
                        senderJson.addProperty("userId", this.getUserInfo(curMsg.getSenderId()).getId());
                        senderJson.addProperty("userName", this.getUserInfo(curMsg.getSenderId()).getUserName());
                        senderJson.addProperty("userAvatar", this.getUserInfo(curMsg.getSenderId()).getImageUrl());
                        jo.add("sender", senderJson);
                    }
                    ja.add(jo);
                }
            }
        }
        return ja;
    }

    private User getUserInfo(long userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                return users.get(i);
            }
        }
        for (int i = 0; i < blockedUsers.size(); i++) {
            if (blockedUsers.get(i).getId() == userId) {
                return blockedUsers.get(i);
            }
        }
        return null;
    }

    /**
     * Broadcast socket message to all the user in this room.
     * @param response socket response
     * @param sessionDB sessionDB
     */
    public void broadcastAll(SocketResponse response, SessionDB sessionDB) {
        Map<Session,String> sessionUserMap = sessionDB.getSessionUserMap();
        Gson gson = new Gson();
        for (int i = 0; i < users.size(); i++) {
            for (Session s: sessionUserMap.keySet()) {
                if (sessionUserMap.get(s).equals(users.get(i).getUserName())) {
                    try {
                        s.getRemote().sendString(String.valueOf(gson.toJson(response)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for (int i = 0; i < blockedUsers.size(); i++) {
            for (Session s: sessionUserMap.keySet()) {
                if (sessionUserMap.get(s).equals(blockedUsers.get(i).getUserName())) {
                    try {
                        s.getRemote().sendString(String.valueOf(gson.toJson(response)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Broadcast public message in this room.
     * @param aMessage the message to be broadcast.
     * @param sessionDB sessionDB
     */
    public void broadcastPublic(AMessage aMessage, SessionDB sessionDB) {
        // prepare response msg
        User sender = this.getUserInfo(aMessage.getSenderId());
        JsonObject senderJson = new JsonObject();
        senderJson.addProperty("userId", sender.getId());
        senderJson.addProperty("userName", sender.getUserName());
        senderJson.addProperty("userAvatar", sender.getImageUrl());

        SocketResponse response = new SocketResponse(aMessage.getId(), "new", false, aMessage.getText(), senderJson, null, this.id);

        this.broadcastAll(response, sessionDB);
    }

    public void broadcastSystem(AMessage aMessage, SessionDB sessionDB) {
        SocketResponse response = new SocketResponse(aMessage.getId(), "system", false, aMessage.getText(), null, null, this.id);
        this.broadcastAll(response, sessionDB);
    }

    /**
     * Broadcast an edit message to the user in this room.
     * @param aMessage the edited message
     * @param sessionDB sessionDB
     */
    public void broadcastEdit(AMessage aMessage, SessionDB sessionDB) {
        // prepare response msg
        SocketResponse response = new SocketResponse(aMessage.getId(), "edit", false, aMessage.getText(), null, null, this.id);

        this.broadcastAll(response, sessionDB);
    }

    /**
     * Broadcast a delete message to the user in this room.
     * @param aMessage the deleted message
     * @param sessionDB sessionDB
     */
    public void broadcastDelete(AMessage aMessage, SessionDB sessionDB) {
        // prepare response msg
        SocketResponse response = new SocketResponse(aMessage.getId(), "remove", false, null, null, null, this.id);
        this.broadcastAll(response, sessionDB);
    }

    /**
     * Broadcast a private message to the user who should receive this message.
     * @param aMessage the private message
     * @param sessionDB sessionDB
     */
    public void broadcastPrivate(AMessage aMessage, SessionDB sessionDB) {
        PrivateMessage privateMessage = (PrivateMessage) aMessage;
        // prepare response msg
        User sender = this.getUserInfo(privateMessage.getSenderId());
        JsonObject senderJson = new JsonObject();
        senderJson.addProperty("userId", sender.getId());
        senderJson.addProperty("userName", sender.getUserName());
        senderJson.addProperty("userAvatar", sender.getImageUrl());

        User receiver = this.getUserInfo(privateMessage.getReceiverId());
        JsonObject receiverJson = new JsonObject();
        receiverJson.addProperty("userId", receiver.getId());
        receiverJson.addProperty("userName", receiver.getUserName());
        receiverJson.addProperty("userAvatar", receiver.getImageUrl());

        SocketResponse response = new SocketResponse(privateMessage.getId(), "new", true, privateMessage.getText(), senderJson, receiverJson, this.id);

        Map<Session,String> sessionUserMap = sessionDB.getSessionUserMap();
        Gson gson = new Gson();
        for (Session s: sessionUserMap.keySet()) {
            if (sessionUserMap.get(s).equals(sender.getUserName()) || sessionUserMap.get(s).equals(receiver.getUserName())) {
                try {
                    s.getRemote().sendString(String.valueOf(gson.toJson(response)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
