package com.google.barberbookingapp.Model.ActivitesInterfaces;

import com.google.barberbookingapp.Model.entities.ShoppingItems;

import java.util.List;

public interface IsShoppingDataLoadListner {

    void ShoppingDataLoadSuccess(List<ShoppingItems > shoppingItemsList);
    void  ShoppingDataLoadFailed(String message);

}
