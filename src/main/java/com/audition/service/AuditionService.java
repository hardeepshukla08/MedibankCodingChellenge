package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import java.util.List;

import com.audition.model.Comment;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuditionService {

    private AuditionIntegrationClient auditionIntegrationClient;

    public List<AuditionPost> getPosts(String category) {
        return auditionIntegrationClient.getPosts(category);
    }

    public AuditionPost getPostById(final String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    public List<Comment> getCommentsForPost(String postId) {
        return auditionIntegrationClient.getCommentsForPost(postId);
    }

    public AuditionPost getPostWithComments(String postId) {
        return auditionIntegrationClient.getPostWithComments(postId);
    }
}
