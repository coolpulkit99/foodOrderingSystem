package com.weekday.orderingsystem.ov1.networkoperations;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weekday.orderingsystem.ov1.dto.Order;

public class ResponseParser {

    public static Order[] convertJsonToOrders(String filePath){
        ObjectMapper objectMapper = new ObjectMapper();
		try {
            //replace with network operations later, ex getting response from api
            Order[] orders = objectMapper.readValue(new File(filePath), Order[].class);
            return orders;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Order[0];
    } 
    
}
