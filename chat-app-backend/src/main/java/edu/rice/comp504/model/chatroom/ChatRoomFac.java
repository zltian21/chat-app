package edu.rice.comp504.model.chatroom;

import edu.rice.comp504.model.user.User;

public class ChatRoomFac implements IChatRoomFac{
    private static ChatRoomFac chatRoomFacIns = null;
    private static long nextChatroomId = 1;

    /**
     * Get chatroom factory instance.
     * @return chatroom factory instance.
     */
    public static ChatRoomFac getInstance() {
        if (chatRoomFacIns == null) {
            chatRoomFacIns = new ChatRoomFac();
        }
        return chatRoomFacIns;
    }

    /**
     * Makes a chat Room , Adds it to the allChatRooms list.
     * @return A ChatRoom.
     */
    public ChatRoom makeChatRoom(String roomName, boolean isPrivate, User admin, int size) {
        ChatRoom newChatroom;
        if (!isPrivate) {
            newChatroom = new PublicChatRoom(nextChatroomId, roomName, false, admin, size);
        } else {
            newChatroom = new PrivateChatRoom(nextChatroomId, roomName, true, admin, size);
        }
        nextChatroomId += 1;
        return newChatroom;
    }
}
