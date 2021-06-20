package com.demo.blog;

import com.demo.blog.model.Post;
import com.demo.blog.service.PostDetailsServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static com.demo.blog.utility.Util.API_URI_GEL_ALL_POSTS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class PostDetailsServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private final PostDetailsServiceImpl postDetailsService = new PostDetailsServiceImpl();

    private List<Post> posts;

    @Before
    void setupAll()
    {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setup() {
        posts = new ArrayList<>();
        posts.add(new Post("1", "1001", "sunt aut facere repellat ", "provident occaecati excepturi optio reprehenderi"));
        posts.add(new Post("2", "1002", "sunt aut facere repellat ", "provident occaecati excepturi optio reprehenderi"));
        posts.add(new Post("3", "1003", "sunt aut facere repellat ", "provident occaecati excepturi optio reprehenderi"));
        posts.add(new Post("4", "1004", "sunt aut facere repellat ", "provident occaecati excepturi optio reprehenderi"));
        posts.add(new Post("5", "1005", "sunt aut facere repellat ", "provident occaecati excepturi optio reprehenderi"));
    }

    @Test
    void getAllPostsRestTemplateCallToGetPostsApiTest()
    {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<String> response = testRestTemplate.exchange(API_URI_GEL_ALL_POSTS, HttpMethod.GET,
                null, new ParameterizedTypeReference<String>() {
                });
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    void getAllPostsMockitoTest()
    {
        Mockito.when(restTemplate.exchange(API_URI_GEL_ALL_POSTS, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Post>>() {
                })).thenReturn(new ResponseEntity<>(posts , HttpStatus.OK));
        List<Post> postList = postDetailsService.getAllPosts();
        Assertions.assertNotNull(postList);
        Assertions.assertEquals(posts.size(), postList.size());
    }
}
