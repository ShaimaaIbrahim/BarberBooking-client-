package com.google.barberbookingapp.UI.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.EventBus.confirmBookingEvent;
import com.google.barberbookingapp.Model.EventBus.displayTimeSlotEvent;
import com.google.barberbookingapp.Model.FCM.FcmCommon;
import com.google.barberbookingapp.Model.FCM.FcmResponce;
import com.google.barberbookingapp.Model.FCM.FcmSendData;
import com.google.barberbookingapp.Model.FCM.MyToken;
import com.google.barberbookingapp.Model.entities.BookingInformation;
import com.google.barberbookingapp.Model.entities.Notification;
import com.google.barberbookingapp.Model.mvp.CartModel;
import com.google.barberbookingapp.Model.mvp.CartPresenter;
import com.google.barberbookingapp.Model.mvp.CartView;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.Repository.RetrofitFcm.IFCMApi;
import com.google.barberbookingapp.Repository.RetrofitFcm.RetrofitClient;
import com.google.barberbookingapp.Repository.RoomDb.CartItem;
import com.google.barberbookingapp.databinding.FragmentBookingStepFourBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;
/*

                String startTime  = Common.ConvertTimeSlotsToString(Common.currentTimeSlot);

                String [] convertTime = startTime.split("_");
                String  [] startTimeConvert = convertTime[0].split(":");

                int startHorInt = Integer.parseInt(startTimeConvert[0].trim());
                int startMinuteInt = Integer.parseInt(startTimeConvert[1].trim());

                Calendar calendarDateWithOurHouse = Calendar.getInstance();

                calendarDateWithOurHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());

                calendarDateWithOurHouse.set(Calendar.HOUR_OF_DAY , startHorInt);
                calendarDateWithOurHouse.set(Calendar.MINUTE , startMinuteInt);

                //...............
                Timestamp timestamp = new Timestamp(calendarDateWithOurHouse.getTime());

                BookingInformation information = new BookingInformation();

                information.setDone(false); // always false because always use it to filter users

                information.setTimestamp(timestamp);

                information.setCityBook(Common.city);

            //    Toast.makeText(getActivity() , Common.city , Toast.LENGTH_LONG).show();

                information.setBarberId(Common.currentBarber.getBarberId());
                information.setBarberName(Common.currentBarber.getName());
                information.setCustomerName(Common.currentUser.getName());
                information.setCustomerPhone(Common.currentUser.getPhoneNumber());
                information.setSalonAddress(Common.currentSalon.getAddress());
                information.setSalonId(Common.currentSalon.getSalonId());
                information.setCustomerName(Common.currentSalon.getName());
                information.setSalonName(Common.currentSalon.getName());




                // format date
                information.setTime(new StringBuilder(Common.ConvertTimeSlotsToString(Common.currentTimeSlot)).
                        append("at")
                        .append(simpleDateFormat.format(calendarDateWithOurHouse.getTime())).toString());

                information.setSlot(Common.currentTimeSlot);


                //
                DocumentReference bookingDate = FirebaseFirestore.getInstance().collection("AllSalon")
                        .document(Common.city).collection("Branch").document(Common.currentSalon.getSalonId())
                        .collection("Barber").document(Common.currentBarber.getBarberId()).
                                collection(Common.simpleFormatDate.format(Common.bookingDate.getTime()))
                        .document(String.valueOf(Common.currentTimeSlot));

                bookingDate.set(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        addToUserBooking(information);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext() , e.getMessage() ,Toast.LENGTH_LONG).show();

                    }
                });
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingStep4Fragment extends Fragment implements CartView {

    private CompositeDisposable disposable = new CompositeDisposable();
    private SimpleDateFormat simpleDateFormat;
    private static BookingStep4Fragment instance;
    private FragmentBookingStepFourBinding binding;
 //   private LocalBroadcastManager localBroadcastManager;
    private AlertDialog alertDialog;
    private IFCMApi ifcmApi;
    private CartPresenter cartPresenter;

    private BroadcastReceiver cofimBookingReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

             binding.confirm.setEnabled(true);
             setData();
        }
    };
    //==============================Event_Bus===============

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Subscribe(sticky = true , threadMode = ThreadMode.MAIN)
    public void setDataBooking(confirmBookingEvent event){

        if (event.isConfirm){
            setData();

        }

    }
    //=====================

    private void setData() {

        binding.txtBarberName.setText(Common.currentBarber.getName());
        binding.txtSalonLocation.setText(Common.currentSalon.getAddress());
        binding.txtSalonPhone.setText(Common.currentUser.getPhoneNumber());
        binding.txtSalonName.setText(Common.currentSalon.getName());

       binding.txtBarberBooking.setText(new StringBuilder(Common.ConvertTimeSlotsToString(Common.currentTimeSlot)).append("at")
                .append(simpleDateFormat.format(Common.bookingDate.getTime())).toString());


        binding.txtSalonWebsite.setText(Common.currentSalon.getWebsite());
        binding.txtSalonTime.setText(Common.currentSalon.getOpenHour());


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //localBroadcastManager.unregisterReceiver(cofimBookingReciever);
        disposable.clear();
    }

    public BookingStep4Fragment() {
        // Required empty public constructor
    }



    public static BookingStep4Fragment getInstance(){
        if (instance==null){
            instance=new BookingStep4Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //format date which displayed at

        alertDialog = new SpotsDialog.Builder().setContext(getContext() ).setCancelable(false).build();
        simpleDateFormat= new SimpleDateFormat("dd/MM/yyy");

        ifcmApi= RetrofitClient.getInstance().create(IFCMApi.class);

        cartPresenter = new CartModel(this);

       /* localBroadcastManager= LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(cofimBookingReciever , new IntentFilter(Common.KEY_CONFIRM_BOOKING));*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingStepFourBinding.bind(inflater.inflate(R.layout.fragment_booking_step_four, container, false));



        binding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.show();
                 String userPhone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString();
                cartPresenter.getAllItemsFromCart(getContext() , userPhone);

            }
        });

   return binding.getRoot();
    }



    private void addToUserBooking(BookingInformation information) {


        if (!alertDialog.isShowing()){
            alertDialog.show();
        }
        CollectionReference userBooking= FirebaseFirestore.getInstance().collection("User").
                document(Common.currentUser.getPhoneNumber()).collection("Booking");

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.HOUR_OF_DAY , 0);
        calendar.add(Calendar.HOUR , 0);
        calendar.add(Calendar.DATE , 0);

        Timestamp toDaytimestamp = new Timestamp(calendar.getTime());


        //check if this userBookingDocument is exist

        //when you search for specific query if document has this query
        userBooking
                .whereGreaterThanOrEqualTo("timestamp" , toDaytimestamp)
                .whereEqualTo("done" , false).
               limit(1)
           .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                          if (task.getResult().isEmpty()){

                              userBooking.document().set(information)

                                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @RequiresApi(api = Build.VERSION_CODES.O)
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {


                                            Notification notification = new Notification();
                                              notification.setUid(UUID.randomUUID().toString());
                                              notification.setTitle("New Booking !");
                                              notification.setContent("You have a new appoiment for customer hair care with " + Common.currentUser.getName());
                                              notification.setRead(false);   // we only filter notification with read equal false

                                              //this code returns server timestamp
                                              notification.setServerTimestamp(FieldValue.serverTimestamp());

                                              FirebaseFirestore.getInstance().collection("AllSalon").document(Common.city)
                                                      .collection("Branch").document(Common.currentSalon.getSalonId())
                                                      .collection("Barber").document(Common.currentBarber.getBarberId())
                                                      .collection("Notification").document(notification.getUid())

                                                      .set(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                  @Override
                                                  public void onSuccess(Void aVoid) {

                                                     FirebaseFirestore.getInstance().collection("Tokens")
                                                             .whereEqualTo("userPhone" , Common.currentBarber.getUsername())
                                                             .limit(1)
                                                             .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                         @SuppressLint("CheckResult")
                                                         @Override
                                                         public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                             if (task.isSuccessful() && task.getResult().size() > 0){

                                                                 for (QueryDocumentSnapshot snapshot : task.getResult()){

                                                                     MyToken myToken1 = snapshot.toObject(MyToken.class);

                                                                     FcmSendData fcmSendData = new FcmSendData();
                                                                     Map<String , String> data = new HashMap<>();

                                                                     data.put(FcmCommon.TITLE_KEY , "New Booking !");
                                                                     data.put(FcmCommon.CONTENT_KEY , "You have new booking from user : " + Common.currentUser.getName());

                                                                     fcmSendData.setData(data);
                                                                     fcmSendData.setTo(myToken1.getToken());

                                                                     disposable.add(
                                                                            ifcmApi.sendNotification(fcmSendData)
                                                                                    .subscribeOn(Schedulers.io())
                                                                                    .observeOn(AndroidSchedulers.mainThread())
                                                                                    .subscribe(new Consumer<FcmResponce>() {
                                                                                        @Override
                                                                                        public void accept(FcmResponce fcmResponce) throws Exception {

                                                                                            if (alertDialog.isShowing())
                                                                                                alertDialog.dismiss();

                                                                                            addToCalendar(Common.bookingDate, Common.ConvertTimeSlotsToString(Common.currentTimeSlot));

                                                                                            resetDate();
                                                                                            getActivity().finish();

                                                                                            Toast.makeText(getContext(), "Notification success", Toast.LENGTH_LONG).show();

                                                                                        }
                                                                                    }, new Consumer<Throwable>() {
                                                                                        @Override
                                                                                        public void accept(Throwable throwable) throws Exception {
                                                                                            Log.e("shaimaa" , throwable.getMessage());

                                                                                            if (alertDialog.isShowing())
                                                                                                alertDialog.dismiss();

                                                                                            addToCalendar(Common.bookingDate, Common.ConvertTimeSlotsToString(Common.currentTimeSlot));
                                                                                            resetDate();
                                                                                            getActivity().finish();
                                                                                            Toast.makeText(getContext(), " Notification failed", Toast.LENGTH_LONG).show();

                                                                                        }
                                                                                    }));
                                                                 }
                                                             }else {
                                                                 alertDialog.dismiss();
                                                                 Toast.makeText(getContext(), " Not", Toast.LENGTH_LONG).show();

                                                             }

                                                         }
                                                     });
                                                  }
                                              });
                                          }
                                      }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Toast.makeText(getContext() , e.getMessage() ,Toast.LENGTH_LONG).show();

                                      if (alertDialog.isShowing())  alertDialog.dismiss();
                                  }}); }
                          else {

                                      if (alertDialog.isShowing())
                                      alertDialog.dismiss();

                              resetDate();
                              getActivity().finish();
                              Toast.makeText(getContext() , "there is another appointment " ,Toast.LENGTH_LONG).show();
                          } }
        });


    }


    //all information we need to add to calendar

    private void addToCalendar(Calendar bookingDate, String startDate) {

        String startTime  = Common.ConvertTimeSlotsToString(Common.currentTimeSlot);
        String [] convertTime = startTime.split("_");


        // for example start time to end time

        //09:00 - 09:10
        //09
        String  [] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
        int startMinuteInt = Integer.parseInt(startTimeConvert[1].trim());
        //10
        String  [] endTimeConvert = convertTime[0].split(":");
        int endHourInt = Integer.parseInt(startTimeConvert[0].trim());
        int endMinuteInt = Integer.parseInt(startTimeConvert[1].trim());

        //calendar for start time
        Calendar startEvent = Calendar.getInstance();
        startEvent.setTimeInMillis(startEvent.getTimeInMillis());
        startEvent.set(Calendar.HOUR_OF_DAY , startHourInt);
        startEvent.set(Calendar.MINUTE , startMinuteInt);

        //calendar for end time
        Calendar endEvent = Calendar.getInstance();
        endEvent.setTimeInMillis(endEvent.getTimeInMillis());
        endEvent.set(Calendar.HOUR_OF_DAY , endHourInt);
        endEvent.set(Calendar.MINUTE , endMinuteInt);

        //simple date format
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm");
        String startEventTime= format.format(startEvent.getTime());
        String endEventTime = format.format(endEvent.getTime());

        // add booking information to calendar of the user
       addToCalendarDevice(startEventTime , endEventTime , "HairCut Booking"
       , new StringBuilder("HairCut from")
        .append(startTime)
        .append(" with ")
        .append(Common.currentBarber.getName())
        .append(" at ")
        .append(Common.currentSalon.getName()).toString()
               , new StringBuilder(" Address : ")
               .append(Common.currentSalon.getAddress()).toString()
       );
    }


    private void addToCalendarDevice(String startEventTime, String endEventTime, String title, String description, String location) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm");
            try {
                Date startDate= format.parse(startEventTime);
                Date endDate =format.parse(endEventTime);

                ContentValues contentValues = new ContentValues();

                //put
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    contentValues.put(CalendarContract.Events.CALENDAR_ID , getCalendar(getContext()));
                }else {
                }

                contentValues.put(CalendarContract.Events.TITLE , title);
                contentValues.put(CalendarContract.Events.DESCRIPTION , description);
                contentValues.put(CalendarContract.Events.EVENT_LOCATION , location);

                //time
                contentValues.put(CalendarContract.Events.DTSTART, startDate.getTime());
                contentValues.put(CalendarContract.Events.DTEND , endDate.getTime());
                contentValues.put(CalendarContract.Events.ALL_DAY, 0);
                contentValues.put(CalendarContract.Events.HAS_ALARM , 1);

                //timeZone
                String timeZone = TimeZone.getDefault().getID();
                contentValues.put(CalendarContract.Events.EVENT_TIMEZONE , timeZone);

                Uri calendars;
                if(Build.VERSION.SDK_INT >=8) {
                    calendars = Uri.parse("content://com.android.calendar/events");
             }else {
                    calendars = Uri.parse("content://calendar/events");
          }

                Uri save_uri=  getActivity().getContentResolver().insert(calendars , contentValues);

                getActivity().getContentResolver().insert(calendars , contentValues);

                Paper.init(getActivity());
                Paper.book().write(Common.EVENT_URI_CASHE , save_uri.toString());



            } catch (ParseException e) {
                e.printStackTrace();
            }
    }

    //to get calendarId

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCalendar(Context context) {
        // Get Default calendar Id of calendar of Gmail
        String GmailIdCalendar = "";
        String projection[]={
                "_id" ,"calendar_displayName"
        };
        Uri calendars;

            calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = context.getContentResolver();
        //select all calendars

            Cursor managedCursor = contentResolver.query(calendars , projection , null , null);

            if (managedCursor.moveToFirst()){
         String calName ;

         int nameCol= managedCursor.getColumnIndex(projection[1]);

         int IdColumn= managedCursor.getColumnIndex(projection[0]);

          do {
           calName= managedCursor.getString(nameCol);

         if (calName.contains("@gmail.com")){

               GmailIdCalendar= managedCursor.getString(IdColumn);

               Log.e(TAG + "shaimaa" , GmailIdCalendar);

               break;
        }
          }while (managedCursor.moveToNext());

            managedCursor.close();}

return GmailIdCalendar;
    }


    private void resetDate() {
    Common.step=0;
    Common.bookingDate =null;
    Common.currentTimeSlot = -1;
    Common.currentBarber=null;
    Common.currentSalon=null;

    }


    @Override
    public void ShowAllCartItems(List<CartItem> cartItemList) {
        // we get all cart items

        String startTime  = Common.ConvertTimeSlotsToString(Common.currentTimeSlot);

        String [] convertTime = startTime.split("_");
        String  [] startTimeConvert = convertTime[0].split(":");

        int startHorInt = Integer.parseInt(startTimeConvert[0].trim());
        int startMinuteInt = Integer.parseInt(startTimeConvert[1].trim());

        Calendar calendarDateWithOurHouse = Calendar.getInstance();

        calendarDateWithOurHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());

        calendarDateWithOurHouse.set(Calendar.HOUR_OF_DAY , startHorInt);
        calendarDateWithOurHouse.set(Calendar.MINUTE , startMinuteInt);

        //...............
        Timestamp timestamp = new Timestamp(calendarDateWithOurHouse.getTime());

        BookingInformation information = new BookingInformation();

        information.setDone(false); // always false because always use it to filter users

        information.setTimestamp(timestamp);

        information.setCityBook(Common.city);

        //    Toast.makeText(getActivity() , Common.city , Toast.LENGTH_LONG).show();

        information.setBarberId(Common.currentBarber.getBarberId());
        information.setBarberName(Common.currentBarber.getName());
        information.setCustomerName(Common.currentUser.getName());
        information.setCustomerPhone(Common.currentUser.getPhoneNumber());
        information.setSalonAddress(Common.currentSalon.getAddress());
        information.setSalonId(Common.currentSalon.getSalonId());
        information.setCustomerName(Common.currentSalon.getName());
        information.setSalonName(Common.currentSalon.getName());
        information.setCartItemList(cartItemList);




        // format date
        information.setTime(new StringBuilder(Common.ConvertTimeSlotsToString(Common.currentTimeSlot)).
                append("at")
                .append(simpleDateFormat.format(calendarDateWithOurHouse.getTime())).toString());

        information.setSlot(Common.currentTimeSlot);

        DocumentReference bookingDate = FirebaseFirestore.getInstance().collection("AllSalon")
                .document(Common.city).collection("Branch").document(Common.currentSalon.getSalonId())
                .collection("Barber").document(Common.currentBarber.getBarberId()).
                        collection(Common.simpleFormatDate.format(Common.bookingDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));


        bookingDate.set(information).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                addToUserBooking(information);

              //  cartPresenter.ClearCart(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString() , getContext());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext() , e.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void ShowAllCartItemCount(int count) {

    }

    @Override
    public void showProductById(CartItem cartItem) {

    }

    @Override
    public void showSumPrice(long sumPrice) {

    }
}
