package com.google.barberbookingapp.UI.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.databinding.ActivitySignInPhoneBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;



public class SignInPhoneActivity extends AppCompatActivity {

    private ActivitySignInPhoneBinding binding;
    private static final String TAG = "SignInPhoneActivity";
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks  mCallbacks;

    private static void onClick(View v) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in_phone);
        mAuth = FirebaseAuth.getInstance();

        binding.signUp.setOnClickListener(v -> {
            sendVerification();
        });


        binding.skip.setOnClickListener(SignInPhoneActivity::onClick);
    }

    private void sendVerification() {

        String phoneNumber = Objects.requireNonNull(binding.editPhone.getText()).toString();

        if (phoneNumber.isEmpty() || phoneNumber == null) {

            binding.editPhone.setError("Enter Phone Number");
            binding.editPhone.requestFocus();

            return;
        }
        if (phoneNumber.length() < 10) {
            binding.editPhone.setError("Enter Valid  Phone Number");
            binding.editPhone.requestFocus();
            return;
        }

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                try {

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                super.onCodeSent(s, forceResendingToken);

                Log.e(TAG + "s" , s);

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("ID", s);
                myEdit.apply();

              startActivity(new Intent(SignInPhoneActivity.this , EnterCodeActivity.class));

            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
               this,               // Activity (for callback binding)
                mCallbacks);


    }

    }

