package com.elena.library.view.base;

import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.elena.library.R;
import com.elena.library.data.api.RequestEvent;
import com.elena.library.data.api.RequestResult;

public class BaseFragment extends Fragment {
    private void handleError() {
        Toast.makeText(requireActivity(), getString(R.string.error_general), Toast.LENGTH_SHORT).show();
    }

    protected Observer<RequestEvent<RequestResult>> createLoadingObserver(LoadCallback callback) {
        return tRequestEvent -> {
            RequestResult result = tRequestEvent.getContentIfNotHandled();
            if (result != null) {
                if (result instanceof RequestResult.Success) {
                    callback.onSuccess((RequestResult.Success) result);
                } else if (result instanceof RequestResult.Error) {
                    handleError();
                    callback.onError();
                } else if (result instanceof RequestResult.Loading) {
                    callback.onProgress();
                }
            }
        };
    }
}
