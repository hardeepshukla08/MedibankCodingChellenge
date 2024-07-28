package com.audition.controller;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import com.audition.web.AuditionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditionControllerTest {

    @Mock
    private AuditionService auditionService;

    @Mock
    private AuditionLogger auditionLogger;

    @InjectMocks
    private AuditionController auditionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPosts() {
        List<AuditionPost> expectedPosts = new ArrayList<>();
        when(auditionService.getPosts(null)).thenReturn(expectedPosts);

        List<AuditionPost> actualPosts = auditionController.getPosts(null);

        assertEquals(expectedPosts, actualPosts);
        verify(auditionService, times(1)).getPosts(null);
    }

    @Test
    void testGetPost() {
        AuditionPost expectedPost = new AuditionPost();
        when(auditionService.getPostById("1")).thenReturn(expectedPost);

        AuditionPost actualPost = auditionController.getPost("1");

        assertEquals(expectedPost, actualPost);
        verify(auditionService, times(1)).getPostById("1");
    }

    @Test
    void testGetPostWithEmptyId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            auditionController.getPost("");
        });

        String expectedMessage = "Post ID must not be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetComments() {
        List<Comment> expectedComments = new ArrayList<>();
        when(auditionService.getCommentsForPost("1")).thenReturn(expectedComments);

        List<Comment> actualComments = auditionController.getComments("1");

        assertEquals(expectedComments, actualComments);
        verify(auditionService, times(1)).getCommentsForPost("1");
    }

    @Test
    void testGetCommentsWithEmptyId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            auditionController.getComments("");
        });

        String expectedMessage = "Post ID must not be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetPostWithComments() {
        AuditionPost expectedPost = new AuditionPost();
        when(auditionService.getPostWithComments("1")).thenReturn(expectedPost);

        AuditionPost actualPost = auditionController.getPostWithComments("1");

        assertEquals(expectedPost, actualPost);
        verify(auditionService, times(1)).getPostWithComments("1");
    }
}
