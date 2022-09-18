package com.orbit.DataFeeder.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Document(collection = "UserSchema")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonInclude( JsonInclude.Include.NON_NULL )
public class UserResponse {
    String username;
    String name;
    String date;
    String roles;
}
