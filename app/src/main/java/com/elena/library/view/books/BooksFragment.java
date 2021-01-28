package com.elena.library.view.books;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;

import com.elena.library.R;
import com.elena.library.data.model.BookAuthor;
import com.elena.library.databinding.FragmentBooksBinding;
import com.elena.library.view.base.BaseFragment;
import com.elena.library.view.books.adapter.BooksAdapter;
import com.elena.library.viewmodel.BooksViewModel;

import org.jetbrains.annotations.NotNull;

public class BooksFragment extends BaseFragment {
    private FragmentBooksBinding binding;
    private BooksAdapter adapter;

    public BooksFragment() {

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_books, container, false);

        BooksFragmentArgs args = BooksFragmentArgs.fromBundle(getArguments());
        int pageSize = args.getPageSize();
        String authorName = args.getAuthorName();

        BooksViewModel viewModel = new ViewModelProvider(requireActivity()).get(BooksViewModel.class);
        viewModel.fetchData(pageSize, authorName);
        viewModel.getList().observe(getViewLifecycleOwner(), createListObserver());

        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvAuthors.setVisibility(View.GONE);
        binding.tvError.setVisibility(View.GONE);

    }

    private Observer<PagedList<BookAuthor>> createListObserver() {
        return bookAuthors -> {
            if (bookAuthors == null) {
                binding.rvAuthors.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.VISIBLE);
            } else {
                adapter.submitList(bookAuthors);
                binding.rvAuthors.setVisibility(View.VISIBLE);
                binding.tvError.setVisibility(View.GONE);
            }
        };
    }

    private void setupRecyclerView() {
        adapter = new BooksAdapter();
        binding.rvAuthors.setAdapter(adapter);
        binding.rvAuthors.setHasFixedSize(true);
    }
}
