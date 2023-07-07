package com.project.RedditClone.utility;

import com.project.RedditClone.dto.CommentsDto;
import com.project.RedditClone.model.Comment;
import com.project.RedditClone.model.Post;
import com.project.RedditClone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapperUtility {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "text",source = "commentsDto.text")
    @Mapping(target = "createdDate",expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment map(CommentsDto commentsDto, Post post, User user);
    @Mapping(target = "postId",expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName",expression = "java(comment.getUser().getUserName())")
    CommentsDto mapToDto(Comment comment);
}
