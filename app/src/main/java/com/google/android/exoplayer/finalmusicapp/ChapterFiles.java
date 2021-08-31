package com.google.android.exoplayer.finalmusicapp;

public class ChapterFiles {
    private String chapterName;
    private String chapterUrl;
    private int duration;

    public ChapterFiles(String chapterName, String chapterUrl, int duration) {
        this.chapterName = chapterName;
        this.chapterUrl = chapterUrl;
        this.duration = duration;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
