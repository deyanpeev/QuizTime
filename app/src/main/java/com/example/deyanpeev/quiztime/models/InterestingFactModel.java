package com.example.deyanpeev.quiztime.models;

public class InterestingFactModel {
    private String shortTag;
    private String description;

    public InterestingFactModel(){

    }

    public InterestingFactModel(String shortTag, String description){
        this.setShortTag(shortTag);
        this.setDescription(description);
    }

    public String getShortTag(){
        return this.shortTag;
    }

    public void setShortTag(String shortTag){
        this.shortTag = shortTag;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
