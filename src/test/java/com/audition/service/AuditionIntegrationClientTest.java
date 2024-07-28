package com.audition.service;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditionIntegrationClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AuditionLogger auditionLogger;

    @InjectMocks
    private AuditionIntegrationClient auditionIntegrationClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPosts() {
        AuditionPost[] postsArray = {
                new AuditionPost(1, 1, "Post 1", "Body 1", "Category1",null),
                new AuditionPost(2, 2, "Post 2", "Body 2", "Category2",null)
        };
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts", AuditionPost[].class))
                .thenReturn(postsArray);

        List<AuditionPost> posts = auditionIntegrationClient.getPosts(null);

        assertNotNull(posts);
        assertEquals(2, posts.size());
        verify(restTemplate, times(1)).getForObject("https://jsonplaceholder.typicode.com/posts", AuditionPost[].class);
    }

    @Test
    void testGetPostsWithCategory() {
        AuditionPost[] postsArray = {
                new AuditionPost(1, 1, "Post 1", "Body 1", "Category1",null),
                new AuditionPost(2, 2, "Post 2", "Body 2", "Category2",null)
        };
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts", AuditionPost[].class))
                .thenReturn(postsArray);

        List<AuditionPost> posts = auditionIntegrationClient.getPosts("Category1");

        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals("Category1", posts.get(0).getCategory());
        verify(restTemplate, times(1)).getForObject("https://jsonplaceholder.typicode.com/posts", AuditionPost[].class);
    }

    @Test
    void testGetPostById() {
        AuditionPost post = new AuditionPost(1, 1, "Post 1", "Body 1", "Category1",null);
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/1", AuditionPost.class))
                .thenReturn(post);

        AuditionPost result = auditionIntegrationClient.getPostById("1");

        assertNotNull(result);
        assertEquals(post, result);
        verify(restTemplate, times(1)).getForObject("https://jsonplaceholder.typicode.com/posts/1", AuditionPost.class);
    }

    @Test
    void testGetPostByIdNotFound() {
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/1", AuditionPost.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Exception exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostById("1");
        });

        String expectedMessage = "Cannot find a Post with id 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetPostWithComments() {
        Comment[] commentsArray = {
                new Comment(1, 1, "Comment 1", "comment1@test.com", "Body 1"),
                new Comment(2, 1, "Comment 2", "comment2@test.com", "Body 2")
        };
        AuditionPost post = new AuditionPost(1, 1, "Post 1", "Body 1", "Category1", List.of(commentsArray));


        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/1", AuditionPost.class))
                .thenReturn(post);
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/1/comments", Comment[].class))
                .thenReturn(commentsArray);

        AuditionPost result = auditionIntegrationClient.getPostWithComments("1");

        assertNotNull(result);
        assertEquals(post, result);
        assertEquals(2, result.getComments().size());
        verify(restTemplate, times(1)).getForObject("https://jsonplaceholder.typicode.com/posts/1", AuditionPost.class);
        verify(restTemplate, times(1)).getForObject("https://jsonplaceholder.typicode.com/posts/1/comments", Comment[].class);
    }

    @Test
    void testGetCommentsForPost() {
        Comment[] commentsArray = {
                new Comment(1, 1, "Comment 1", "comment1@test.com", "Body 1"),
                new Comment(2, 1, "Comment 2", "comment2@test.com", "Body 2")
        };
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/comments?postId=1", Comment[].class))
                .thenReturn(commentsArray);

        List<Comment> comments = auditionIntegrationClient.getCommentsForPost("1");

        assertNotNull(comments);
        assertEquals(2, comments.size());
        verify(restTemplate, times(1)).getForObject("https://jsonplaceholder.typicode.com/comments?postId=1", Comment[].class);
    }

    @Test
    void testGetCommentsForPostNotFound() {
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/comments?postId=1", Comment[].class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Exception exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getCommentsForPost("1");
        });

        String expectedMessage = "Cannot find comments for post with id 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
