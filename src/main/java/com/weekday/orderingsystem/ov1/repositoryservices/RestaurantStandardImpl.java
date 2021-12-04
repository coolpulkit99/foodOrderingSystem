package com.weekday.orderingsystem.ov1.repositoryservices;

import java.util.HashMap;
import java.util.PriorityQueue;

import com.weekday.orderingsystem.ov1.dto.CookingSlot;
import com.weekday.orderingsystem.ov1.dto.Order;
import com.weekday.orderingsystem.ov1.Constants.DeliveryConstants;
import com.weekday.orderingsystem.ov1.Constants.RestaurantConstants;

public class RestaurantStandardImpl implements Restaurant {

    PriorityQueue<CookingSlot> cookingSlots;
    int maxSlots;

    public RestaurantStandardImpl(int maxSlots) {
        cookingSlots = new PriorityQueue<>(
                (x, y) -> Integer.compare(x.getOrderCompletionTime(), y.getOrderCompletionTime()));
        this.maxSlots = maxSlots;
    }

    private PriorityQueue<CookingSlot> calculateUpdatedCookingSlotState(Order order) {
        PriorityQueue<CookingSlot> cookingSlotsTemp = new PriorityQueue<>(cookingSlots);

        for (Character c : order.getMeals()) {
            int slotRequired = RestaurantConstants.MEAL_SLOT_REQUIRED.get(c);
            int timeRequired = RestaurantConstants.MEAL_TIME_REQUIRED.get(c);
            int remainingSlots = this.maxSlots - cookingSlotsTemp.size() ;
            CookingSlot lastClearedSlot = null;

            for (int i = 1; i <= (slotRequired - remainingSlots) && !cookingSlotsTemp.isEmpty(); i++) {
                lastClearedSlot = cookingSlotsTemp.remove();
            }
            if (lastClearedSlot != null) {
                timeRequired += lastClearedSlot.getOrderCompletionTime();
            }
            CookingSlot newMeal = new CookingSlot(order.getOrderId(), timeRequired, c);
            cookingSlotsTemp.add(newMeal);
        }
        return cookingSlotsTemp;
    }

    @Override
    public double calculatePreprationTime(Order order) {
        PriorityQueue<CookingSlot> updatedCookingState = calculateUpdatedCookingSlotState(order);
        int maxTimeForOrder = 0;
        while (!updatedCookingState.isEmpty()) {
            CookingSlot cookingSlot = updatedCookingState.remove();
            if (cookingSlot.getOrderId() == order.getOrderId()) {
                maxTimeForOrder = Math.max(maxTimeForOrder, cookingSlot.getOrderCompletionTime());
            }
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

    @Override
    public double takeOrder(Order order) {

        if(! canAccomodateOrder(order)){
            return -1;
        }

        double timeToCustomer = calculateFulfillmentTime(order);
        if ( timeToCustomer <= DeliveryConstants.DELIVERY_LIMIT) {
            this.cookingSlots = calculateUpdatedCookingSlotState(order);
            return timeToCustomer;
        } else {
            return -1;
        }

    }

}
