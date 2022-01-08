package edu.rice.comp504.model.user;

import java.util.ArrayList;

public interface IUserDB {
    public boolean loginCheck(String username, String password);

    public boolean usernameExist(String username);

    public void addUser(User user);

    public User getUser(long userId);

    public ArrayList<User> getAllUser();

}
