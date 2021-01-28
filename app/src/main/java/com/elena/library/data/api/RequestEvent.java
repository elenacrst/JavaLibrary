package com.elena.library.data.api;

public class RequestEvent<T> {
    private final T content;
    private boolean handled;

    public RequestEvent(T content) {
        this.content = content;
    }

    public T getContentIfNotHandled() {
        if (handled) {
            return null;
        } else {
            handled = true;
            return content;
        }
    }
}
