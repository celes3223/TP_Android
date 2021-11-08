package com.example.tp_android.ui.fisio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FisioViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FisioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is reserva fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
