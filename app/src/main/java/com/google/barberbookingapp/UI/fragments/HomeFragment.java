package com.google.barberbookingapp.UI.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import ss.com.bannerslider.Slider;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.barberbookingapp.Model.ActivitesInterfaces.AllUserBookingInfo;
import com.google.barberbookingapp.Model.ActivitesInterfaces.IBabberLoadLisstner;
import com.google.barberbookingapp.Model.ActivitesInterfaces.IBookingInformationChangeListner;
import com.google.barberbookingapp.Model.ActivitesInterfaces.ILookBookLoadListner;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.entities.Banner;
import com.google.barberbookingapp.Model.entities.BookingInformation;
import com.google.barberbookingapp.Model.mvp.CartModel;
import com.google.barberbookingapp.Model.mvp.CartPresenter;
import com.google.barberbookingapp.Model.service.PicassoLoadingImageService;
import com.google.barberbookingapp.Model.viewModels.HomeFragmentViewModel;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.UI.activities.BookingActivity;
import com.google.barberbookingapp.UI.activities.CartActivity;
import com.google.barberbookingapp.UI.activities.HistoryActivity;
import com.google.barberbookingapp.UI.adapters.HomeSliderAdapter;
import com.google.barberbookingapp.UI.adapters.LookBookAdapter;
import com.google.barberbookingapp.databinding.FragmentHomeBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements IBabberLoadLisstner, ILookBookLoadListner, AllUserBookingInfo, IBookingInformationChangeListner {

    private AllUserBookingInfo bookingInfo;

    private FragmentHomeBinding binding;
    private LinearLayout layout_user_information;
    private HomeFragmentViewModel viewModel;
    private AlertDialog alertDialog;
    private IBookingInformationChangeListner iBookingInformationChangeListner;
    private CartPresenter cartPresenter;

    private ListenerRegistration userBookingListner = null;
    private EventListener<QuerySnapshot> useBookingEvent = null;


    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingInfo = this;

        alertDialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        iBookingInformationChangeListner = this;
        if (Common.currentUser.getPhoneNumber() != null) {
            loadUserBooking();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);


        cartPresenter = new CartModel();

        // To initialize banner with picassoService for loading images
        Slider.init(new PicassoLoadingImageService());

        viewModel.setIBannerInterface(this);
        viewModel.setILookBookInterface(this);


        //check if logged
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
//            setUserInformation();
            loadBanner();
            loadLookBook();
            loadUserBooking();
            initRealTimeUserBooking();
            countCartItem();

        }

        binding.historyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(getActivity() , HistoryActivity.class));
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBookingFromBarber(false);
            }
        });

        //TODO...
        binding.cartCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CartActivity.class));
            }
        });
        binding.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBookingFromUser();
            }
        });
        binding.bookingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), BookingActivity.class));
            }
        });

        return binding.getRoot();
    }

    private void initRealTimeUserBooking() {

        useBookingEvent = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // todo load userBooking again
                loadUserBooking();
            }
        };


    }

    private void countCartItem() {

        int count = cartPresenter.getCountItemsInCart(Common.currentUser.getPhoneNumber(), getContext());

        binding.notificationBadge.setText(String.valueOf(count));
    }

    private void changeBookingFromUser() {
        AlertDialog.Builder ConfirmDialoge = new AlertDialog.Builder(getActivity()).setCancelable(false).setTitle("Hey!").setMessage("Do you really to change booking ? \n because we will delete your old booking information \n just confirm").setNegativeButton("CANCEL ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteBookingFromBarber(true);
            }
        });
        ConfirmDialoge.show();
    }

    private void deleteBookingFromBarber(boolean isChange) {
        alertDialog.show();
        /*
        to delete booking information , we need delete it from barber collection then delete
        from user collection last delete from event calendar
         */
        // we need call Common.currentBooking because we need some data from Booking information
        if (Common.currentBooking != null) {

            if (Common.city != null && !Common.city.isEmpty()) {

                DocumentReference barberBookingInfo = FirebaseFirestore.getInstance().collection("AllSalon").document(Common.city).collection("Branch").document(Common.currentBooking.getSalonId()).collection("Barber").document(Common.currentBooking.getBarberId()).collection(Common.convertTimestampToStringKey(Common.currentBooking.getTimestamp())).document(String.valueOf(Common.currentBooking.getSlot()));

                // when we had refrence decument we can delete it
                barberBookingInfo.delete().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // after we delete from barber we need to delete from user

                        deleteBookingFromUser(isChange);
                    }
                });

            } else {
                Toast.makeText(getActivity(), "Booking infrmation Must not be null", Toast.LENGTH_LONG).show();
                if (alertDialog.isShowing())  alertDialog.dismiss();
            }
        }
    }

    private void deleteBookingFromUser(boolean isChange) {

        if (!TextUtils.isEmpty(Common.currentBookingId)) {


        } else {

            Toast.makeText(getActivity(), "Booking information Id  Must not be null", Toast.LENGTH_LONG).show();
            if (alertDialog.isShowing() ) alertDialog.dismiss();

        }
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("User").document(Common.currentUser.getPhoneNumber()).collection("Booking").document(Common.currentBookingId);

        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //after deletig from user we need to delete from calendar
                // firse we need to read saved calendar uri

                Paper.init(getActivity());

                if (Paper.book().read(Common.EVENT_URI_CASHE.toString()) != null) {

                    String event = Paper.book().read(Common.EVENT_URI_CASHE.toString());

                    Uri eventUri = null;

                    if (event != null && TextUtils.isEmpty(event)) eventUri = Uri.parse(event);

                    if (eventUri != null)
                        getActivity().getContentResolver().delete(eventUri, null, null);
                }

                Toast.makeText(getActivity(), "Success Delete Booking !", Toast.LENGTH_LONG).show();


                //refresh
                loadUserBooking();

                if (isChange) iBookingInformationChangeListner.OnBookingInformationChange();

                if (alertDialog.isShowing()) alertDialog.dismiss();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void loadLookBook() {
        viewModel.loadLookBook();
    }

    private void loadBanner() {
        viewModel.loadBanner();
    }

    private void setUserInformation() {

        binding.layoutUserInformation.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(Common.currentUser.getName()) && Common.currentUser != null) {
            binding.txtUserName.setText(Common.currentUser.getName());

        }

    }


    @Override
    public void onBannerLoadSucceded(List<Banner> bannerList) {

        binding.bannerSlider.setAdapter(new HomeSliderAdapter(bannerList));
    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLookBookLoadSucceded(List<Banner> bannerList) {
        binding.recyclerLookBook.setHasFixedSize(true);
        binding.recyclerLookBook.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerLookBook.setAdapter(new LookBookAdapter(bannerList, getActivity()));
    }

    @Override
    public void onLookBookLoadFailed(String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserBooking();
        countCartItem();
    }

    private void loadUserBooking() {

        CollectionReference userBooking = FirebaseFirestore.getInstance().collection("User").document(Common.currentUser.getPhoneNumber()).collection("Booking");

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.HOUR, 0);
        calendar.add(Calendar.DATE, 0);

        Timestamp toDaytimestamp = new Timestamp(calendar.getTime());

        userBooking.
                whereGreaterThanOrEqualTo("timestamp", toDaytimestamp).whereEqualTo("done", false).
                limit(1).
                // select only one
                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    if (!task.getResult().isEmpty()) {

                        for (QueryDocumentSnapshot snapshot : task.getResult()) {


                            BookingInformation bookingInformation = snapshot.toObject(BookingInformation.class);
                            bookingInfo.onBookingInfoLoadSuccess(bookingInformation, snapshot.getId());

                            break;  // stop loop as soon as

                        }
                    } else {
                        bookingInfo.onBookingInfoLoadEmpty();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bookingInfo.onBookingInfoLoadFailed(e.getMessage());
            }
        });
        // here we will listne to userBookingInd=formation

        if (useBookingEvent != null) {

            if (userBookingListner == null) {
                userBookingListner = userBooking.addSnapshotListener(useBookingEvent);

            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (userBookingListner != null) userBookingListner.remove();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (userBookingListner != null) userBookingListner.remove();
    }

    @Override
    public void onBookingInfoLoadSuccess(BookingInformation information, String documentId) {

        Common.currentBooking = information;
        Common.currentBookingId = documentId;

        binding.cardBookingInfo.setVisibility(View.VISIBLE);
        binding.txtSalonAddress.setText(information.getSalonAddress());
        binding.txtStylistName.setText(information.getBarberName());
        binding.txtTime.setText(information.getTime());

        //diffrence time
        String dateRemain = DateUtils.getRelativeTimeSpanString((information.getTimestamp().toDate().getTime()), Calendar.getInstance().getTimeInMillis(), 0).toString();

        binding.txtTimeRemain.setText(dateRemain);

        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

    }

    @Override
    public void onBookingInfoLoadEmpty() {
        binding.cardBookingInfo.setVisibility(View.GONE);
    }

    @Override
    public void onBookingInfoLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnBookingInformationChange() {
        // we .just start BookingActivity
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }
}
