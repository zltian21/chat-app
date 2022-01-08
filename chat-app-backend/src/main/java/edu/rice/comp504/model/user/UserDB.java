package edu.rice.comp504.model.user;

import edu.rice.comp504.model.message.AMessage;

import java.util.ArrayList;
import java.util.List;

public class UserDB implements IUserDB{
    private static UserDB userDBIns = null;
    private ArrayList<User> allUsers;

    private UserDB() {
        this.allUsers = new ArrayList<User>();
    }

    /**
     * Get UserDB instance
     * @return userDB instance.
     */
    public static UserDB getInstance() {
        if (userDBIns == null) {
            userDBIns = new UserDB();
        }
        return userDBIns;
    }

    /**
     * check if the username and password are correct.
     * @param username username.
     * @param password password.
     * @return A User.
     */
    public boolean loginCheck(String username,String password) {
        // TODO login check logic
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUserName().equals(username) && allUsers.get(i).getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void addUser(User user) {
        this.allUsers.add(user);
    }

    /**
     * check if username exist with given username
     * @param username username
     * @return if username exist.
     */
    public boolean usernameExist(String username) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get user instance with userId
     * @param userId userId
     * @return A User.
     */
    public User getUser(long userId) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getId() == userId) {
                return allUsers.get(i);
            }
        }
        return null;
    }

    /**
     * Get user id giving username
     * @param username username
     * @return userId.
     */
    public long getUserIdByName(String username) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUserName().equals(username)) {
                return allUsers.get(i).getId();
            }
        }
        return -1;
    }

    public ArrayList<User> getAllUser() {
        return this.allUsers;
    }


    /**
     * Remove user by user Id.
     * Only use for unit test.
     * @param userId user id.
     */
    public void removeUserById(long userId) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getId() == userId) {
                allUsers.remove(i);
                break;
            }
        }
        return;
    }
}
