package com.google.barberbookingapp.Model.mvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.google.barberbookingapp.Repository.RoomDb.CartItem;
import com.google.barberbookingapp.Repository.RoomDb.RoomClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class CartModel implements CartPresenter{

    private CartView cartView;
    private   List<CartItem> cartItemList = new ArrayList<>();
    private  CartItem cartItem;
    private int count;
    private long sumPrice;

    public CartModel() {

    }

    public CartModel(CartView cartView) {
        this.cartView = cartView;
    }




    @SuppressLint("CheckResult")
    @Override
    public List<CartItem> getAllItemsFromCart(Context context, String userPhone) {

        Observable.defer(new Callable<ObservableSource<List<CartItem>>>() {
            @Override
            public ObservableSource<List<CartItem>> call() throws Exception {

               cartItemList  = RoomClient.getInstance(context).getAppDatabase().cartDao().getAllItemsFromCart(userPhone);
                return Observable.just(cartItemList);
            }
        }).subscribeOn(Schedulers.newThread())
                //Observers

                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<CartItem>>() {
            @Override
            public void accept(List<CartItem> cartItemList) throws Exception {
                cartView.ShowAllCartItems(cartItemList);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("Rejected" , throwable.getMessage());
            }
        });
     return cartItemList;
    }

    @SuppressLint("CheckResult")
    @Override
    public int getCountItemsInCart(String userPhone , Context context) {
        Observable.defer(new Callable<ObservableSource<Integer>>(){
            @Override
            public ObservableSource<Integer> call() throws Exception {

                count = RoomClient.getInstance(context).getAppDatabase().cartDao().getCountItemsInCart(userPhone);
                return Observable.just(count);
            }
        }).subscribeOn(Schedulers.newThread())
                //Observers

                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer count) throws Exception {

                cartView.ShowAllCartItemCount(count);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("Rejected" , throwable.getMessage());
            }
        });
        return count;
    }

    @SuppressLint("CheckResult")
    @Override
    public CartItem getProductInCart(String productId, String userPhone , Context context) {
        Observable.defer(new Callable<ObservableSource<CartItem>>() {
            @Override
            public ObservableSource<CartItem> call() throws Exception {


                 cartItem = RoomClient.getInstance(context).getAppDatabase().cartDao().getProductInCart(productId , userPhone);

                return Observable.just(cartItem);
            }
        }).subscribeOn(Schedulers.newThread())
                //Observers

                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<CartItem>() {
            @Override
            public void accept(CartItem cartItem) throws Exception {

                cartView.showProductById(cartItem); }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("Rejected" , throwable.getMessage());
            }
        });
        return cartItem;
    }

    @SuppressLint("CheckResult")
    @Override
    public void insert(Context context, CartItem cartItems) {
        Observable.fromCallable(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                RoomClient.getInstance(context).getAppDatabase().cartDao().insert(cartItems);
                return true;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.e("Accepted" , " Accepted");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("Rejected" , "Rejected");
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void update(CartItem cartItem , Context context) {
        Observable.fromCallable(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {

                RoomClient.getInstance(context).getAppDatabase().cartDao().update(cartItem);
                return true;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.e("Accepted" , " Accepted");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("Rejected" , "Rejected");
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void delete(CartItem cartItem , Context context) {
        Observable.fromCallable(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                RoomClient.getInstance(context).getAppDatabase().cartDao().delete(cartItem);
                return true;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.e("Accepted" , " Accepted");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("Rejected" , "Rejected");
            }
        });
    }


    @SuppressLint("CheckResult")
    @Override
    public void ClearCart(String userPhone , Context context) {

        Observable.fromCallable(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                RoomClient.getInstance(context).getAppDatabase().cartDao().ClearCart(userPhone);
                return true;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.e("Accepted" , " Accepted");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("Rejected" , "Rejected");
            }
        });
    }


    @SuppressLint("CheckResult")
    @Override
    public long sumPrice(String userPhone, Context context) {

        Observable.defer(new Callable<ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> call() throws Exception {


                sumPrice = RoomClient.getInstance(context).getAppDatabase().cartDao().getSumPrice(userPhone);

                return Observable.just(sumPrice);
            }
        }).subscribeOn(Schedulers.newThread())
                //Observers

                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long sumPrice) throws Exception {

                cartView.showSumPrice(sumPrice);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("Rejected" , throwable.getMessage());
            }
        });
        return sumPrice;
    }

    @Override
    public long deletePrice(String userPhone, Context context) {
        return 0;
    }
}




















