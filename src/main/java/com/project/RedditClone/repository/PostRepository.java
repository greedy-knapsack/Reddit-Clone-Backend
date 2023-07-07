package com.project.RedditClone.repository;

import com.project.RedditClone.dto.PostResponse;
import com.project.RedditClone.model.Post;
import com.project.RedditClone.model.Subreddit;
import com.project.RedditClone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>{
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
