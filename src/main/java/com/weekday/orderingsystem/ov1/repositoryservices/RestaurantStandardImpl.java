package com.weekday.orderingsystem.ov1.repositoryservices;

import java.util.HashMap;
import java.util.PriorityQueue;

import com.weekday.orderingsystem.ov1.dto.CookingSlot;
import com.weekday.orderingsystem.ov1.dto.Order;
import com.weekday.orderingsystem.ov1.Constants.DeliveryConstants;
import com.weekday.orderingsystem.ov1.Constants.RestaurantConstants;

public class RestaurantStandardImpl implements Restaurant {

    int maxSlots;
    double restaurantTimeOffest;
    int remainingSlots;
    char currentbiggestMeal;
    PriorityQueue<CookingSlot> existingOrders;

    public RestaurantStandardImpl(int maxSlots) {
        this.maxSlots = maxSlots;
        this.restaurantTimeOffest = 0;
        this.remainingSlots = maxSlots;
        this.currentbiggestMeal = ' ';
        this.existingOrders = new PriorityQueue<>(
            (x, y) -> Double.compare(x.getOrderCompletionTime(), y.getOrderCompletionTime()));
   
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


    private int slotsRequired(Order order) {
        int slotRequired = 0;
        for (Character c : order.getMeals()) {
            slotRequired += RestaurantConstants.MEAL_SLOT_REQUIRED.get(c);
        }
        return slotRequired;
    }

    @Override
    public double takeOrder(Order order) {
        int slotsRequired = slotsRequired(order);

        if (slotsRequired > this.maxSlots) {
            return -1;
        }

        if (slotsRequired <= this.remainingSlots) {
            
            // for (Character c : order.getMeals()) {
            //     this.remainingSlots -= RestaurantConstants.MEAL_SLOT_REQUIRED.get(c);
                
            //     if (RestaurantConstants.MEAL_TIME_REQUIRED
            //             .getOrDefault(this.currentbiggestMeal,0) < RestaurantConstants.MEAL_TIME_REQUIRED.get(c)) {
            //         this.currentbiggestMeal = c;
            //     }
            // }

        } else {
            while(this.remainingSlots<slotsRequired){
                CookingSlot lastFastestOrder = existingOrders.remove();
                remainingSlots+= lastFastestOrder.getSlotsUsed();
                this.restaurantTimeOffest = lastFastestOrder.getOrderCompletionTime();
            }

            // this.restaurantTimeOffest = RestaurantConstants.MEAL_TIME_REQUIRED.get(this.currentbiggestMeal);
            // this.remainingSlots = this.maxSlots;
            // this.currentbiggestMeal = ' ';
        }
        double timeToCustomer = calculateFulfillmentTime(order) + this.restaurantTimeOffest;
        CookingSlot currentOrder=new CookingSlot(order.getOrderId(),timeToCustomer,slotsRequired);
        this.existingOrders.add(currentOrder);
        this.remainingSlots -= slotsRequired;

        if (timeToCustomer <= DeliveryConstants.DELIVERY_LIMIT) {
            return timeToCustomer;
        } else {
            return -1;
        }

    }

}
