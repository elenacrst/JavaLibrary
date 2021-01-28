package com.elena.library.data.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class BookAuthor {
    private String author;
    private int bookCount = 1;
    @SerializedName("created_at")
    private long createdAt;
    private String title;
    @SerializedName("story_title")
    private String storyTitle;

    public String getTitle() {
        if (!TextUtils.isEmpty(title)) {
            return title;
        }
        if (!TextUtils.isEmpty(storyTitle)) {
            return storyTitle;
        }
        return "NA";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void incrementBookCount() {
        this.bookCount++;
    }

    public String isEven() {
        return bookCount % 2 == 0 ? "*" : "";
    }

    public Integer getBookCount() {
        return bookCount;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookAuthor that = (BookAuthor) o;
        return bookCount == that.bookCount &&
                createdAt == that.createdAt &&
                Objects.equals(author, that.author);
    }
}
