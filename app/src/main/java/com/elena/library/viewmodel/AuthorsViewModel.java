package com.elena.library.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elena.library.data.api.RequestEvent;
import com.elena.library.data.api.RequestResult;
import com.elena.library.data.api.LibraryApi;
import com.elena.library.data.api.RetrofitClient;
import com.elena.library.data.model.ArticlesResponse;
import com.elena.library.data.model.BookAuthor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AuthorsViewModel extends ViewModel {
    private final MutableLiveData<RequestEvent<RequestResult>> bookAuthorsEvent = new MutableLiveData<>();
    private final MutableLiveData<List<BookAuthor>> bookAuthors = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Integer> totalPages = new MutableLiveData<>();
    private final MutableLiveData<Integer> pageSize = new MutableLiveData<>();
    private final LibraryApi api = RetrofitClient.getApi();

    public LiveData<List<BookAuthor>> getBookAuthors() {
        return bookAuthors;
    }

    public LiveData<RequestEvent<RequestResult>> getBookAuthorsEvent() {
        return bookAuthorsEvent;
    }

    public void fetchAuthorsData() {
        bookAuthorsEvent.setValue(new RequestEvent<>(new RequestResult.Loading()));
        compositeDisposable.add(
                api.getBookAuthors(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(createFirstPageObserver())
        );
    }

    private DisposableSingleObserver<ArticlesResponse> createFirstPageObserver() {
        return new DisposableSingleObserver<ArticlesResponse>() {

            @Override
            public void onSuccess(@NotNull ArticlesResponse response) {
                AuthorsViewModel.this.bookAuthors.setValue(response.getBookAuthors());
                totalPages.setValue(response.getTotalPages());
                pageSize.setValue(response.getPageSize());
                List<Single<ArticlesResponse>> requests = new ArrayList<>();
                for (int i = 2; i < response.getTotalPages(); i++) {
                    Single<ArticlesResponse> data = api.getBookAuthors(i)
                            .subscribeOn(Schedulers.io());
                    requests.add(data);
                }
                Single<List<BookAuthor>> observable = Single.zip(requests, objects -> {
                    List<BookAuthor> list = bookAuthors.getValue() != null ? bookAuthors.getValue() : new ArrayList<>();
                    for (Object o : objects) {
                        if (o instanceof ArticlesResponse) {
                            list.addAll(((ArticlesResponse) o).getBookAuthors());
                        }
                    }
                    return list;
                })
                        .subscribeOn(Schedulers.io());
                compositeDisposable.add(
                        observable.observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableSingleObserver<List<BookAuthor>>() {

                                    @Override
                                    public void onSuccess(@NotNull List<BookAuthor> bookAuthors) {
                                        AuthorsViewModel.this.bookAuthors.setValue(bookAuthors);
                                        mapUniqueAuthors();
                                    }

                                    @Override
                                    public void onError(@NotNull Throwable e) {
                                        bookAuthorsEvent.setValue(new RequestEvent<>(new RequestResult.Error()));
                                    }
                                })
                );

            }

            @Override
            public void onError(@NotNull Throwable e) {
                bookAuthorsEvent.setValue(new RequestEvent<>(new RequestResult.Error()));
            }
        };
    }

    private void mapUniqueAuthors() {
        List<BookAuthor> authors = new ArrayList<>();
        if (bookAuthors.getValue() != null) {
            for (BookAuthor b : bookAuthors.getValue()) {
                authors = increaseExistingElseInsert(authors, b);
            }
        }

        bookAuthors.setValue(authors);
        bookAuthorsEvent.setValue(new RequestEvent<>(new RequestResult.Success()));
    }

    private List<BookAuthor> increaseExistingElseInsert(List<BookAuthor> authors, BookAuthor author) {
        List<BookAuthor> bookAuthors = new ArrayList<>();
        boolean found = false;
        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i).getAuthor().equals(author.getAuthor())) {
                BookAuthor changed = authors.get(i);
                changed.incrementBookCount();
                bookAuthors.add(changed);
                found = true;
            } else {
                bookAuthors.add(authors.get(i));
            }
        }
        if (!found) {
            bookAuthors.add(author);
        }
        return bookAuthors;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public LiveData<Integer> getPageSize() {
        return pageSize;
    }
}
