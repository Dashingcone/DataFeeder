package com.orbit.DataFeeder.Service;

import com.orbit.DataFeeder.Repository.NoteBookDetails;
import com.orbit.DataFeeder.collection.NoteDetail;
import com.orbit.DataFeeder.collection.NoteSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InotebookServiceImpl implements InotebookService {

    @Autowired
    private NoteBookDetails noteBookDetails;

    @Override
    public NoteSchema save(NoteDetail noteDetail,String username) {
        NoteSchema noteSchema = new NoteSchema(username,noteDetail.getTitle(),
                noteDetail.getDescription(),noteDetail.getTag(),noteDetail.getDate());

        return noteBookDetails.save(noteSchema);
    }
}
