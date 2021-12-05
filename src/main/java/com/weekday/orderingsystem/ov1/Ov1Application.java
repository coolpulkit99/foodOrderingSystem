package com.weekday.orderingsystem.ov1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.weekday.orderingsystem.ov1.Constants.RestaurantConstants;
import com.weekday.orderingsystem.ov1.dto.Order;
import com.weekday.orderingsystem.ov1.networkoperations.ResponseParser;
import com.weekday.orderingsystem.ov1.repositoryservices.Restaurant;
import com.weekday.orderingsystem.ov1.repositoryservices.RestaurantStandardImpl;

public class Ov1Application {

	public static void main(String[] args){
		//pass json file location as cmd line param
		Order[] orders = ResponseParser.convertJsonToOrders(args[0]);		
		Restaurant restaurant=new RestaurantStandardImpl(RestaurantConstants.MAX_SLOTS);
		List<String> outputs = calculateOrderQueueTimes(orders, restaurant);
		for(String output: outputs){
			System.out.println(output);
		}
	}

	public static List<String> calculateOrderQueueTimes(Order[] orders, Restaurant restaurant){
		List<String> preprationTimeMessages= new ArrayList<>();
		DecimalFormat df=Utils.getDecimalFormatter();
		for(Order order:orders){
			double timeToCustomer = restaurant.takeOrder(order);
			if(timeToCustomer==-1){
				preprationTimeMessages.add("Order "+order.getOrderId()+" is denied because the restaurant cannot accommodate it.");
			} else {
				preprationTimeMessages.add("Order "+ order.getOrderId() + " will get delivered in " + df.format(timeToCustomer)+" minutes");
			}

		}
		return preprationTimeMessages;
	}

}
