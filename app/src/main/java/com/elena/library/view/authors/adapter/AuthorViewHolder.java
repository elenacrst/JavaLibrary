package com.elena.library.view.authors.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.elena.library.data.model.BookAuthor;
import com.elena.library.databinding.ItemAuthorBinding;

public class AuthorViewHolder extends RecyclerView.ViewHolder {
    private final ItemAuthorBinding binding;

    public AuthorViewHolder(ItemAuthorBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }

    public void bind(BookAuthor authorData) {
        binding.setBookAuthor(authorData);
    }
}
