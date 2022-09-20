package com.orbit.DataFeeder.Service;

import com.orbit.DataFeeder.Repository.NoteBookDetails;
import com.orbit.DataFeeder.collection.NoteDetail;
import com.orbit.DataFeeder.collection.NoteSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Service
public class InotebookServiceImpl implements InotebookService {

    @Autowired
    private NoteBookDetails noteBookDetails;

    @Override
    public NoteSchema save(NoteDetail noteDetail,String username) {

        UUID uuid=UUID.randomUUID();
        String id = uuid.toString();
        id = id.replace("-","").toUpperCase();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
        NoteSchema noteSchema = new NoteSchema(id,username,noteDetail,timeStamp);

        return noteBookDetails.save(noteSchema);
    }

    @Override
    public  List<NoteSchema> findAll() {
        List<NoteSchema> lis = noteBookDetails.findAll();
        return lis;
    }
}
