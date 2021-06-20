package com.demo.blog.service;

import com.demo.blog.exception.UserPostsNotFoundException;
import com.demo.blog.model.Post;
import com.demo.blog.model.User;
import com.demo.blog.model.UserPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.demo.blog.utility.Util.API_URI_GET_ALL_USERS;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<User> getAllUsers() {
        ResponseEntity<List<User>> userDetailsResponse =
                restTemplate.exchange(API_URI_GET_ALL_USERS, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
                });
        Optional<List<User>> users = Optional.ofNullable(userDetailsResponse.getBody());

        if (!users.isPresent()) {
            throw new UserPostsNotFoundException("No users and no posts are available");
        }
        return users.get();
    }

    @Override
    public List<User> getAllUsersWithPagination(Optional<List<User>> users, Integer pageNo, Integer pageSize) {
        if (users.isPresent()) {
            if (pageNo + pageSize > users.get().size())
                throw new UserPostsNotFoundException(" pageNo and pageSize together cannot exceed the available  list : " + users.get().size());
        } else {
            throw new UserPostsNotFoundException("users not found exception");
        }
        return users.get().subList(pageNo, pageNo + pageSize);
    }

    /**
     * @param users all users
     * @param posts all posts of all users
     * @return userPosts all users along with their posts are returned (paginated values are iterated)
     */
    @Override
    public List<UserPost> getAllUserDetailsForAllPosts(Optional<List<User>> users, Optional<List<Post>> posts) {
        List<UserPost> userPosts = new ArrayList<>();
        if (users.isPresent()) {
            for (User user : users.get()) {
                UserPost userPost = new UserPost();
                userPost.setUser(user);
                posts.ifPresent(postDetails -> userPost.setPosts(postDetails.stream()
                        .filter(p -> p.getUserId().equals(user.getId()))
                        .collect(Collectors.toList())));
                if (Objects.nonNull(userPost.getPosts()))
                    userPost.setNumOfPosts(userPost.getPosts().size());
                userPosts.add(userPost);

            }
        }
        return userPosts;
    }

}
