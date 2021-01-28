package com.elena.library.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticlesResponse {
    private int page;
    @SerializedName("per_page")
    private int pageSize;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("data")
    private List<BookAuthor> bookAuthors;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<BookAuthor> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(List<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }
}
