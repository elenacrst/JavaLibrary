package com.elena.library.view.books.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;

import com.elena.library.data.model.BookAuthor;
import com.elena.library.databinding.ItemBookBinding;

import org.jetbrains.annotations.NotNull;

import static com.elena.library.view.authors.adapter.AuthorsAdapter.DIFF_CALLBACK;

public class BooksAdapter extends PagedListAdapter<BookAuthor, BookViewHolder> {
    public BooksAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @NotNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemBookBinding binding = ItemBookBinding.inflate(layoutInflater, parent, false);

        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BookViewHolder holder, int position) {
        BookAuthor item = getItem(position);
        holder.bind(item);
    }
}
