package com.elena.library.data.api;

import com.elena.library.data.model.ArticlesResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LibraryApi {
    @GET("/api/articles")
    Single<ArticlesResponse> getBookAuthors(@Query("page") int page);

    @GET("/api/articles")
    Single<ArticlesResponse> getBooks(@Query("page") int page, @Query("author") String authorName);
}
