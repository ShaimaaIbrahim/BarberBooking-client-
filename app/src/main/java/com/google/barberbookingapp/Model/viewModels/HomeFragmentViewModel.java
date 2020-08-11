package com.google.barberbookingapp.Model.viewModels;

import android.app.Application;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.barberbookingapp.Model.ActivitesInterfaces.IBabberLoadLisstner;
import com.google.barberbookingapp.Model.ActivitesInterfaces.ILookBookLoadListner;
import com.google.barberbookingapp.Model.entities.Banner;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class HomeFragmentViewModel extends BaseViewModel {

    private CollectionReference bannerRef, lookBookRef;
    private IBabberLoadLisstner iBabberLoadLisstner;
    private ILookBookLoadListner iLookBookLoadListner;

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);

        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
        lookBookRef=FirebaseFirestore.getInstance().collection("LookBook");
    }

    public  void setIBannerInterface(IBabberLoadLisstner iBabberLoadLisstner){
         this.iBabberLoadLisstner=iBabberLoadLisstner;
    }

    public  void setILookBookInterface(ILookBookLoadListner iLookBookLoadListner){
         this.iLookBookLoadListner=iLookBookLoadListner;
    }

    public void loadBanner(){

        bannerRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult()!=null){

                    List<Banner > bannerList = new ArrayList<>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot snapshot : task.getResult()){
                            Banner banner = snapshot.toObject(Banner.class);
                            bannerList.add(banner);
                        }
                        iBabberLoadLisstner.onBannerLoadSucceded(bannerList);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBabberLoadLisstner.onBannerLoadFailed(e.getMessage().toString());

            }
        });
    }

    public void loadLookBook() {
        lookBookRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult()!=null){
                    List<Banner>bannersList = new ArrayList<>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot snapshot : task.getResult()){
                            Banner banner = snapshot.toObject(Banner.class);
                            bannersList.add(banner);
                        }
                        iLookBookLoadListner.onLookBookLoadSucceded(bannersList);
                    } } }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iLookBookLoadListner.onLookBookLoadFailed(e.getMessage().toString());

            }
        });
    }


}
