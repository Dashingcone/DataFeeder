package com.orbit.DataFeeder.collection;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@Builder
@Document(collection = "UserSchema")
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude( JsonInclude.Include.NON_NULL )
public class UserSchema {

     @Id
     String username;

     @NotEmpty
     String name;

     @NotNull
     @NotEmpty
     String password;

     @NotEmpty
     String date;

     @NotEmpty
     @NotNull
     String roles;

}
