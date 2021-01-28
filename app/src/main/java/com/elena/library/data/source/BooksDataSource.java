package com.elena.library.data.source;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import com.elena.library.data.api.LibraryApi;
import com.elena.library.data.model.ArticlesResponse;
import com.elena.library.data.model.BookAuthor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class BooksDataSource extends ItemKeyedDataSource<Long, BookAuthor> {
    private final LibraryApi api;
    private final CompositeDisposable cd;
    private final String authorName;
    private int totalPages;
    private int currentPage = 1;

    public BooksDataSource(LibraryApi api, CompositeDisposable compositeDisposable, String authorName) {
        this.api = api;
        this.cd = compositeDisposable;
        this.authorName = authorName;
    }

    @Override
    public void loadInitial(@NonNull @NotNull ItemKeyedDataSource.LoadInitialParams<Long> params, @NonNull @NotNull ItemKeyedDataSource.LoadInitialCallback<BookAuthor> callback) {
        cd.add(
                api.getBooks(1, authorName)
                        .map(response -> {
                            totalPages = response.getTotalPages();
                            return response.getBookAuthors();
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(createInitialObserver(callback))
        );

    }

    private DisposableSingleObserver<List<BookAuthor>> createInitialObserver(LoadInitialCallback<BookAuthor> callback) {
        return new DisposableSingleObserver<List<BookAuthor>>() {
            @Override
            public void onSuccess(@NotNull List<BookAuthor> authors) {
                callback.onResult(authors);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                callback.onResult(new ArrayList<>());
            }
        };
    }

    @Override
    public void loadAfter(@NonNull @NotNull ItemKeyedDataSource.LoadParams<Long> params, @NonNull @NotNull ItemKeyedDataSource.LoadCallback<BookAuthor> callback) {
        currentPage++;
        if (currentPage > totalPages) return;
        cd.add(
                api.getBooks(currentPage, authorName)
                        .map(ArticlesResponse::getBookAuthors)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<BookAuthor>>() {
                            @Override
                            public void onSuccess(@NotNull List<BookAuthor> authors) {
                                callback.onResult(authors);
                            }

                            @Override
                            public void onError(@NotNull Throwable e) {
                                callback.onResult(new ArrayList<>());
                            }
                        })
        );
    }

    @Override
    public void loadBefore(@NonNull @NotNull ItemKeyedDataSource.LoadParams<Long> params, @NonNull @NotNull ItemKeyedDataSource.LoadCallback<BookAuthor> callback) {
    }

    @NonNull
    @NotNull
    @Override
    public Long getKey(@NonNull @NotNull BookAuthor item) {
        return item.getCreatedAt();
    }
}
