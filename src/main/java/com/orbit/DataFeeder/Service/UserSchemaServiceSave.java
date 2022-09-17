package com.orbit.DataFeeder.Service;


import com.orbit.DataFeeder.collection.UserSchema;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface UserSchemaServiceSave {
    <T> T save(UserSchema userSchema) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
