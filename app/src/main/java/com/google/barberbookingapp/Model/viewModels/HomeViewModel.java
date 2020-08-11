package com.google.barberbookingapp.Model.viewModels;

import android.app.AlertDialog;
import android.app.Application;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.barberbookingapp.Model.ActivitesInterfaces.HomeInterface;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;


public class HomeViewModel  extends BaseViewModel {

    private HomeInterface homeInterface;

    public HomeViewModel(@NonNull Application application) {

        super(application);

    }

   public void setHomeInterface(HomeInterface homeInterface){
   this.homeInterface=homeInterface;
          }

    public void GetUserInformation(FirebaseUser firebaseUser , String PhoneNumber , AlertDialog alertDialog , BottomNavigationView view) {

          getRepository().getUserInformation(firebaseUser, PhoneNumber , alertDialog , view);
    }

    public void SetUserInformation (String name , String phone , String address , AlertDialog alertDialog , BottomSheetDialog dialog , BottomNavigationView view) {
        getRepository().setUserInformation(name , phone , address , alertDialog , dialog, view);
    }

}
