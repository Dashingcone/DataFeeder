package com.orbit.DataFeeder.Repository;

import com.orbit.DataFeeder.collection.Inotebook;
import com.orbit.DataFeeder.collection.UserSchema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSchemaRepository extends MongoRepository<UserSchema,String> {
}
