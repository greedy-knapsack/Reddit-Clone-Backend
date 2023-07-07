package com.project.RedditClone.service;

import com.project.RedditClone.dto.VoteDto;
import com.project.RedditClone.exception.PostNotFoundException;
import com.project.RedditClone.exception.SpringRedditException;
import com.project.RedditClone.model.Post;
import com.project.RedditClone.model.Vote;
import com.project.RedditClone.repository.PostRepository;
import com.project.RedditClone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.project.RedditClone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    public void vote(VoteDto voteDto) {
        System.out.println(voteDto.getPostId());
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        System.out.println(authService.getCurrentUser());
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,authService.getCurrentUser());
        if(voteByPostAndUser.isPresent() &&
        voteByPostAndUser.get().getVoteType()
                .equals(voteDto.getVoteType())){
            throw new SpringRedditException("You have already "+
                    voteDto.getVoteType() + "'d for this  post");
        }
        if(UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount()+1);
        }
        else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto,post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
