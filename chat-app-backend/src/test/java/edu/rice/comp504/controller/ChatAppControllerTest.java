package edu.rice.comp504.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import edu.rice.comp504.adapter.DispatchAdapter;
import edu.rice.comp504.model.Response;
import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.chatroom.ChatRoomDB;
import edu.rice.comp504.model.chatroom.ChatRoomFac;
import edu.rice.comp504.model.message.AMessage;
import edu.rice.comp504.model.message.IMessageFac;
import edu.rice.comp504.model.message.MessageDB;
import edu.rice.comp504.model.message.MessageFac;
import edu.rice.comp504.model.notification.NotificationFac;
import edu.rice.comp504.model.user.User;
import edu.rice.comp504.model.user.UserDB;
import edu.rice.comp504.model.user.UserFac;
import junit.framework.TestCase;
import netscape.javascript.JSObject;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

public class ChatAppControllerTest extends TestCase {
    DispatchAdapter dis= new DispatchAdapter();
    private static UserFac userFac;
    private static ChatRoomDB chatRoomDB;
    private static UserDB userDB;
    private static ChatRoomFac chatRoomFac;
    private static IMessageFac messageFac;
    private static MessageDB messageDB;

    /**
     * Test for JoinRoom.
     */
    @Test
    public void testJoinRoom() {
        userFac = UserFac.getInstance();
        userDB = UserDB.getInstance();
        chatRoomFac = ChatRoomFac.getInstance();
        chatRoomDB = ChatRoomDB.getInstance();


        User newUser = userFac.makeUser("Rice", "Sports", "tm67", "abcd", 21, "1234");
        userDB.addUser(newUser);
        User admin = userDB.getUser(newUser.getId());
        ChatRoom newRoom = chatRoomFac.makeChatRoom("Sports Club", false, admin, 10);
        chatRoomDB.addChatRoom(newRoom);
        Date date = new Date();
        admin.joinRoom(newRoom, new Timestamp(date.getTime()));
        User newUser1 = userFac.makeUser("Rice", "Sports", "tm677", "abcd", 21, "1234");
        userDB.addUser(newUser1);
        Response res=(dis.joinRoom(newRoom.getId(), newUser1.getId()));
        assertEquals("User Added to that Chat Room",true,newRoom.userIsJoined(newUser1.getId()));
    }

    /**
     * Test for Remove User from ChatRoom.
     */
    @Test
    public void testRemoveUser() {
        userFac = UserFac.getInstance();
        userDB = UserDB.getInstance();
        chatRoomFac = ChatRoomFac.getInstance();
        chatRoomDB = ChatRoomDB.getInstance();

        User newUser = userFac.makeUser("Rice", "Sports", "tm67", "abcd", 21, "1234");
        userDB.addUser(newUser);
        User admin = userDB.getUser(newUser.getId());
        ChatRoom newRoom = chatRoomFac.makeChatRoom("Sports Club", false, admin, 10);
        chatRoomDB.addChatRoom(newRoom);
        Date date = new Date();
        admin.joinRoom(newRoom, new Timestamp(date.getTime()));
        User newUser1 = userFac.makeUser("Rice", "Sports", "tm677", "abcd", 21, "1234");
        userDB.addUser(newUser1);
        Response resJoin=(dis.joinRoom(newRoom.getId(), newUser1.getId()));
        assertEquals("User Joined Chat Room",true,newRoom.userIsJoined(newUser1.getId()));
        Response resRemove=(dis.removeUser(newUser.getId(),newUser1.getId(),newRoom.getId()));
        assertEquals("User Has been removed from the Chat Room",false,newRoom.userIsJoined(newUser1.getId()));
    }

    /**
     * Test for Block User from Chat Room.
     */
    @Test
    public void testBlockUser() {
        userFac = UserFac.getInstance();
        userDB = UserDB.getInstance();
        chatRoomFac = ChatRoomFac.getInstance();
        chatRoomDB = ChatRoomDB.getInstance();
        User newUser = userFac.makeUser("Rice", "Sports", "tm67", "abcd", 21, "1234");
        userDB.addUser(newUser);
        User admin = userDB.getUser(newUser.getId());
        ChatRoom newRoom = chatRoomFac.makeChatRoom("Sports Club", false, admin, 10);
        chatRoomDB.addChatRoom(newRoom);
        Date date = new Date();
        admin.joinRoom(newRoom, new Timestamp(date.getTime()));
        User newUser1 = userFac.makeUser("Rice", "Sports", "tm677", "abcd", 21, "1234");
        userDB.addUser(newUser1);
        Response resJoin=(dis.joinRoom(newRoom.getId(), newUser1.getId()));
        assertEquals("User Joined Chat Room",true,newRoom.userIsJoined(newUser1.getId()));
        Response resRemove=(dis.blockUser(newUser.getId(),newUser1.getId(),newRoom.getId()));
        assertEquals("User Has been Blocked from the Chat Room",1,newRoom.getBlockedUsers().size());
    }

    /**
     * Test for UnBlock User from Chat Room.
     */
    @Test
    public void testUnBlockUser() {
        userFac = UserFac.getInstance();
        userDB = UserDB.getInstance();
        chatRoomFac = ChatRoomFac.getInstance();
        chatRoomDB = ChatRoomDB.getInstance();
        User newUser = userFac.makeUser("Rice", "Sports", "tm67", "abcd", 21, "1234");
        userDB.addUser(newUser);
        User admin = userDB.getUser(newUser.getId());
        ChatRoom newRoom = chatRoomFac.makeChatRoom("Sports Club", false, admin, 10);
        chatRoomDB.addChatRoom(newRoom);
        Date date = new Date();
        admin.joinRoom(newRoom, new Timestamp(date.getTime()));
        User newUser1 = userFac.makeUser("Rice", "Sports", "tm677", "abcd", 21, "1234");
        userDB.addUser(newUser1);
        Response resJoin=(dis.joinRoom(newRoom.getId(), newUser1.getId()));
        assertEquals("User Joined Chat Room",true,newRoom.userIsJoined(newUser1.getId()));
        Response resblock=(dis.blockUser(newUser.getId(),newUser1.getId(),newRoom.getId()));
        assertEquals("User Has been Blocked from the Chat Room",1,newRoom.getBlockedUsers().size());
        Response resUnblock=(dis.unblockUser(newUser.getId(),newUser1.getId(),newRoom.getId()));
        assertEquals("User Has been Unblocked from the Chat Room",0,newRoom.getBlockedUsers().size());
    }

    /**
     * Test for Leave all Chat Rooms.
     */
    @Test
    public void testLeaveAllChatRooms() {
        userFac = UserFac.getInstance();
        userDB = UserDB.getInstance();
        chatRoomFac = ChatRoomFac.getInstance();
        chatRoomDB = ChatRoomDB.getInstance();
        User newUser = userFac.makeUser("Rice", "Sports", "tm67", "abcd", 21, "1234");
        userDB.addUser(newUser);
        User admin = userDB.getUser(newUser.getId());
        ChatRoom newRoom = chatRoomFac.makeChatRoom("Sports Club", false, admin, 10);
        chatRoomDB.addChatRoom(newRoom);
        Date date = new Date();
        admin.joinRoom(newRoom, new Timestamp(date.getTime()));
        User newUser1 = userFac.makeUser("Rice", "Sports", "tm677", "abcd", 21, "1234");
        userDB.addUser(newUser1);
        Response resJoin=(dis.joinRoom(newRoom.getId(), newUser1.getId()));
        assertEquals("User Joined Chat Room",1,newUser1.getChatRooms().size());
        Response resLeaveAll=(dis.leaveAllChatroom(newUser1.getId()));
        assertEquals("User left all Chat Rooms",0,newUser1.getChatRooms().size());
    }
    /**
     * Test for Send Invite.
     */
    @Test
    public void testSendInvite() {

        //Getting required Instances.
        userFac = UserFac.getInstance();
        userDB = UserDB.getInstance();
        chatRoomFac = ChatRoomFac.getInstance();
        chatRoomDB = ChatRoomDB.getInstance();

        //Creating a new User.
        User newUser = userFac.makeUser("Rice", "Sports", "tm67", "abcd", 21, "1234");
        userDB.addUser(newUser);
        //Making the User Admin.
        User admin = userDB.getUser(newUser.getId());
        //Creating a Public Chat Room.
        ChatRoom newRoom = chatRoomFac.makeChatRoom("Sports Club", false, admin, 10);
        chatRoomDB.addChatRoom(newRoom);
        Date date = new Date();
        admin.joinRoom(newRoom, new Timestamp(date.getTime()));

        //Creating a new User.
        User newUser1 = userFac.makeUser("Rice", "Sports", "tm677", "abcd", 21, "1234");
        userDB.addUser(newUser1);
        //New User currently has no notifications.
        assertEquals("User Should get Notification",0,newUser1.getNotificationDB().getAllNotifications().size());
        //Getting Response for sending Invite in Public Chat room.
        Response resPublicInvite=(dis.sendInvite(newUser1.getId(), newUser1.getId(),newRoom.getId()));
        assertEquals("Cannot Send Invites for Public Chat Rooms","only private chatroom can send invitation",resPublicInvite.getErrMsg());

        //Creating a Private ChatRoom with same admin.
        ChatRoom privateChatRoom = chatRoomFac.makeChatRoom("Sports Club", true, admin, 10);
        chatRoomDB.addChatRoom(privateChatRoom);
        //Getting Response for sending Invite in Private Chat room.
        Response resPrivateInvite=(dis.sendInvite(newUser.getId(), newUser1.getId(),privateChatRoom.getId()));
        assertEquals("Send Invites for Private Chat Room","success",resPrivateInvite.getErrMsg());
        assertEquals("User Should get Notification",1,newUser1.getNotificationDB().getAllNotifications().size());
    }

    /**
     * Tests for Edit Message.
     */
    @Test
    public void testEditMessage() {

        //Getting required Instances.
        userFac = UserFac.getInstance();
        userDB = UserDB.getInstance();
        chatRoomFac = ChatRoomFac.getInstance();
        chatRoomDB = ChatRoomDB.getInstance();
        messageFac = MessageFac.getInstance();

        //Creating a new User.
        User newUser = userFac.makeUser("Rice", "Sports", "tm67", "abcd", 21, "1234");
        userDB.addUser(newUser);
        //Making the User Admin.
        User admin = userDB.getUser(newUser.getId());
        //Creating a Public Chat Room.
        ChatRoom newRoom = chatRoomFac.makeChatRoom("Sports Club", false, admin, 10);
        chatRoomDB.addChatRoom(newRoom);
        Date date = new Date();
        admin.joinRoom(newRoom, new Timestamp(date.getTime()));

        //Creating a new User.
        User newUser1 = userFac.makeUser("Rice", "Sports", "tm677", "abcd", 21, "1234");
        userDB.addUser(newUser1);
        //New User currently has no notifications.
        assertEquals("User Should get Notification",0,newUser1.getNotificationDB().getAllNotifications().size());
        //Getting response for editing a message which is not in the chat room.
        AMessage message  = messageFac.makeMessage("public","Hi There!","", newUser1.getId(),1,new Timestamp(date.getTime()), false);
        Response resEdit = dis.editMessage(newUser1.getId(), message.getId(), newRoom.getId(), "New Edited Message");
        assertEquals("Cannot Edit Message","can not find this message",resEdit.getErrMsg());
        //Adding message to the chat room.
        newRoom.addNewMessage(message);
        //Getting response for editing a message which is in the chat room.
        Response resEditAdded = dis.editMessage(newUser1.getId(), message.getId(), newRoom.getId(), "New Edited Message");
        assertEquals("Message edited successfully","success",resEditAdded.getErrMsg());

    }
    /**
     * Tests for Remove Message.
     */
    @Test
    public void testRemoveMessage() {

        //Getting required Instances.
        userFac = UserFac.getInstance();
        userDB = UserDB.getInstance();
        chatRoomFac = ChatRoomFac.getInstance();
        chatRoomDB = ChatRoomDB.getInstance();
        messageFac = MessageFac.getInstance();

        //Creating a new User.
        User newUser = userFac.makeUser("Rice", "Sports", "tm67", "abcd", 21, "1234");
        userDB.addUser(newUser);
        //Making the User Admin.
        User admin = userDB.getUser(newUser.getId());
        //Creating a Public Chat Room.
        ChatRoom newRoom = chatRoomFac.makeChatRoom("Sports Club", false, admin, 10);
        chatRoomDB.addChatRoom(newRoom);
        Date date = new Date();
        admin.joinRoom(newRoom, new Timestamp(date.getTime()));

        //Creating a new User.
        User newUser1 = userFac.makeUser("Rice", "Sports", "tm677", "abcd", 21, "1234");
        userDB.addUser(newUser1);
        User newUser2 = userFac.makeUser("Rice", "Sports", "tm6777", "abcd", 21, "1234");
        userDB.addUser(newUser2);
        //New User currently has no notifications.
        assertEquals("User Should get Notification",0,newUser1.getNotificationDB().getAllNotifications().size());
        //Getting response for editing a message which is not in the chat room.
        AMessage message  = messageFac.makeMessage("public","Hi There!","", newUser1.getId(),1,new Timestamp(date.getTime()), false);
        Response resRemove = dis.removeMessage(newUser1.getId(), message.getId(), newRoom.getId());
        assertEquals("Cannot delete Message","can not find this message",resRemove.getErrMsg());
        //Adding message to the chat room.
        newRoom.addNewMessage(message);
        //Getting response for remove tried by another user.
        Response resRemoveByAnotherUser = dis.removeMessage(newUser2.getId(), message.getId(), newRoom.getId());
        assertEquals("User cannot remove message","user can not remove this message",resRemoveByAnotherUser.getErrMsg());

        //Getting response for remove by message owner
        Response resRemoveByUser = dis.removeMessage(newUser1.getId(), message.getId(), newRoom.getId());
        assertEquals("User message removed","success",resRemoveByUser.getErrMsg());

        //Getting response for remove by admin
        Response resRemoveByAdmin = dis.removeMessage(admin.getId(), message.getId(), newRoom.getId());
        assertEquals("User message removed","success",resRemoveByAdmin.getErrMsg());
    }

    /**
     * Tests to get User Info.
     */
    @Test
    public void testGetUSerInfo() {

        //Getting required Instances.
        userFac = UserFac.getInstance();
        userDB = UserDB.getInstance();
        chatRoomFac = ChatRoomFac.getInstance();
        chatRoomDB = ChatRoomDB.getInstance();
        messageFac = MessageFac.getInstance();

        //Creating a new User.
        User newUser = userFac.makeUser("Rice", "Sports", "tm67", "abcd", 21, "1234");
        userDB.addUser(newUser);
        //Making the User Admin.
        User admin = userDB.getUser(newUser.getId());
        //Creating a Public Chat Room.
        ChatRoom newRoom = chatRoomFac.makeChatRoom("Sports Club", false, admin, 10);
        chatRoomDB.addChatRoom(newRoom);
        Date date = new Date();
        admin.joinRoom(newRoom, new Timestamp(date.getTime()));

        //Get UserInfo
        Response resUser = dis.getUserInfo(newUser.getId());
        JsonArray resData = resUser.getData();
        JsonElement user = resData.get(0);
        assertEquals("User Name Check","tm67",user.getAsJsonObject().get("username").getAsString());
        assertEquals("Age Check","21",user.getAsJsonObject().get("age").getAsString());
        assertEquals("Interests Check","Sports",user.getAsJsonObject().get("interests").getAsString());
        assertEquals("imageUrl Check","abcd",user.getAsJsonObject().get("imageUrl").getAsString());
        assertEquals("HateSpeech Count Check","0",user.getAsJsonObject().get("hateSpeechCount").getAsString());
        assertEquals("School Name check","Rice",user.getAsJsonObject().get("schoolName").getAsString());
        AMessage message  = messageFac.makeMessage("public","hate speech","", newUser.getId(),0,new Timestamp(date.getTime()), false);
        newRoom.addNewMessage(message);
        assertEquals("HateSpeech Count Check","0",user.getAsJsonObject().get("hateSpeechCount").getAsString());
    }
}