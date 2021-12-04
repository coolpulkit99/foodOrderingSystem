package com.weekday.orderingsystem.ov1.Constants;

import java.util.HashMap;

final public class RestaurantConstants {
    
    // Feature TODO: Change logic for constant updation later, ex: updating delivery constant times from database 
    
    private RestaurantConstants(){
    }

    // public static final int A_TIME=17;  // in minutes
    // public static final int M_TIME=29;  // in minutes
    // public static final int A_SLOT_REQUIRED=1; 
    // public static final int M_SLOT_REQUIRED=2; 
    public final static HashMap<Character,Integer> MEAL_TIME_REQUIRED = new HashMap<>();            
    public final static HashMap<Character,Integer> MEAL_SLOT_REQUIRED = new HashMap<>();            
        
    static{
        final char[] MEAL_TYPE = {'A', 'M'};
        final int[] TIME_REQUIRED = {17, 29};
        final int[] SLOT_REQUIRED = {1, 2};
        
        for(int i = 0; i < MEAL_TYPE.length; i++){
            MEAL_TIME_REQUIRED.put(MEAL_TYPE[i], TIME_REQUIRED[i]);
            MEAL_SLOT_REQUIRED.put(MEAL_TYPE[i], SLOT_REQUIRED[i]);
        }
    }

    public static final int MAX_SLOTS=7;
     
}
