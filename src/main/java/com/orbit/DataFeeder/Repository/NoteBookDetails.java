package com.orbit.DataFeeder.Repository;

import com.orbit.DataFeeder.collection.Inotebook;
import com.orbit.DataFeeder.collection.NoteSchema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteBookDetails extends MongoRepository<NoteSchema,String> {

}
