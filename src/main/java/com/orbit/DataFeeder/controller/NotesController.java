package com.orbit.DataFeeder.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.orbit.DataFeeder.Service.InotebookService;
import com.orbit.DataFeeder.Service.UserSchemaServiceSave;
import com.orbit.DataFeeder.collection.NoteDetail;
import com.orbit.DataFeeder.collection.NoteSchema;
import com.orbit.DataFeeder.collection.UserResponse;
import com.orbit.DataFeeder.collection.UserSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://domain2.com", maxAge = 3600)
@RestController
@RequestMapping(path = "/notebook")
public class NotesController {
    final private String AUTHORIZATION = "Authorization";
    final private String BEARER = "Bearer ";

    private final static Logger logger =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Autowired
    private InotebookService inotebookService;

    @Autowired
    private UserSchemaServiceSave userSchemaServiceSave;

    @PostMapping(path = "/api/addNotes",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNotes(@RequestBody NoteDetail noteDetail, HttpServletRequest request) throws NoSuchFieldException {
        ResponseEntity<?> res = null;
        try{
            String username = utilityToFetchUserFromToken(request);
            res = new  ResponseEntity<>(inotebookService.save(noteDetail,username),HttpStatus.OK);
        }catch (Exception e){
            logger.log(Level.SEVERE," Internal Server Error");
            new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return res;
    }

    @GetMapping(path = "/api/fetchAllNotes",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  getUsers(HttpServletRequest request)  {
        ResponseEntity<?> res = null;
        String username = utilityToFetchUserFromToken(request);
        try{
            res = new  ResponseEntity<>(inotebookService.findAll(username),HttpStatus.FOUND);
        }catch (Exception e){
            System.out.println(" Error hai bhai ");
            logger.log(Level.SEVERE," Internal Server Error");
            new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return res;
    }



    @PutMapping(path = "/api/updateNotes",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  updateNote(@RequestParam String id , @RequestBody NoteDetail noteDetail,
                                         HttpServletRequest request)  {
        ResponseEntity<?> res = null;
        String username = utilityToFetchUserFromToken(request);
        try{
            res = inotebookService.update(username,id,noteDetail);
        }catch (Exception e){
            logger.log(Level.SEVERE," Internal Server Error");
            new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return res;
    }


    @DeleteMapping(path = "/api/deleteNotes",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  deleteNotes(@RequestParam String id ,
                                         HttpServletRequest request)  {
        ResponseEntity<?> res = null;
        String username = utilityToFetchUserFromToken(request);
        try{
            res = inotebookService.delete(id,username);
        }catch (Exception e){
            logger.log(Level.SEVERE," Internal Server Error");
            new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return res;
    }

    public String utilityToFetchUserFromToken(HttpServletRequest request){
        String authHeader = request.getHeader(AUTHORIZATION);
        UserResponse userResponse = null;
        String userName = null;
        if(authHeader!=null && authHeader.startsWith(BEARER)) {
            try {
                String refreshToken = authHeader.substring(BEARER.length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                userName = decodedJWT.getSubject();


                try {
                    userResponse = userSchemaServiceSave.getSingleUser(userName);
                } catch (Exception e) {
                    throw new RuntimeException("Error fetching user");
                }
            }catch (Exception e){
            }
        }

        return userName;
    }

}
