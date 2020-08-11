package com.google.barberbookingapp.UI.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.FCM.FcmCommon;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


public class MainActivity extends AppCompatActivity {



    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;



    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);


        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR).
                withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                if (firebaseUser != null) {

                    FirebaseInstanceId.getInstance()
                            .getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {

                            FcmCommon.updateToken(getBaseContext() , instanceIdResult.getToken());
                            Log.e("shaimaa" , instanceIdResult.getToken());

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra(Common.IS_LOGIN, true);
                            startActivity(intent);
                            finish();
                        }}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra(Common.IS_LOGIN, true);
                            startActivity(intent);
                            finish();
                        }
                    });

                } else {

                } }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();




        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignInPhoneActivity.class));
            }
        });


        binding.txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra(Common.IS_LOGIN, false);
                startActivity(intent);
            }
        });
    }
}




