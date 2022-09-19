package com.orbit.DataFeeder.collection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Document(collection = "NoteSchema")
public class NoteDetail {

    @NotBlank
    @NotEmpty
    @NotNull
    String title;

    @NotBlank
    @NotEmpty
    @NotNull
    String description;

    String tag;
}
