package com.orbit.DataFeeder.collection;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Builder
@Document(collection = "NoteSchema")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteSchema {

    @NotNull
    String title;
    @NotNull
    String description;

    String tag;
    String date;
}
