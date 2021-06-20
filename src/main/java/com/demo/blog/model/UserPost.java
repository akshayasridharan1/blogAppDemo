package com.demo.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class UserPost {

    private User user;

    @JsonIgnoreProperties(value = {"userId", "id"})
    private List<Post> posts;

    private int numOfPosts;

    public UserPost() {
    }

    public UserPost(User user, List<Post> posts) {
        this.user = user;
        this.posts = posts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public int getNumOfPosts() {
        return numOfPosts;
    }

    public void setNumOfPosts(int numOfPosts) {
        this.numOfPosts = numOfPosts;
    }
}