package com.google.barberbookingapp.UI.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.databinding.ActivityEnterCodeBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


public class EnterCodeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityEnterCodeBinding binding;
    private static final String TAG = "EnterCodeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_code);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = EnterCodeActivity.this.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                String verificationId = sharedPreferences.getString("ID", " 000000");




                binding.firstPinView.setPinViewEventListener(new Pinview.PinViewEventListener() {
                    @Override
                    public void onDataEntered(Pinview pinview, boolean fromUser) {

                        String code = pinview.getValue().toString();

                        Toast.makeText(EnterCodeActivity.this, code , Toast.LENGTH_LONG).show();

                        Log.e(TAG+ "CODE" , code);

                        assert verificationId != null;
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

                             signInWithPhoneAuthCredential(credential);
                    }
                });

            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {

            if (task.isSuccessful()) {

            //    Toast.makeText(EnterCodeActivity.this, "Successful", Toast.LENGTH_LONG).show();

                startActivity(new Intent(EnterCodeActivity.this , HomeActivity.class));

                Log.e(TAG + "sha", task.toString());

            } else {

                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                //
                    //    Toast.makeText(EnterCodeActivity.this, "failed", Toast.LENGTH_LONG).show();

                    Log.e(TAG + "sh", task.toString());


                }
            }
        });


    }
}





