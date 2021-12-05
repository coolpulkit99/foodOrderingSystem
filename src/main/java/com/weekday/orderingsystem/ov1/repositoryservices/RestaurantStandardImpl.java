package com.weekday.orderingsystem.ov1.repositoryservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import com.weekday.orderingsystem.ov1.dto.CookingSlot;
import com.weekday.orderingsystem.ov1.dto.Order;
import com.weekday.orderingsystem.ov1.Constants.DeliveryConstants;
import com.weekday.orderingsystem.ov1.Constants.RestaurantConstants;

public class RestaurantStandardImpl implements Restaurant {

    int maxSlots;
    double restaurantTimeOffest;
    int remainingSlots;
    PriorityQueue<CookingSlot> existingOrders;

    public RestaurantStandardImpl(int maxSlots) {
        this.maxSlots = maxSlots;
        this.restaurantTimeOffest = 0;
        this.remainingSlots = maxSlots;
        // min heap to get order sorting by min order delivery time
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

        // check if restaurant can accomodate order at once
        if (slotsRequired > this.maxSlots) {
            return -1;
        }

        List<CookingSlot> removedOrders=new ArrayList<>(); 
        double restaurantTimeOffestLocal=this.restaurantTimeOffest;
        int remainingSlotsLocal=this.remainingSlots;
        // check for empty slots and update slots and time if no empty slots 
        if (slotsRequired > remainingSlotsLocal) {
            while(remainingSlotsLocal<slotsRequired){
                CookingSlot lastFastestOrder = existingOrders.remove();
                removedOrders.add(lastFastestOrder);
                remainingSlotsLocal+= lastFastestOrder.getSlotsUsed();
                restaurantTimeOffestLocal = lastFastestOrder.getOrderCompletionTime();
            }
        }
 
        double timeToCustomer = calculateFulfillmentTime(order) + restaurantTimeOffestLocal;
        
        if (timeToCustomer <= DeliveryConstants.DELIVERY_LIMIT) {
            // add order to order heap and update restaurant state from local states
            CookingSlot currentOrder=new CookingSlot(order.getOrderId(),timeToCustomer,slotsRequired);
            remainingSlotsLocal -= slotsRequired;
            this.existingOrders.add(currentOrder);
            this.remainingSlots=remainingSlotsLocal;
            this.restaurantTimeOffest=restaurantTimeOffestLocal;
            return timeToCustomer;
        } else {
            // in case order is denied update restaurant state to older one
            for(CookingSlot removedOrder : removedOrders){
                this.existingOrders.add(removedOrder);
            }
            return -1;
        }
        

        

    }

}
