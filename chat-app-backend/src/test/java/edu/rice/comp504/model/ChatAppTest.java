package edu.rice.comp504.model;

import edu.rice.comp504.adapter.DispatchAdapter;
import edu.rice.comp504.adapter.WebSocketAdapter;
import edu.rice.comp504.model.chatroom.ChatRoomDB;
import edu.rice.comp504.model.notification.NotificationDB;
import edu.rice.comp504.model.notification.NotificationFac;
import edu.rice.comp504.model.user.UserDB;
import junit.framework.TestCase;
import org.junit.Test;

public class ChatAppTest extends TestCase {
    DispatchAdapter dis = new DispatchAdapter();

    @Test
    public void testUserExist() {
        boolean testRes;
        testRes = dis.userExist(1);
        assertFalse("User Exist Test F", testRes);
        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();
        testRes = dis.userExist(tempID);
        assertTrue("User Exist Test T", testRes);
        UserDB.getInstance().removeUserById(tempID);
    }

    @Test
    public void testUserBanned() {
        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        boolean testRes;
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();
        testRes = dis.isBanned(tempID);
        assertFalse("User Banned Test F", testRes);

        for (int i = 0; i < 10; i++) {
            UserDB.getInstance().getUser(tempID).addHateSpeechCount();
        }
        testRes = dis.isBanned(tempID);
        assertTrue("User Banned Test T", testRes);
        UserDB.getInstance().removeUserById(tempID);
    }

    @Test
    public void testLogin() { // Session
        WebSocketAdapter wsa = new WebSocketAdapter();

        Response testResponse;
        testResponse = dis.login("TEST", "TEST");
        assertEquals("Login Test Not Registered", "User name or password incorrect", testResponse.getErrMsg());

        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();
        testResponse = dis.login("Usr1", "Wrong");
        assertEquals("Login Test Wrong Password", "User name or password incorrect", testResponse.getErrMsg());

        testResponse = dis.login("Usr1", "password");
        assertEquals("Login Test Correct Password", "success", testResponse.getErrMsg());

        UserDB.getInstance().removeUserById(tempID);
    }

    @Test
    public void testLogout() { // Session
        ChatApp tempApp = ChatApp.getInstance();
        Response testResponse;
        testResponse = tempApp.logout(999);
        assertEquals("Logout Test Not ", "user do not login", testResponse.getErrMsg());
    }

    @Test
    public void testRegister() {
        Response testResponse;
        testResponse = dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();
        assertEquals("Register Test success", "success", testResponse.getErrMsg());

        testResponse = dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        assertEquals("Register Test user exists", "username already exist", testResponse.getErrMsg());

        testResponse = dis.register("Usr2", "password", 20, "invalid", "Test", "Rice University");
        assertEquals("Register Test success", "success", testResponse.getErrMsg());
        long tempID2 = UserDB.getInstance().getAllUser().get(1).getId();

        UserDB.getInstance().removeUserById(tempID2);
        UserDB.getInstance().removeUserById(tempID);
    }

    @Test
    public void testCreateRoom() {
        Response testResponse;
        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();

        testResponse = dis.createRoom("testRoom", 10, false, tempID);
        assertEquals("Create Room Test success", "success", testResponse.getErrMsg());
        long tempRoomId = ChatRoomDB.getInstance().getPublicChatroom().get(0).getId();

        testResponse = dis.createRoom("testRoom", 5, false, tempID);
        assertEquals("Create Room Test Room name duplicate", "roomName already exist", testResponse.getErrMsg());

        ChatRoomDB.getInstance().removeChatRoom(tempRoomId);
        UserDB.getInstance().removeUserById(tempID);
    }

    @Test
    public void testGetPublicChatrooms() {
        Response testResponse;
        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();
        dis.register("Usr2", "password", 20, "invalid", "Test", "Rice University");
        long tempIDSec = UserDB.getInstance().getAllUser().get(1).getId();

        dis.createRoom("testRoom", 10, false, tempID);
        long tempRoomId = ChatRoomDB.getInstance().getPublicChatroom().get(0).getId();
        testResponse = dis.getPublicChatroom(tempIDSec);
        assertEquals("Get Public Room Test", "success", testResponse.getErrMsg());

        ChatRoomDB.getInstance().removeChatRoom(tempRoomId);
        UserDB.getInstance().removeUserById(tempID);
        UserDB.getInstance().removeUserById(tempIDSec);
    }

    @Test
    public void testGetMyChatrooms() {
        Response testResponse;
        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();

        dis.createRoom("testRoom", 10, false, tempID);
        long tempRoomId = ChatRoomDB.getInstance().getPublicChatroom().get(0).getId();
        testResponse = dis.getMyChatroom(tempID);
        assertEquals("Get My Room Test", "success", testResponse.getErrMsg());

        ChatRoomDB.getInstance().removeChatRoom(tempRoomId);
        UserDB.getInstance().removeUserById(tempID);
    }

    @Test
    public void testGetUserNotification() {
        Response testResponse;
        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();

        dis.register("Usr2", "password", 20, "invalid", "Test", "Rice University");
        long tempIDSec = UserDB.getInstance().getAllUser().get(1).getId();

        dis.createRoom("testRoom", 10, true, tempID);
        long tempRoomId = ChatRoomDB.getInstance().getPrivateChatroom().get(0).getId();

        assertEquals("Get User Notification Test NotificationDB Empty",
                0, UserDB.getInstance().getUser(tempIDSec).getNotificationDB().getAllNotifications().size());
        dis.sendInvite(tempID, tempIDSec, tempRoomId);
        assertEquals("Get User Notification Test NotificationDB One",
                1, UserDB.getInstance().getUser(tempIDSec).getNotificationDB().getAllNotifications().size());

        testResponse = dis.getUserNotification(tempIDSec);
        assertEquals("Get User Notification Test", "success", testResponse.getErrMsg());

        ChatRoomDB.getInstance().removeChatRoom(tempRoomId);
        UserDB.getInstance().removeUserById(tempID);
        UserDB.getInstance().removeUserById(tempIDSec);
    }

    @Test
    public void testGetListofUser() {
        Response testResponse;
        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();

        testResponse = dis.getListofUser(13, tempID);
        assertEquals("Get List of User Test: Room not exist", "chatroom not exist", testResponse.getErrMsg());

        dis.createRoom("testRoom", 10, false, tempID);
        long tempRoomId = ChatRoomDB.getInstance().getPublicChatroom().get(0).getId();

        dis.register("Usr2", "password", 20, "invalid", "Test", "Rice University");
        long tempIDSec = UserDB.getInstance().getAllUser().get(1).getId();

        testResponse = dis.getListofUser(tempRoomId, tempIDSec);
        assertEquals("Get List of User Test: Room not joined", "user do not join this chatroom", testResponse.getErrMsg());

        testResponse = dis.getListofUser(tempRoomId, tempID);
        assertEquals("Get List of User Test: success", "success", testResponse.getErrMsg());

        ChatRoomDB.getInstance().removeChatRoom(tempRoomId);
        UserDB.getInstance().removeUserById(tempID);
        UserDB.getInstance().removeUserById(tempIDSec);
    }

    @Test
    public void testGetListofBlockedUser() {
        Response testResponse;
        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();

        testResponse = dis.getListofBlockedUser(13, tempID);
        assertEquals("Get List of Blocked User Test: Room not exist", "chatroom not exist", testResponse.getErrMsg());

        dis.createRoom("testRoom", 10, false, tempID);
        long tempRoomId = ChatRoomDB.getInstance().getPublicChatroom().get(0).getId();

        dis.register("Usr2", "password", 20, "invalid", "Test", "Rice University");
        long tempIDSec = UserDB.getInstance().getAllUser().get(1).getId();

        testResponse = dis.getListofBlockedUser(tempRoomId, tempIDSec);
        assertEquals("Get List of Blocked User Test: Room not joined", "user do not join this chatroom", testResponse.getErrMsg());

        dis.joinRoom(tempRoomId, tempIDSec);
        dis.blockUser(tempID, tempIDSec, tempRoomId);

        testResponse = dis.getListofBlockedUser(tempRoomId, tempID);
        assertEquals("Get List of Blocked User Test: success", "success", testResponse.getErrMsg());

        ChatRoomDB.getInstance().removeChatRoom(tempRoomId);
        UserDB.getInstance().removeUserById(tempID);
        UserDB.getInstance().removeUserById(tempIDSec);
    }

    @Test
    public void testGetChatRoom() {
        Response testResponse;
        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();

        testResponse = dis.getChatRoom(999, tempID);
        assertEquals("Get ChatRoom Test: not exist", "chatroom not exist", testResponse.getErrMsg());

        dis.createRoom("testRoom", 10, false, tempID);
        long tempRoomId = ChatRoomDB.getInstance().getPublicChatroom().get(0).getId();
        testResponse = dis.getChatRoom(tempRoomId, tempID);
        assertEquals("Get ChatRoom Test: success", "success", testResponse.getErrMsg());

        dis.register("Usr2", "password", 20, "invalid", "Test", "Rice University");
        long tempIDSec = UserDB.getInstance().getAllUser().get(1).getId();
        testResponse = dis.getChatRoom(tempRoomId, tempIDSec);
        assertEquals("Get ChatRoom Test: not joined", "user do not join the chatroom", testResponse.getErrMsg());

        ChatRoomDB.getInstance().removeChatRoom(tempRoomId);
        UserDB.getInstance().removeUserById(tempID);
        UserDB.getInstance().removeUserById(tempIDSec);
    }

    @Test
    public void testMsgToClientSender() {
        MsgToClientSender.broadcastMessage("User1", "TEST MESSAGE");
    }

    @Test
    public void testLeaveChatRoom() {
        Response testResponse;

        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();
        testResponse = dis.leaveChatRoom(999, tempID);
        assertEquals("Leave ChatRoom Test: room not exist", "chatroom not exist", testResponse.getErrMsg());

        dis.createRoom("testRoom", 10, false, tempID);
        long tempRoomId = ChatRoomDB.getInstance().getPublicChatroom().get(0).getId();

        dis.register("Usr2", "password", 20, "invalid", "Test", "Rice University");
        long tempIDSec = UserDB.getInstance().getAllUser().get(1).getId();

        // TODO: Join Chat room system problem
        dis.joinRoom(tempRoomId, tempIDSec);
        testResponse = dis.leaveChatRoom(tempRoomId, tempIDSec);
        assertEquals("Leave ChatRoom Test member", "success", testResponse.getErrMsg());

        testResponse = dis.leaveChatRoom(tempRoomId, tempIDSec);
        assertEquals("Leave ChatRoom Test member not joined", "user do not join the chatroom", testResponse.getErrMsg());

        dis.joinRoom(tempRoomId, tempIDSec);

        dis.register("Usr3", "password", 20, "invalid", "Test", "Rice University");
        long tempIDThi = UserDB.getInstance().getAllUser().get(2).getId();
        dis.joinRoom(tempRoomId, tempIDThi);
        dis.blockUser(tempID, tempIDThi, tempRoomId);

        testResponse = dis.leaveChatRoom(tempRoomId, tempID);
        assertEquals("Leave ChatRoom Test: Admin", "success", testResponse.getErrMsg());

        ChatRoomDB.getInstance().removeChatRoom(tempRoomId);
        UserDB.getInstance().removeUserById(tempID);
        UserDB.getInstance().removeUserById(tempIDSec);
        UserDB.getInstance().removeUserById(tempIDThi);
    }

    @Test
    public void testOpInvitation() {
        Response testResponse;
        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();

        testResponse = dis.opInvitation(111, true, tempID);
        assertEquals("Invitation Test", "can not find this invitation", testResponse.getErrMsg());


        UserDB.getInstance().getUser(tempID).getNotificationDB().addNotification(NotificationFac.getInstance().makeNotification("system",
                "Test", "Test", tempID, 1));
        long tempNotificationID = UserDB.getInstance().getUser(tempID).getNotificationDB().getAllNotifications().get(0).getNotificationId();
        testResponse = dis.opInvitation(tempNotificationID, true, tempID);
        assertEquals("Invitation Test", "invitation status error", testResponse.getErrMsg());

        UserDB.getInstance().getUser(tempID).getNotificationDB().addNotification(NotificationFac.getInstance().makeNotification("invite",
                "Test", "Test", tempID, 1));
        long tempNotificationIdSec = UserDB.getInstance().getUser(tempID).getNotificationDB().getAllNotifications().get(1).getNotificationId();
        testResponse = dis.opInvitation(tempNotificationIdSec, true, tempID);
        assertEquals("Invitation Test Accept", "chatroom not exist", testResponse.getErrMsg());

        UserDB.getInstance().getUser(tempID).getNotificationDB().addNotification(NotificationFac.getInstance().makeNotification("invite",
                "Test", "Test", tempID, 1));
        long tempNotificationIdThi = UserDB.getInstance().getUser(tempID).getNotificationDB().getAllNotifications().get(2).getNotificationId();
        testResponse = dis.opInvitation(tempNotificationIdThi, false, tempID);
        assertEquals("Invitation Test Decline", "success", testResponse.getErrMsg());
        UserDB.getInstance().removeUserById(tempID);
    }

    @Test
    public void testGetListofUserToInvite() {
//        getListofUserToInvite(long userId, long chatroomId)
        Response testResponse;

        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();
        testResponse = dis.getListofUserToInvite(tempID, 111);
        assertEquals("Get List of User to Invite Test: Room not exist", "chatroom not exist", testResponse.getErrMsg());

        dis.register("Usr2", "password", 20, "invalid", "Test", "Rice University");
        long tempIDSec = UserDB.getInstance().getAllUser().get(1).getId();

        dis.createRoom("testRoom", 10, true, tempID);
        long tempRoomId = ChatRoomDB.getInstance().getPrivateChatroom().get(0).getId();

        testResponse = dis.getListofUserToInvite(tempIDSec, tempRoomId);
        assertEquals("Get List of User to Invite Test: Not Admin", "user is not admin", testResponse.getErrMsg());

        testResponse = dis.getListofUserToInvite(tempID, tempRoomId);
        assertEquals("Get List of User to Invite Test: Admin", "success", testResponse.getErrMsg());

        ChatRoomDB.getInstance().removeChatRoom(tempRoomId);
        UserDB.getInstance().removeUserById(tempID);
        UserDB.getInstance().removeUserById(tempIDSec);
    }

    @Test
    public void testReceiveMsg() {
        String message = "{\"isPrivate\": true," +
                " \"content\": \"Test\", " +
                "\"senderId\": 5, " +
                "\"receiverId\": 5, " +
                "\"roomId\": 5}";
        ChatApp.getInstance().receivedMsg(message);

        dis.register("Usr1", "password", 20, "invalid", "Test", "Rice University");
        long tempID = UserDB.getInstance().getAllUser().get(0).getId();

        dis.createRoom("testRoom", 10, false, tempID);
        long tempRoomId = ChatRoomDB.getInstance().getPublicChatroom().get(0).getId();

        dis.register("Usr2", "password", 20, "invalid", "Test", "Rice University");
        long tempIDSec = UserDB.getInstance().getAllUser().get(1).getId();

        message = "{\"isPrivate\": false," +
                " \"content\": \"Test\", " +
                "\"senderId\": " + tempIDSec + ", " +
                "\"receiverId\": 5, " +
                "\"roomId\": " + tempRoomId + "}";
        ChatApp.getInstance().receivedMsg(message);

        message = "{\"isPrivate\": false," +
                " \"content\": \"hate speech\", " +
                "\"senderId\": " + tempID + ", " +
                "\"receiverId\": 5, " +
                "\"roomId\": " + tempRoomId + "}";
        ChatApp.getInstance().receivedMsg(message);

        message = "{\"isPrivate\": true," +
                " \"content\": \"hate speech\", " +
                "\"senderId\": " + tempID + ", " +
                "\"receiverId\": 5, " +
                "\"roomId\": " + tempRoomId + "}";
        ChatApp.getInstance().receivedMsg(message);

        dis.joinRoom(tempRoomId, tempIDSec);
        message = "{\"isPrivate\": true," +
                " \"content\": \"hate speech\", " +
                "\"senderId\": " + tempID + ", " +
                "\"receiverId\": "+ tempIDSec +", " +
                "\"roomId\": " + tempRoomId + "}";
        ChatApp.getInstance().receivedMsg(message);

        message = "{\"isPrivate\": false," +
                " \"content\": \"hate speech\", " +
                "\"senderId\": " + tempID + ", " +
                "\"receiverId\": "+ tempIDSec +", " +
                "\"roomId\": " + tempRoomId + "}";
        ChatApp.getInstance().receivedMsg(message);

        dis.getChatRoom(tempRoomId, tempIDSec);

        ChatRoomDB.getInstance().removeChatRoom(tempRoomId);
        UserDB.getInstance().removeUserById(tempID);
        UserDB.getInstance().removeUserById(tempIDSec);
    }


}