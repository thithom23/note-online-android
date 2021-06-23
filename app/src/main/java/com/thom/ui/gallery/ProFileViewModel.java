package com.thom.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProFileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProFileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("QNU");
    }

    public LiveData<String> getText() {
        return mText;
    }
}