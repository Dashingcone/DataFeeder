package com.orbit.DataFeeder.Service;


import com.orbit.DataFeeder.collection.UserSchema;

public interface UserSchemaServiceSave {
    <T> T save(UserSchema userSchema);
}
