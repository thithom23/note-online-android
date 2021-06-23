package com.thom.ui.home;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thom.entity.Note;

import java.util.List;
public class HomeViewModel extends ViewModel {
    private List<Note> notes;
    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("QNU");
    }


    public LiveData<String> getText() {
        return mText;
    }
}