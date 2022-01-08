package edu.rice.comp504.model.message;

import java.sql.Timestamp;

public class MessageFac implements IMessageFac{
    private static MessageFac messageFacIns = null;
    private static long nextMessageId = 1;

    /**
     * Get message factory instance.
     * @return message factory instance.
     */
    public static MessageFac getInstance() {
        if (messageFacIns == null) {
            messageFacIns = new MessageFac();
        }
        return messageFacIns;
    }

    /**
     * Create a message instance.
     */
    public AMessage makeMessage(String type, String text, String content, long senderId, long receiverId, Timestamp sentAt, boolean isSeen) {
        AMessage aMessage;
        if (type.equals("public")) {
            aMessage = new PublicMessage("public", nextMessageId, text, content, senderId, sentAt, true);
            nextMessageId += 1;
            return aMessage;
        } else if (type.equals("private")) {
            aMessage = new PrivateMessage("private", nextMessageId, text, content, senderId, sentAt, true, receiverId);
            nextMessageId += 1;
            return aMessage;
        } else if (type.equals("system")) {
            aMessage = new SystemMessage("system", nextMessageId, text, content, -1, sentAt, true);
            nextMessageId += 1;
            return aMessage;
        } else {
            return null;
        }
    }
}
