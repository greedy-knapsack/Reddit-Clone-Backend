package com.project.RedditClone.exception;

public class SubRedditNotFoundException extends RuntimeException{
    public SubRedditNotFoundException(String message){
        super(message);
    }
}
