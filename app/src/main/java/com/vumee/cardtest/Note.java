package com.vumee.cardtest;

public class Note {
    private String title;
    private String description;
    private int priority;
    private String image;
    private String lplink;


    public Note() {
        //empty constructor needed
    }



    public Note(String title, String description, String image, String lplink, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.image = image;
        this.lplink = lplink;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public int getPriority() {
        return priority;
    }

    public String getLplink() {
        return lplink;
    }
    public void setLplink(String lplink) {
        this.lplink = lplink;
    }
}