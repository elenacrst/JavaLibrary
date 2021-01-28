package com.elena.library.view.authors.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.elena.library.data.model.BookAuthor;
import com.elena.library.databinding.ItemAuthorBinding;

import org.jetbrains.annotations.NotNull;

public class AuthorsAdapter extends ListAdapter<BookAuthor, AuthorViewHolder> {
    private final AuthorCallback callback;

    public AuthorsAdapter(AuthorCallback callback) {
        super(DIFF_CALLBACK);
        this.callback = callback;
    }

    @androidx.annotation.NonNull
    @NotNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@androidx.annotation.NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemAuthorBinding binding = ItemAuthorBinding.inflate(layoutInflater, parent, false);

        return new AuthorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder,
                                 int position) {
        BookAuthor bookAuthor = getItem(position);
        if (bookAuthor != null) {
            holder.bind(bookAuthor);
            holder.itemView.setOnClickListener(v -> callback.onItemClick(getItem(holder.getAdapterPosition())));
        }
    }

    public static final DiffUtil.ItemCallback<BookAuthor> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<BookAuthor>() {
                @Override
                public boolean areItemsTheSame(BookAuthor old, BookAuthor newData) {
                    return old.getCreatedAt() == newData.getCreatedAt();
                }

                @Override
                public boolean areContentsTheSame(BookAuthor old,
                                                  @NotNull BookAuthor newData) {
                    return old.equals(newData);
                }
            };
}