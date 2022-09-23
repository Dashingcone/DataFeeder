package com.orbit.DataFeeder.Service;

import com.orbit.DataFeeder.Repository.NoteBookDetails;
import com.orbit.DataFeeder.collection.NoteDetail;
import com.orbit.DataFeeder.collection.NoteSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InotebookServiceImpl implements InotebookService {

    @Autowired
    private NoteBookDetails noteBookDetails;

    @Override
    public NoteSchema save(NoteDetail noteDetail,String username) {

        UUID uuid=UUID.randomUUID();
        String id = uuid.toString();
        id = id.replace("-","").toUpperCase();
        NoteSchema noteSchema = new NoteSchema(id,username,noteDetail,getCurrentTime());

        return noteBookDetails.save(noteSchema);
    }

    @Override
    public  List<NoteSchema> findAll(String username) {
        List<NoteSchema> lis = noteBookDetails.findAll();

        return lis.stream().filter(f->f.getUsername().equals(username))
                .map(m-> new NoteSchema(m.getNoteId(),m.getUsername(),m.getNoteDetail(),m.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public <T> T update(String username,String noteId,NoteDetail noteDetail) {
            Optional<NoteSchema> noteSchema = noteBookDetails.findById(noteId);
            NoteSchema nt = null;
            if(noteSchema.isPresent()){
                if(username.equalsIgnoreCase(noteSchema.get().getUsername())){
                     nt = new NoteSchema(noteId,username,noteDetail,getCurrentTime());
                     noteBookDetails.save(nt);
                }else{
                    return (T) new ResponseEntity("Not Found", HttpStatus.FORBIDDEN);
                }
            }
            return (T) new ResponseEntity(nt,HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity delete(String id, String username) {
        Optional<NoteSchema> noteSchema = noteBookDetails.findById(id);
        if(noteSchema.isPresent()){
            if(username.equalsIgnoreCase(noteSchema.get().getUsername())){
                noteBookDetails.deleteById(id);
            }else{
                return  new ResponseEntity("Not Found", HttpStatus.FORBIDDEN);
            }
        }
        return  new ResponseEntity("Note with ID: "+id+" deleted!",HttpStatus.ACCEPTED);
    }

    String getCurrentTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
    }
}
