package edu.rice.comp504.model.message;

import java.util.ArrayList;

public interface IMessageDB {

    public void addMessage(AMessage aMessage);

    public ArrayList<AMessage> getAllMessages();

    public AMessage getMessage(long messageId);

    public void removeMessage(long messageId);
}
