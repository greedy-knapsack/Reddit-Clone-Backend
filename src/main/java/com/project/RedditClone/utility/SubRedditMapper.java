package com.project.RedditClone.utility;

import com.project.RedditClone.dto.SubRedditDto;
import com.project.RedditClone.model.Post;
import com.project.RedditClone.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
@ComponentScan
@Mapper(componentModel = "spring")
public interface SubRedditMapper {
    @Mapping(target = "numberOfPosts",expression = "java(mapPosts(subreddit.getPosts()))")
    SubRedditDto mapSubredditToDto(Subreddit subreddit);
    default Integer mapPosts(List<Post> numberOfPosts){
        return numberOfPosts.size();
    }
    @InheritInverseConfiguration
    @Mapping(target = "posts",ignore = true)
    Subreddit mapDtoToSubreddit(SubRedditDto subRedditDto);
}
