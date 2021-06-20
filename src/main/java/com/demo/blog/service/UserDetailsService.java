package com.demo.blog.service;


import com.demo.blog.model.Post;
import com.demo.blog.model.User;
import com.demo.blog.model.UserPost;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserDetailsService {

    public List<UserPost> getAllUserDetailsForAllPosts(Optional<List<User>> users, Optional<List<Post>> posts);

    public List<User> getAllUsers();

    public List<User> getAllUsersWithPagination(Optional<List<User>> users, Integer pageNo, Integer pageSize);

}
