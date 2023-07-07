package com.project.RedditClone.service;

import com.project.RedditClone.dto.PostRequest;
import com.project.RedditClone.dto.PostResponse;
import com.project.RedditClone.exception.PostNotFoundException;
import com.project.RedditClone.exception.SubRedditNotFoundException;
import com.project.RedditClone.utility.PostMapperUtility;
import com.project.RedditClone.model.Post;
import com.project.RedditClone.model.Subreddit;
import com.project.RedditClone.model.User;
import com.project.RedditClone.repository.PostRepository;
import com.project.RedditClone.repository.SubredditRepository;
import com.project.RedditClone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final SubredditRepository subredditRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final PostMapperUtility postMapperUtility;
    private final UserRepository userRepository;
    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubRedditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapperUtility.map(postRequest,subreddit,authService.getCurrentUser()));
    }
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapperUtility.mapToDto(post);
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubReddit(Long subRedditId) {
        Subreddit subreddit = subredditRepository.findById(subRedditId)
                .orElseThrow(() -> new SubRedditNotFoundException(subRedditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapperUtility::mapToDto).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByuserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapperUtility::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapperUtility::mapToDto)
                .collect(Collectors.toList());
    }
}
