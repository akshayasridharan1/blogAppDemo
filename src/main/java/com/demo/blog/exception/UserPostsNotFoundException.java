package com.demo.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserPostsNotFoundException extends RuntimeException {

    public UserPostsNotFoundException(String exception) {
        super(exception);
    }
}