package edu.rice.comp504.model.user;

import java.util.ArrayList;

public class UserFac implements IUserFac{

    private static UserFac userFacIns = null;
    private UserDB userDB;
    private int nextUserId = 1;

    private UserFac() {
        this.userDB = UserDB.getInstance();
    }

    /**
     * get user factory instance
     * @return User factory Instance.
     */
    public static UserFac getInstance() {
        if (userFacIns == null) {
            userFacIns = new UserFac();
        }
        return userFacIns;
    }

    /**
     * Makes a user, Adds it to the allUsers list.
     * @return A User.
     */
    // ?????  name v.s username
    // ?????  type of interests   String ? ArrayList<String> ?
    @Override
    public User makeUser(String schoolName, String interests, String userName, String imageUrl, int age, String password) {
        User newUser = new User(nextUserId, schoolName, interests, userName, imageUrl, age, password);
        nextUserId += 1;
        return newUser;
    }


}
