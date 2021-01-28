package com.elena.library.view.base;

import com.elena.library.data.api.RequestResult;

public interface LoadCallback {
    void onProgress();

    void onSuccess(RequestResult.Success result);

    void onError();
}
