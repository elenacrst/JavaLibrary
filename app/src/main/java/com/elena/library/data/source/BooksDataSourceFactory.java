package com.elena.library.data.source;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.elena.library.data.api.LibraryApi;
import com.elena.library.data.model.BookAuthor;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.CompositeDisposable;

public class BooksDataSourceFactory extends DataSource.Factory<Long, BookAuthor> {
    private final MutableLiveData<BooksDataSource> dataSource = new MutableLiveData<>();
    private final CompositeDisposable cd;
    private final LibraryApi api;
    private final String authorName;

    public BooksDataSourceFactory(CompositeDisposable cd, LibraryApi api, String authorName) {
        this.cd = cd;
        this.api = api;
        this.authorName = authorName;
    }

    @NonNull
    @NotNull
    @Override
    public DataSource<Long, BookAuthor> create() {
        BooksDataSource dataSource = new BooksDataSource(api, cd, authorName);
        this.dataSource.postValue(dataSource);
        return dataSource;
    }
}
