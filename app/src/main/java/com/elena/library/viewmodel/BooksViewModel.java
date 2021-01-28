package com.elena.library.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.elena.library.data.api.RetrofitClient;
import com.elena.library.data.model.BookAuthor;
import com.elena.library.data.source.BooksDataSourceFactory;

import io.reactivex.disposables.CompositeDisposable;

public class BooksViewModel extends ViewModel {
    private LiveData<PagedList<BookAuthor>> list;
    private final CompositeDisposable cd = new CompositeDisposable();

    public void fetchData(int pageSize, String authorName) {
        BooksDataSourceFactory dataSourceFactory = new BooksDataSourceFactory(cd, RetrofitClient.getApi(), authorName);
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build();
        list = new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        cd.dispose();
    }

    public LiveData<PagedList<BookAuthor>> getList() {
        return list;
    }
}
