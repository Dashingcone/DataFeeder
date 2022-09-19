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
        String username = utilityToFetchUserFromToken(request);
        inotebookService.save(noteDetail,username);
        return new  ResponseEntity<>("userResponse",HttpStatus.FOUND);
    }

    @GetMapping(path = "/api/fetchAllNotes",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  getUsers()  {
        ResponseEntity<?> res = null;

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
