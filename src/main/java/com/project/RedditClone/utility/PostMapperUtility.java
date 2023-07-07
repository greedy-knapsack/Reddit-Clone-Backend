package com.project.RedditClone.utility;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.project.RedditClone.dto.PostRequest;
import com.project.RedditClone.dto.PostResponse;
import com.project.RedditClone.model.*;
import com.project.RedditClone.repository.CommentRepository;
import com.project.RedditClone.repository.VoteRepository;
import com.project.RedditClone.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.project.RedditClone.model.VoteType.DOWNVOTE;
import static com.project.RedditClone.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapperUtility {
        @Autowired
        private CommentRepository commentRepository;
        @Autowired
        private AuthService authService;
        @Autowired
        private VoteRepository voteRepository;

        @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
        @Mapping(target = "description", source = "postRequest.description")
        @Mapping(target = "subreddit",source = "subreddit")
        @Mapping(target = "voteCount", constant = "0")
        @Mapping(target = "user",source = "user")
        public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

        @Mapping(target = "id", source = "postId")
        @Mapping(target = "postName", source = "postName")
        @Mapping(target = "subredditName", source = "subreddit.name")
        @Mapping(target = "userName", source = "user.userName")
        @Mapping(target = "commentCount", expression = "java(commentCount(post))")
        @Mapping(target = "duration",expression = "java(getDuration(post))")
        @Mapping(target = "upVote",expression = "java(isPostUpVoted(post))")
        @Mapping(target = "downVote",expression = "java(isPostDownVoted(post))")
        public abstract PostResponse mapToDto(Post post);

        Integer commentCount(Post post){
                return commentRepository.findByPost(post).size();
        }
        String getDuration(Post post){
                return TimeAgo.using(post.getCreatedDate().toEpochMilli());
        }
        boolean isPostUpVoted(Post post) {
                return checkVoteType(post, UPVOTE);
        }

        boolean isPostDownVoted(Post post) {
                return checkVoteType(post, DOWNVOTE);
        }

        private boolean checkVoteType(Post post, VoteType voteType) {
                if (authService.isLoggedIn()) {
                        Optional<Vote> voteForPostByUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                                authService.getCurrentUser());
                        return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                                .isPresent();
                }
                return false;
        }
}

