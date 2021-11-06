package com.example.tp_android.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.tp_android.data.LoginDataSource;
import com.example.tp_android.data.LoginRepository;

public class LoginViewModelFactory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
