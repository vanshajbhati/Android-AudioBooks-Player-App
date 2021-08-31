package com.google.android.exoplayer.finalmusicapp;

public class TrackFiles {
    private String title;
    private String singer;
    private String thumbnailUrl;
    private String urlLink;
    private int duration;



    public TrackFiles() {
        this.title = title;
        this.singer = singer;
        this.thumbnailUrl = thumbnailUrl;
        this.urlLink = urlLink;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUrlLink(){ return urlLink;}

    public void setUrlLink(String urlLink){ this.urlLink= urlLink;}

    public int getduration(){ return duration;}

    public void getduration(int duration){ this.duration = duration;}


}


