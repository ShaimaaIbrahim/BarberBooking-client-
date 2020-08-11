package com.google.barberbookingapp.Repository;

import android.app.AlertDialog;
import android.content.Context;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.barberbookingapp.Model.ActivitesInterfaces.HomeInterface;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.entities.User;
import com.google.barberbookingapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;


public final class Repository {

    private CollectionReference UserRefrence;
    private HomeInterface homeInterface;
    private static Repository repository;

    public static Repository getInstance(Context context){

        if (repository==null){

          repository  =new Repository() ;
        }
        return repository;
    }


    public void getUserRefrence(){

        UserRefrence = FirebaseFirestore.getInstance().collection("User");

    }
    public void setHomeInterface(HomeInterface homeInterface){
        this.homeInterface=homeInterface;
    }


    public void getUserInformation(FirebaseUser firebaseUser , String PhoneNumber , AlertDialog alertDialog , BottomNavigationView view){

      //  UserRefrence = FirebaseFirestore.getInstance().collection("User");

        getUserRefrence();

        DocumentReference currentUser = UserRefrence.document(PhoneNumber);

        currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    DocumentSnapshot userSnapShot = task.getResult();

                    if (!userSnapShot.exists()){

                        homeInterface.ShowUpdateDialoge(PhoneNumber );

                    }else {

                        Common.currentUser=userSnapShot.toObject(User.class);

                        view.setSelectedItemId(R.id.action_home);
                    }

                    if (homeInterface.DialogeIsShowing(alertDialog)){

                        alertDialog.dismiss();
                    }


                }
            }
        });
    }


    public void setUserInformation(String name , String phone , String address , AlertDialog alertDialog , BottomSheetDialog dialog , BottomNavigationView view){

     //   UserRefrence = FirebaseFirestore.getInstance().collection("User");

        getUserRefrence();

        User user = new User(name, phone, address);

        if (!alertDialog.isShowing()){
            alertDialog.show();
        }
        UserRefrence.document(phone).set(user).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (alertDialog.isShowing()){
                            alertDialog.dismiss();
                        }
                        Common.currentUser=user;

                        view.setSelectedItemId(R.id.action_home);

                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (alertDialog.isShowing()){
                    alertDialog.dismiss();

                }
                dialog.dismiss();
            }
        });

    }
}
