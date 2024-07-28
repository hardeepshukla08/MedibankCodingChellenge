package com.audition.integration;

import com.audition.common.constants.AuditionConstants;
import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.audition.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class AuditionIntegrationClient {

    private static final Logger logger = LoggerFactory.getLogger(AuditionIntegrationClient.class);

    @Autowired
    private AuditionLogger auditionLogger;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Fetches posts from the external API and filters them by category.
     *
     * @param category the category to filter posts by
     * @return the list of posts, filtered by category if provided
     */
    public List<AuditionPost> getPosts(String category) {
        auditionLogger.info(logger, "Fetching posts with category: " + category);
        String url = AuditionConstants.BASE_URL +"/posts";
        try {
            AuditionPost[] postsArray = restTemplate.getForObject(url, AuditionPost[].class);
            List<AuditionPost> posts = postsArray != null ? Arrays.asList(postsArray) : new ArrayList<>();
            if (category != null && !category.isEmpty()) {
                posts = posts.stream()
                        .filter(post -> category.equalsIgnoreCase(post.getCategory()))
                        .collect(Collectors.toList());
            }
            auditionLogger.info(logger, "Fetched " + posts.size() + " posts with category: " + category);
            return posts;
        } catch (HttpClientErrorException e) {
            auditionLogger.logErrorWithException(logger, "Error fetching posts with category: " + category, e);
            throw new SystemException("An error occurred while fetching posts: " + e.getMessage(), e.getStatusCode().toString(), e.getStatusCode().value());
        }
    }


    /**
     * Fetches a post by its ID from the external API.
     *
     * @param id the ID of the post
     * @return the fetched post
     */
    public AuditionPost getPostById(final String id) {
        auditionLogger.info(logger, "Fetching post with ID: " + id);
        String url = AuditionConstants.BASE_URL + "/posts/" + id;
        try {
            AuditionPost post = restTemplate.getForObject(url, AuditionPost.class);
            auditionLogger.info(logger, "Fetched post with ID: " + id);
            return post;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                auditionLogger.logErrorWithException(logger, "Cannot find a Post with id " + id, e);
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found", 404);
            } else {
                auditionLogger.logErrorWithException(logger, "Error fetching post with ID: " + id, e);
                throw new SystemException("An error occurred: " + e.getMessage(), e.getStatusCode().toString(), e.getStatusCode().value());
            }
        }
    }


    /**
     * Fetches a post and its comments by post ID from the external API.
     *
     * @param id the ID of the post
     * @return the fetched post with its comments
     */
    public AuditionPost getPostWithComments(final String id) {
        auditionLogger.info(logger, "Fetching post with comments for post ID: " + id);
        String postUrl = AuditionConstants.BASE_URL + "/posts/" + id;
        String commentsUrl = AuditionConstants.BASE_URL+ "/posts/" + id + "/comments";

        try {
            AuditionPost post = restTemplate.getForObject(postUrl, AuditionPost.class);
            Comment[] commentsArray = restTemplate.getForObject(commentsUrl, Comment[].class);

            if (post != null && commentsArray != null) {
                post.setComments(Arrays.asList(commentsArray));
            }

            auditionLogger.info(logger, "Fetched post with comments for post ID: " + id);
            return post;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                auditionLogger.logErrorWithException(logger, "Cannot find a Post with id " + id, e);
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found", 404);
            } else {
                auditionLogger.logErrorWithException(logger, "Error fetching post with comments for post ID: " + id, e);
                throw new SystemException("An error occurred: " + e.getMessage(), e.getStatusCode().toString(), e.getStatusCode().value());
            }
        }
    }


    /**
     * Fetches comments for a specific post by post ID from the external API.
     *
     * @param postId the ID of the post
     * @return the list of comments for the post
     */
    public List<Comment> getCommentsForPost(final String postId) {
        auditionLogger.info(logger, "Fetching comments for post with ID: " + postId);
        String url = AuditionConstants.BASE_URL + "/comments?postId=" + postId;
        try {
            Comment[] commentsArray = restTemplate.getForObject(url, Comment[].class);
            List<Comment> comments = commentsArray != null ? Arrays.asList(commentsArray) : new ArrayList<>();
            auditionLogger.info(logger, "Fetched " + comments.size() + " comments for post with ID: " + postId);
            return comments;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                auditionLogger.logErrorWithException(logger, "Cannot find comments for post with id " + postId, e);
                throw new SystemException("Cannot find comments for post with id " + postId, "Resource Not Found", 404);
            } else {
                auditionLogger.logErrorWithException(logger, "Error fetching comments for post with ID: " + postId, e);
                throw new SystemException("An error occurred: " + e.getMessage(), e.getStatusCode().toString(), e.getStatusCode().value());
            }
        }
    }

}
