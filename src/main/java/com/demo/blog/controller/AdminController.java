package com.demo.blog.controller;

import com.demo.blog.model.Post;
import com.demo.blog.model.User;
import com.demo.blog.model.UserPost;
import com.demo.blog.service.PostDetailsService;
import com.demo.blog.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * Blog Admin API - access all the users details along with their posts
 */
@RestController
@RequestMapping("blogApp/admin")
public class AdminController {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PostDetailsService postDetailsService;

    /**
     * @param pageNo  current page to be displayed, starts from 0
     * @param pageSize number of users displayed in a page at a time, default 10
     * @return userPosts - the users along with their posts using Pagination
     */
    @GetMapping("/getAllUsersPosts")
    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                           @RequestParam(defaultValue = "10") Integer pageSize) {

        Optional<List<User>> users = Optional.ofNullable(userDetailsService.getAllUsers());

        Optional<List<User>> usersWithPagination = Optional.ofNullable(userDetailsService.getAllUsersWithPagination(users, pageNo, pageSize));

        Optional<List<Post>> posts = Optional.ofNullable(postDetailsService.getAllPosts());

        Optional<List<UserPost>> userPosts = Optional.ofNullable(userDetailsService.getAllUserDetailsForAllPosts(usersWithPagination, posts));

        Map<String, Object> response = new HashMap<>();
        response.put("userPosts", userPosts);
        response.put("totalItems", users.get().size());
        response.put("currentItems", pageSize);
        response.put("currentPage", pageNo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}