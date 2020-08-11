package com.google.barberbookingapp.Model.viewModels;

import android.app.Application;

import com.google.barberbookingapp.Repository.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public abstract class BaseViewModel extends AndroidViewModel {

    private static Repository repository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
    }

    public static Repository getRepository() {
        return repository;
    }
}
