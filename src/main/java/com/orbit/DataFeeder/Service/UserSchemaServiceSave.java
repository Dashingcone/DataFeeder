package com.orbit.DataFeeder.Service;


import com.orbit.DataFeeder.collection.UserResponse;
import com.orbit.DataFeeder.collection.UserSchema;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface UserSchemaServiceSave {
    <T> T save(UserSchema userSchema) throws NoSuchAlgorithmException, InvalidKeySpecException;
    <T> T getUsers();
    UserResponse getSingleUser(String userName);
}
