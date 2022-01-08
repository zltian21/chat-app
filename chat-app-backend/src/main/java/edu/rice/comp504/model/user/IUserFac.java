package edu.rice.comp504.model.user;

import edu.rice.comp504.model.message.AMessage;

import java.sql.Timestamp;
import java.util.ArrayList;

public interface IUserFac {

    public User makeUser(String schoolName, String interests, String userName, String imageUrl, int age, String password);

}
