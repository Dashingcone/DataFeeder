package com.orbit.DataFeeder.Service;

import com.orbit.DataFeeder.Repository.UserSchemaRepository;
import com.orbit.DataFeeder.collection.UserSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UserSchemaSaveServiceImpl implements UserSchemaServiceSave{

    @Autowired
    UserSchemaRepository userSchemaRepository;

    @Override
    public <T> T save(UserSchema userSchema) {

        Optional<UserSchema> userSchema1 = userSchemaRepository.findById(userSchema.getEmail());
        if(userSchema1.isEmpty())
            userSchemaRepository.save(userSchema);

        return userSchema1.isEmpty()? (T) userSchema : (T) "User Already Present";
    }
}
