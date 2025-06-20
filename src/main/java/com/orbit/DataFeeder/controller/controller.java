package com.orbit.DataFeeder.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbit.DataFeeder.Service.InotebookService;
import com.orbit.DataFeeder.Service.UserSchemaServiceSave;
import com.orbit.DataFeeder.collection.UserResponse;
import com.orbit.DataFeeder.collection.UserSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/notebook")
public class controller {

    final private String AUTHORIZATION = "Authorization";
    final private String BEARER = "Bearer ";

    @Autowired
    private InotebookService inotebookService;

    @Autowired
    private UserSchemaServiceSave userSchemaServiceSave;

    private final static Logger logger =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    final static String loginUrl = "http://localhost:8080/notebook/login";


    @ResponseStatus(CREATED)
    @PostMapping(path = "/createUser",consumes = APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody UserSchema userSchema, HttpServletResponse response) throws IOException {
        ResponseEntity<?> res= null;
        Map<String, String> map = new HashMap<String, String>();
        HttpHeaders httpHeaders = new HttpHeaders();

        try{
            userSchema.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis())));
            String obj = userSchemaServiceSave.save(userSchema);
            map.put("message", obj);

            if(obj.contains("created")){
                httpHeaders.add("statusCode", String.valueOf(CREATED));
                res = new ResponseEntity<Object>(map, httpHeaders,CREATED);
            } else{
                httpHeaders.add("statusCode", String.valueOf(FOUND));
                res = new ResponseEntity<Object>(map,httpHeaders,FOUND);
            }
        }catch (Exception e){
            logger.log(Level.INFO,"User Creation Failed");
            throw new RuntimeException(e);
        }

        return res;
    }

    @GetMapping(path = "/api/users",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  getUsers()  {
        ResponseEntity<?> res = null;
        try{
            Object obj = userSchemaServiceSave.getUsers();
            res = new ResponseEntity<>(obj,HttpStatus.FOUND);
        }catch (Exception e){
            logger.log(Level.INFO,"Unable to fetch the user details");
        }
        return res;
    }

    @GetMapping(path = "/api/getUser",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  getOneUser(HttpServletRequest request)  {
        UserResponse userResponse = utilityToFetchUserFromToken(request);
        return new  ResponseEntity<>(userResponse,HttpStatus.FOUND);
    }

    @GetMapping(path = "/refreshToken",produces = MediaType.APPLICATION_JSON_VALUE)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(AUTHORIZATION);

        if(authHeader!=null && authHeader.startsWith(BEARER)){
            try{
                String refreshToken = authHeader.substring(BEARER.length());
                Algorithm algorithm  = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String userName = decodedJWT.getSubject();

                 UserResponse userResponse = null;
                 try{
                     userResponse = userSchemaServiceSave.getSingleUser(userName);
                 }catch (Exception e){
                     throw new RuntimeException("Error fetching user");
                 }

                String accessToken = JWT.create()
                        .withSubject(userResponse.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() +10*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", userResponse.getRoles())
                        .sign(algorithm);


                response.setHeader("acessToken",accessToken);
                response.setHeader("refreshToken",refreshToken);

                Map<String,String> tokens = new HashMap<>();
                tokens.put("acessToken",accessToken);
                tokens.put("refereshToken",refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);

                //filterChain.doFilter(request,response);
            }catch (Exception e){
                logger.log(Level.INFO,"Error logging in: {} "+ "as null/wrong/expired/compromised Token");
                response.setHeader("Error",e.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String,String> map = new HashMap<>();
                map.put("Error_Message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),map);
            }
        }else{
            logger.log(Level.INFO,"Access Denied as no Token available");
            response.setHeader("Error","Access Denied as no Token available");
            response.setStatus(FORBIDDEN.value());

            Map<String,String> map = new HashMap<>();
            map.put("Error_Message","Access Denied as no Token available");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),map);
            throw new RuntimeException("Refresh Token is Missing");
        }
    }


    public UserResponse utilityToFetchUserFromToken(HttpServletRequest request){
        String authHeader = request.getHeader(AUTHORIZATION);
        UserResponse userResponse = null;
        if(authHeader!=null && authHeader.startsWith(BEARER)) {
            try {
                String refreshToken = authHeader.substring(BEARER.length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String userName = decodedJWT.getSubject();


                try {
                    userResponse = userSchemaServiceSave.getSingleUser(userName);
                } catch (Exception e) {
                    throw new RuntimeException("Error fetching user");
                }
            }catch (Exception e){
            }
        }

        return userResponse;
    }
}
