package com.orbit.DataFeeder.controller;


import com.orbit.DataFeeder.Service.InotebookService;
import com.orbit.DataFeeder.Service.UserSchemaServiceSave;
import com.orbit.DataFeeder.collection.Inotebook;
import com.orbit.DataFeeder.collection.UserSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/notebook")
public class controller {

    @Autowired
    private InotebookService inotebookService;

    @Autowired
    private UserSchemaServiceSave userSchemaServiceSave;

    @PostMapping(path = "/")
    public String save(@RequestBody Inotebook inotebook)  {
        return inotebookService.save(inotebook);
    }

    @PostMapping(path = "/api/auth/createUser",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>authenticateUser(@RequestBody UserSchema userSchema)  {
        Object obj = userSchemaServiceSave.save(userSchema);
        return new ResponseEntity(obj, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/notes",produces = "application/json")
    public void Notes()  {
        return ;
    }

}
