package com.weekday.orderingsystem.ov1.repositoryservices;

import java.util.HashMap;
import java.util.PriorityQueue;

import com.weekday.orderingsystem.ov1.dto.CookingSlot;
import com.weekday.orderingsystem.ov1.dto.Order;
import com.weekday.orderingsystem.ov1.Constants.RestaurantConstants;

public class RestaurantStandardImpl implements Restaurant {

    PriorityQueue<CookingSlot> cookingSlots;
    int maxSlots;

    RestaurantStandardImpl(int maxSlots) {
        cookingSlots = new PriorityQueue<>(
                (x, y) -> Integer.compare(x.getOrderCompletionTime(), y.getOrderCompletionTime()));
        this.maxSlots = maxSlots;
    }

    private PriorityQueue<CookingSlot> calculateUpdatedCookingSlotState(Order order) {

        PriorityQueue<CookingSlot> cookingSlotsTemp = new PriorityQueue<>(cookingSlots);

        for (Character c : order.getMeals()) {
            int slotRequired = RestaurantConstants.MEAL_TIME_REQUIRED.get(c);
            int timeRequired = RestaurantConstants.MEAL_TIME_REQUIRED.get(c);
            int remainingSlots = cookingSlotsTemp.size() - this.maxSlots; 
            CookingSlot lastClearedSlot = null;
            
            for(int i=1;i<=(slotRequired-remainingSlots) && !cookingSlotsTemp.isEmpty(); i++ )
                lastClearedSlot=cookingSlotsTemp.remove();
            
            if(lastClearedSlot != null)
                timeRequired += lastClearedSlot.getOrderCompletionTime();

            CookingSlot newMeal=new CookingSlot(order.getOrderId(), timeRequired, c);
            cookingSlotsTemp.add(newMeal);

            
        }

        // HashMap<Integer, Integer> mealToSlotMap = new HashMap<>();

        // Map slots taken to the meal count that take those particular slots

        // switch (c) {
        //     case 'A':
        //         // mealToSlotMap.put(RestaurantConstants.A_SLOT_REQUIRED,
        //         //         mealToSlotMap.getOrDefault(RestaurantConstants.A_SLOT_REQUIRED, 0));
        //         break;
        //     case 'M':
        //         // mealToSlotMap.put(RestaurantConstants.M_SLOT_REQUIRED,
        //         //         mealToSlotMap.getOrDefault(RestaurantConstants.M_SLOT_REQUIRED, 0));
        //         break;
        // }
        

        return cookingSlotsTemp;

    }

    @Override
    public double calculatePreprationTime(Order order) {

        return 0;
    }

    @Override
    public double calculateDeliveryTime() {

        return 0;

    }

}
