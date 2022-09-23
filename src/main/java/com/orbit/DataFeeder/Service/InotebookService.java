package com.orbit.DataFeeder.Service;

import com.orbit.DataFeeder.collection.NoteDetail;
import com.orbit.DataFeeder.collection.NoteSchema;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InotebookService {
    NoteSchema save(NoteDetail noteDetail,String username);
    List<NoteSchema> findAll(String username);
    <T> T update(String username,String noteId,NoteDetail noteDetail);
    ResponseEntity delete(String id, String username);
}
