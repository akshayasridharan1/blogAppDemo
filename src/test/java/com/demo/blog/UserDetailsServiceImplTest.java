package com.demo.blog;
import org.junit.jupiter.api.Test;
import com.demo.blog.exception.UserPostsNotFoundException;
import com.demo.blog.model.Company;
import com.demo.blog.model.Post;
import com.demo.blog.model.User;
import com.demo.blog.model.UserPost;
import com.demo.blog.model.address.Address;
import com.demo.blog.model.address.Geo;
import com.demo.blog.service.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static com.demo.blog.utility.Util.API_URI_GET_ALL_USERS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private final UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();

    @Mock
    private RestTemplate restTemplate;

    private List<User> users;

    private List<Post> posts;

    @Before
    void setupAll()
    {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setup() {
        users = new ArrayList<>();
        users.add(new User("1", "John", "John King", "johnking@bt.com", new Address("Bal st", "41", "Sydney", "2000", new Geo(42.7864, 36.7868)), "0477737", "http://insurance.com",new Company("Financial services", "We care for your Health", "real time markets")));
        users.add(new User("2", "Bella", "Bella George", "bellageorge@bt.com", new Address("rom st", "17", "Sydney", "2000", new Geo(20.7864, 10.7868)), "0477937", "http://insurancehi5.com",new Company("Financial services", "We care for your Health", "real time markets")));

        posts = new ArrayList<>();
        posts.add(new Post("1", "1001", "sunt aut facere repellat ", "provident occaecati excepturi optio reprehenderi"));
        posts.add(new Post("2", "1002", "sunt aut facere repellat ", "provident occaecati excepturi optio reprehenderi"));
        posts.add(new Post("3", "1003", "sunt aut facere repellat ", "provident occaecati excepturi optio reprehenderi"));
        posts.add(new Post("4", "1004", "sunt aut facere repellat ", "provident occaecati excepturi optio reprehenderi"));
        posts.add(new Post("5", "1005", "sunt aut facere repellat ", "provident occaecati excepturi optio reprehenderi"));
    }

    @Test
    void getAllUserDetailsForAllPostsSuccessTest()
    {
        List<UserPost> userPosts =  userDetailsService.getAllUserDetailsForAllPosts(Optional.of(users), Optional.of(posts));
        Assertions.assertNotNull(userPosts);
        Assertions.assertEquals(2, userPosts.size());
    }

    @Test
    void getAllUserDetailsForAllPostsEmptyTest()
    {
        List<UserPost> userPosts =  userDetailsService.getAllUserDetailsForAllPosts(Optional.empty(), Optional.empty());
        Assertions.assertNotNull(userPosts);
        Assertions.assertEquals(0, userPosts.size());
    }

    @Test
    void getAllUserDetailsForAllUsersAndPostsEmptyTest()
    {
        List<UserPost> userPosts =  userDetailsService.getAllUserDetailsForAllPosts(Optional.of(users), Optional.empty());
        Assertions.assertNotNull(userPosts);
        Assertions.assertEquals(2, userPosts.size());
    }

    @Test
    void getAllUserDetailsForAllUsersEmptyAndPostsExistsTest()
    {
        List<UserPost> userPosts =  userDetailsService.getAllUserDetailsForAllPosts(Optional.empty(), Optional.of(posts));
        Assertions.assertNotNull(userPosts);
        Assertions.assertEquals(0, userPosts.size());
    }

    @Test
    void getAllUsersWithPaginationSuccessTest()
    {
        List<User> userList =  userDetailsService.getAllUsersWithPagination(Optional.of(users), 0, 2);
        Assertions.assertNotNull(userList);
        Assertions.assertEquals(2, userList.size());
    }

    @Test
    void getAllUsersWithPaginationFailTest()
    {
        assertThatThrownBy(() -> userDetailsService.getAllUsersWithPagination(Optional.of(users), 0, 3))
                .isInstanceOf(UserPostsNotFoundException.class);
    }

    @Test
    void getAllUsersWithPaginationFailPageNoTest()
    {
        assertThatThrownBy(() -> userDetailsService.getAllUsersWithPagination(Optional.of(users), 4, 9))
                .isInstanceOf(UserPostsNotFoundException.class);
    }

    @Test
    void getAllUsersWithPaginationFailPageSizeZeroTest()
    {
        List<User> userList =  userDetailsService.getAllUsersWithPagination(Optional.of(users), 2, 0);
        Assertions.assertNotNull(userList);
        Assertions.assertEquals(0, userList.size());
    }

    @Test
    void getAllUsersRestTemplateCallToGetUsersApiTest()
    {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<String> response = testRestTemplate.exchange(API_URI_GET_ALL_USERS, HttpMethod.GET,
                null, new ParameterizedTypeReference<String>() {
                });

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    void getAllUsersRestTemplateCallToGetUsersApiTestFail()
    {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<String> response = testRestTemplate.exchange(API_URI_GET_ALL_USERS, HttpMethod.GET,
                null, new ParameterizedTypeReference<String>() {
                });

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    void getAllUsersMockitoTest() {
        Mockito.when(restTemplate.exchange(API_URI_GET_ALL_USERS, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<User>>() {
                })).thenReturn(new ResponseEntity<>(users, HttpStatus.OK));
        List<User> userList = userDetailsService.getAllUsers();
        Assertions.assertNotNull(userList);
        Assertions.assertEquals(users.size(), userList.size());
    }
}