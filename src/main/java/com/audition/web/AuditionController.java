package com.audition.web;

import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import java.util.List;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuditionController {

    private final AuditionService auditionService;

    private final AuditionLogger auditionLogger;

    private static final Logger logger = LoggerFactory.getLogger(AuditionController.class);

    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(@RequestParam(required = false) String category) {

        auditionLogger.info(logger, "Fetching posts with category: " + category);
        List<AuditionPost> posts = auditionService.getPosts(category);
        auditionLogger.info(logger, "Fetched " + posts.size() + " posts");
        return posts;
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPost(@PathVariable("id") final String postId) {

        if (postId == null || postId.trim().isEmpty()) {
            throw new IllegalArgumentException("Post ID must not be null or empty");
        }
        auditionLogger.info(logger, "Fetching post with ID: " + postId);
        AuditionPost post = auditionService.getPostById(postId);
        auditionLogger.info(logger, "Fetched post with ID: " + postId);
        return post;
    }

    @RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Comment> getComments(@PathVariable("id") final String postId) {
        if (postId == null || postId.trim().isEmpty()) {
            throw new IllegalArgumentException("Post ID must not be null or empty");
        }
        auditionLogger.info(logger, "Fetching comments for post with ID: " + postId);
        List<Comment> comments = auditionService.getCommentsForPost(postId);
        auditionLogger.info(logger, "Fetched " + comments.size() + " comments for post with ID: " + postId);
        return comments;
    }

    @RequestMapping(value = "/posts/{id}/with-comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPostWithComments(@PathVariable("id") final String postId) {

        auditionLogger.info(logger, "Fetching post with comments for post ID: " + postId);
        AuditionPost auditionPost = auditionService.getPostWithComments(postId);
        auditionLogger.info(logger, "Fetched post with comments for post ID: " + postId);
        return auditionPost;
    }

}
