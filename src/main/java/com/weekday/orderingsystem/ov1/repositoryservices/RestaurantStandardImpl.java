package com.weekday.orderingsystem.ov1.repositoryservices;

import java.util.HashMap;
import java.util.PriorityQueue;

import com.weekday.orderingsystem.ov1.dto.CookingSlot;
import com.weekday.orderingsystem.ov1.dto.Order;
import com.weekday.orderingsystem.ov1.Constants.DeliveryConstants;
import com.weekday.orderingsystem.ov1.Constants.RestaurantConstants;

public class RestaurantStandardImpl implements Restaurant {

    int maxSlots;
    int restaurantTimeOffest;
    int remainingSlots;
    char currentbiggestMeal;

    public RestaurantStandardImpl(int maxSlots) {
        this.maxSlots = maxSlots;
        this.restaurantTimeOffest=0;
        this.remainingSlots=maxSlots;
        this.currentbiggestMeal=' ';
    }

    @Override
    public double calculatePreprationTime(Order order) {
        int maxTimeForOrder = 0;
        for (Character c : order.getMeals()) {
            maxTimeForOrder = Math.max(maxTimeForOrder, RestaurantConstants.MEAL_TIME_REQUIRED.get(c));
        }
        return maxTimeForOrder;
    }

    @Override
    public double calculateDeliveryTime(Order order) {
        return DeliveryConstants.TIME_PER_KM * order.getDistance();
    }

    @Override
    public double calculateFulfillmentTime(Order order) {
        return calculatePreprationTime(order) + calculateDeliveryTime(order);

    }

    private boolean canAccomodateOrder(Order order){
        int slotRequired = 0;
        for (Character c : order.getMeals()) {
            slotRequired += RestaurantConstants.MEAL_SLOT_REQUIRED.get(c);
        }
        return slotRequired<=this.maxSlots;
    }

    private int slotsRequired(Order order){
        int slotRequired = 0;
        for (Character c : order.getMeals()) {
            slotRequired += RestaurantConstants.MEAL_SLOT_REQUIRED.get(c);
        }
        return slotRequired;
    }

    @Override
    public double takeOrder(Order order) {

        if(!canAccomodateOrder(order)){
            return -1;
        }
        
        if(slotsRequired(order)<=this.remainingSlots){

        } else {
            this.restaurantTimeOffest = RestaurantConstants.MEAL_TIME_REQUIRED.get(this.currentbiggestMeal);
            this.remainingSlots = this.maxSlots;
            this.currentbiggestMeal = ' ';
        }

        double timeToCustomer = calculateFulfillmentTime(order);
        if ( timeToCustomer <= DeliveryConstants.DELIVERY_LIMIT) {
            return timeToCustomer;
        } else {
            return -1;
        }

    }

}
