package com.project.RedditClone.repository;

import com.project.RedditClone.dto.CommentsDto;
import com.project.RedditClone.model.Comment;
import com.project.RedditClone.model.Post;
import com.project.RedditClone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
