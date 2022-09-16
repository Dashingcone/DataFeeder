package com.orbit.DataFeeder.Service;

import com.orbit.DataFeeder.Repository.NoteBookDetails;
import com.orbit.DataFeeder.collection.Inotebook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InotebookServiceImpl implements InotebookService {

    @Autowired
    private NoteBookDetails noteBookDetails;

    @Override
    public String save(Inotebook inotebook) {
        return noteBookDetails.save(inotebook).getId();
    }
}
