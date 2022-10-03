package com.orbit.DataFeeder.collection;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenCapture {
    String accessToken;
    String username;
    String refreshToken;
    String statusCode;
}
