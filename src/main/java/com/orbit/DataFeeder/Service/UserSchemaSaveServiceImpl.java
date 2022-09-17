package com.orbit.DataFeeder.Service;

import com.orbit.DataFeeder.Repository.UserSchemaRepository;
import com.orbit.DataFeeder.collection.UserSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.SimpleRouteMatcher;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UserSchemaSaveServiceImpl implements UserSchemaServiceSave, UserDetailsService {

    @Autowired
    UserSchemaRepository userSchemaRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final static Logger logger =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserSchema> userSchema = userSchemaRepository.findById(username);
        UserSchema user = null;
        if(userSchema.isEmpty()){
            logger.log(Level.INFO,"User not found");
            throw new UsernameNotFoundException("User not found");
        }else{
            logger.log(Level.INFO,"User found: "+username);
            user = userSchema.get();
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("Default"));
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
    }

    @Override
    public <T> T save(UserSchema userSchema) throws NoSuchAlgorithmException, InvalidKeySpecException {

        Optional<UserSchema> userSchema1 = userSchemaRepository.findById(userSchema.getUsername());
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        KeySpec spec = null;
        SecretKeyFactory secretKeyFactory = null;
        if(userSchema1.isEmpty()){
           // spec = new PBEKeySpec(userSchema.getPassword().toCharArray(),salt,65536,128);
           // secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
           // byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
          //  String password = new String(hash);
           // logger.log(Level.INFO,"Hashed password is : "+password);
            userSchema.setPassword(passwordEncoder.encode(userSchema.getPassword()));
            userSchemaRepository.save(userSchema);
        }

        return userSchema1.isEmpty()? (T) userSchema : (T) "User Already Present";
    }

    @Override
    public <T> T getUsers() {
            return (T) userSchemaRepository.findAll();
    }

    @Override
    public UserSchema getSingleUser(String userName) {
        Optional<UserSchema> sch =  userSchemaRepository.findById(userName);
        if(sch.isEmpty())
            throw new RuntimeException("No such user found with username: "+userName);

        return sch.get();
    }


}
