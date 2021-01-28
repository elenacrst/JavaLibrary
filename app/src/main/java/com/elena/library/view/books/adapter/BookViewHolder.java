package com.elena.library.view.books.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.elena.library.data.model.BookAuthor;
import com.elena.library.databinding.ItemBookBinding;

public class BookViewHolder extends RecyclerView.ViewHolder {
    private final ItemBookBinding binding;

    public BookViewHolder(ItemBookBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }

    public void bind(BookAuthor authorData) {
        binding.setBookAuthor(authorData);
    }
}
