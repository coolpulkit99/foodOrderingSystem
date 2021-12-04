package com.weekday.orderingsystem.ov1;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weekday.orderingsystem.ov1.Constants.RestaurantConstants;
import com.weekday.orderingsystem.ov1.dto.Order;
import com.weekday.orderingsystem.ov1.repositoryservices.Restaurant;
import com.weekday.orderingsystem.ov1.repositoryservices.RestaurantStandardImpl;

public class Ov1Application {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Order[] orders = objectMapper.readValue(new File("src/main/resources/data.json"), Order[].class);
		// System.out.print(orders);
		Restaurant restaurant=new RestaurantStandardImpl(RestaurantConstants.MAX_SLOTS);
		for(Order order:orders){
			double timeToCustomer = restaurant.takeOrder(order);
			if(timeToCustomer==-1){
				System.out.println(order.getOrderId()+" Denied");
			} else {
				System.out.println(order.getOrderId() + " " + timeToCustomer);
			}

		}


	}

}
