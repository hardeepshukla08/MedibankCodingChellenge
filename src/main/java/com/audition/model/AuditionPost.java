package com.audition.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AuditionPost {

    private int userId;
    private int id;
    private String title;
    private String body;
    private String category;
    private List<Comment> comments;

    public AuditionPost(int userId, int id, String title, String body, String category, List<Comment> comments) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
        this.category = category;
        this.comments = comments == null ? null : new ArrayList<>(comments);
    }

    public List<Comment> getComments() {
        return comments == null ? null : new ArrayList<>(comments);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments == null ? null : new ArrayList<>(comments);
    }
}
