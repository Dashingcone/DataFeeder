package com.orbit.DataFeeder.Service;

import com.orbit.DataFeeder.collection.NoteDetail;
import com.orbit.DataFeeder.collection.NoteSchema;

import java.util.List;

public interface InotebookService {
    NoteSchema save(NoteDetail noteDetail,String username);
    List<NoteSchema> findAll();
}
