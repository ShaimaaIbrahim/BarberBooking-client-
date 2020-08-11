package com.google.barberbookingapp.UI.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.barberbookingapp.Model.ActivitesInterfaces.HomeInterface;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.viewModels.HomeViewModel;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.Repository.Repository;
import com.google.barberbookingapp.UI.fragments.HomeFragment;
import com.google.barberbookingapp.UI.fragments.ShoppingFragment;
import com.google.barberbookingapp.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity  implements HomeInterface {

    private ActivityHomeBinding binding;
    private BottomSheetDialog dialog;
    private FirebaseUser user;
    private AlertDialog alertDialog;
    private HomeViewModel viewModel;
    private FirebaseUser firebaseUser;
    private Repository repository;
    private View sheetView;

    @Override
    protected void onResume() {
        super.onResume();
        checkRatingDDialog();
    }

    private void checkRatingDDialog() {
        Paper.init(this);
      String dataSerialized=  Paper.book().read(Common.RATING_INFORMATION_KEY , "");

      if (!TextUtils.isEmpty(dataSerialized)){
          Map<String , String > dataRecieved = new Gson().fromJson(dataSerialized , new TypeToken<Map<String , String>>(){}.getType());

          if (dataRecieved!=null){

              Common.showRatingDialog(HomeActivity.this , dataRecieved.get(Common.RATING_STATE_KEY) ,
                      dataRecieved.get(Common.RATING_SALON_ID) , dataRecieved.get(Common.RATING_SALON_NAME)
                      ,dataRecieved.get(Common.RATING_BARBER_ID));
          }
      }
    }

    @SuppressLint({"InflateParams", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        user = FirebaseAuth.getInstance().getCurrentUser();

        alertDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        repository = Repository.getInstance(this);


alertDialog.show();

        viewModel.setHomeInterface(this);
        repository.setHomeInterface(this);

        if (firebaseUser!=null) {


            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {

                    Log.e("shaimaa" , instanceIdResult.getToken());
                }
            });

        }


      if (getIntent()!=null){

       boolean aLogIn= getIntent().getBooleanExtra(Common.IS_LOGIN , false);

       if (aLogIn){


      if (user!=null){

           Paper.init(this);
           Paper.book().write(Common.LOGGED_KEY , user.getPhoneNumber().toString());

           viewModel.GetUserInformation(firebaseUser , user.getPhoneNumber() , alertDialog , binding.bottomNavigator);

           checkRatingDDialog();

       }

        }}

        binding.bottomNavigator.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           androidx.fragment.app.Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
              if (menuItem.getItemId()==R.id.action_home){
                  fragment= new HomeFragment();
              }else if (menuItem.getItemId()==R.id.shopping){
                  fragment = new ShoppingFragment();
              }
              return  loadFragment(fragment);
            }

        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean DialogeIsShowing(AlertDialog alertDialog) {
        if (alertDialog.isShowing()){
            return true;
        }else {
            return false;
        }
    }

    @SuppressLint("CutPasteId")
    @Override
    public void ShowUpdateDialoge(String phoneNumber) {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();

        }
        dialog = new BottomSheetDialog(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.setTitle("one More Step");

        sheetView = getLayoutInflater().inflate(R.layout.layout_update_information , null);



        AppCompatButton button=sheetView.findViewById(R.id.update_user);

        String name= ((EditText) sheetView.findViewById(R.id.edit_name)).getText().toString();
        String address= ((EditText) sheetView.findViewById(R.id.edit_address)).getText().toString();


       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Toast.makeText(HomeActivity.this, name + "==" + address, Toast.LENGTH_LONG).show();

               viewModel.SetUserInformation(name, phoneNumber, address, alertDialog, dialog , binding.bottomNavigator);

               if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address)) {
                   dialog.dismiss();
               } else {
                   if (!dialog.isShowing()) {
                       dialog.show();
                   }
               }
           }
       });

        dialog.setContentView(sheetView);

        dialog.show();
    }
}


