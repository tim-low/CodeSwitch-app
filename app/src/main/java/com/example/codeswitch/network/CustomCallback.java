package com.example.codeswitch.network;

import com.example.codeswitch.model.BaseObject;

import java.util.List;

public interface CustomCallback<T> {
    void onResponse(T response);
}
