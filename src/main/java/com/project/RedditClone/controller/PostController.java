package com.project.RedditClone.controller;

import com.project.RedditClone.dto.PostRequest;
import com.project.RedditClone.dto.PostResponse;
import com.project.RedditClone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController{

    private final PostService postService;
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest){
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(id));
    }
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(Long id){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsBySubReddit(id));
    }
    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String name){
        return status(HttpStatus.OK).body(postService.getPostsByUsername(name));
    }
}
