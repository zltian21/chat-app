package edu.rice.comp504.model.message;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageDB implements IMessageDB{

    ArrayList<AMessage> allMessages = new ArrayList<AMessage>();

    public void addMessage(AMessage aMessage) {
        this.allMessages.add(aMessage);
    }

    /**
     * Get message instance with given messageId.
     * @param messageId messageId.
     * @return a message instance.
     */
    public AMessage getMessage(long messageId) {
        for (int i = 0; i < allMessages.size(); i++) {
            if (allMessages.get(i).getId() == messageId) {
                return allMessages.get(i);
            }
        }
        return null;
    }

    public ArrayList<AMessage> getAllMessages() {
        return this.allMessages;
    }

    /**
     * Remove message with given messageId.
     * @param messageId messageId.
     */
    public void removeMessage(long messageId) {
        for (int i = 0; i < allMessages.size(); i++) {
            if (allMessages.get(i).getId() == messageId) {
                allMessages.remove(i);
                return;
            }
        }
    }
}
