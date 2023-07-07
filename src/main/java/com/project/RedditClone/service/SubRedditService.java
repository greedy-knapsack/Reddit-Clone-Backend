package com.project.RedditClone.service;

import com.project.RedditClone.dto.SubRedditDto;
import com.project.RedditClone.exception.SpringRedditException;
import com.project.RedditClone.model.Subreddit;
import com.project.RedditClone.repository.SubredditRepository;
import com.project.RedditClone.utility.PostMapperUtility;
import com.project.RedditClone.utility.SubRedditMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.Instant.now;

@Service
@Slf4j
@AllArgsConstructor
public class SubRedditService {
    private final SubRedditMapper subRedditMapper;
    private final SubredditRepository subredditRepository;

    @Transactional
    public SubRedditDto save(SubRedditDto subRedditDto){
        Subreddit save = subredditRepository.save(subRedditMapper.mapDtoToSubreddit(subRedditDto));
        subRedditDto.setId(save.getId());
        return subRedditDto;
    }
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<SubRedditDto> getAll() {
        return subredditRepository.findAll()
                .stream().map(subRedditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    public SubRedditDto getSubReddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with this id: "+ id));
        return subRedditMapper.mapSubredditToDto(subreddit);
    }
}
