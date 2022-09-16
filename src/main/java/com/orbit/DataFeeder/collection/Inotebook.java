package com.orbit.DataFeeder.collection;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "noteBookDetails")
public class Inotebook {

    @Id
    private String id;
    private String name;
    private String city;
}
