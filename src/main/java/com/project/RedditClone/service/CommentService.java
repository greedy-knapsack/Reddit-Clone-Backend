package com.project.RedditClone.service;

import com.project.RedditClone.dto.CommentsDto;
import com.project.RedditClone.exception.PostNotFoundException;
import com.project.RedditClone.model.Comment;
import com.project.RedditClone.model.NotificationEmail;
import com.project.RedditClone.model.Post;
import com.project.RedditClone.model.User;
import com.project.RedditClone.repository.CommentRepository;
import com.project.RedditClone.repository.PostRepository;
import com.project.RedditClone.repository.UserRepository;
import com.project.RedditClone.utility.CommentMapperUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentMapperUtility commentMapperUtility;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    public void createComment(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapperUtility.map(commentsDto,post,authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.
                build(post.getUser().getUserName()+ " posted a comment on your post,"+ POST_URL);
        sendCommentNotification(message,post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendEmail
                (new NotificationEmail(user.getUserName() + " Commented on your post",user.getEmail(),message));
    }

    public List<CommentsDto> getCommentByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post).stream()
                .map(commentMapperUtility::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentsDto> getCommentByUser(String userName) {
        User user = userRepository.findByuserName(userName)
                .orElseThrow(()-> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapperUtility::mapToDto)
                .collect(Collectors.toList());
    }
}
