package com.elena.library.view.authors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.elena.library.R;
import com.elena.library.data.api.RequestEvent;
import com.elena.library.data.api.RequestResult;
import com.elena.library.databinding.FragmentAuthorsBinding;
import com.elena.library.view.authors.adapter.AuthorsAdapter;
import com.elena.library.view.base.BaseFragment;
import com.elena.library.view.base.LoadCallback;
import com.elena.library.viewmodel.AuthorsViewModel;

import org.jetbrains.annotations.NotNull;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class AuthorsFragment extends BaseFragment {
    private FragmentAuthorsBinding binding;
    private AuthorsAdapter adapter;
    private AuthorsViewModel viewModel;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_authors, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthorsViewModel.class);
        binding.setViewModel(viewModel);
        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (viewModel.getBookAuthors().getValue() == null) {
            viewModel.fetchAuthorsData();
        } else {
            onRequestSuccess();
        }

        viewModel.getBookAuthorsEvent().observe(getViewLifecycleOwner(), createBookAuthorsLoadingObserver());
    }

    private Observer<RequestEvent<RequestResult>> createBookAuthorsLoadingObserver() {
        return createLoadingObserver(new LoadCallback() {
            @Override
            public void onProgress() {
                binding.pbLoading.setVisibility(View.VISIBLE);
                binding.content.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(RequestResult.Success result) {
                onRequestSuccess();
            }

            @Override
            public void onError() {
                binding.pbLoading.setVisibility(View.GONE);
                binding.content.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onRequestSuccess() {
        adapter.submitList(viewModel.getBookAuthors().getValue());
        binding.pbLoading.setVisibility(View.GONE);
        binding.content.setVisibility(View.VISIBLE);
        binding.tvError.setVisibility(View.GONE);
    }

    private void setupRecyclerView() {
        adapter = new AuthorsAdapter(bookAuthor -> {
            Bundle b = new Bundle();
            b.putInt("pageSize", viewModel.getPageSize().getValue() != null ? viewModel.getPageSize().getValue() : 10);
            b.putString("authorName", bookAuthor.getAuthor());
            findNavController(AuthorsFragment.this).navigate(R.id.action_authorsFragment_to_booksFragment, b);
        });
        binding.rvAuthors.setAdapter(adapter);
        binding.rvAuthors.setHasFixedSize(true);
    }


}
