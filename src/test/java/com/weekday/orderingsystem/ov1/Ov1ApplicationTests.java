package com.weekday.orderingsystem.ov1;

import org.junit.jupiter.api.Assertions;

import java.util.List;

import com.weekday.orderingsystem.ov1.Constants.RestaurantConstants;
import com.weekday.orderingsystem.ov1.dto.Order;
import com.weekday.orderingsystem.ov1.networkoperations.ResponseParser;
import com.weekday.orderingsystem.ov1.repositoryservices.Restaurant;
import com.weekday.orderingsystem.ov1.repositoryservices.RestaurantStandardImpl;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

@SpringBootTest(classes = {Ov1Application.class})
class Ov1ApplicationTests {

	@Test
	void integrationTest1() {
		Order[] orders = ResponseParser.convertJsonToOrders("src/test/resources/data.json");		
		Restaurant restaurant=new RestaurantStandardImpl(RestaurantConstants.MAX_SLOTS);
		List<String> outputs = Ov1Application.calculateOrderQueueTimes(orders, restaurant);
    for(String output:outputs)
    System.out.println(output);
		// Assertions.assertEquals("Order 12 will get delivered in 57 minutes",outputs.get(0));
		// Assertions.assertEquals("Order 21 will get delivered in 37 minutes",outputs.get(1));
		// Assertions.assertEquals("Order 14 is denied because the restaurant cannot accommodate it.",outputs.get(2));
		// Assertions.assertEquals("Order 32 will get delivered in 29.8 minutes",outputs.get(3));
		// Assertions.assertEquals("Order 22 will get delivered in 70.8 minutes",outputs.get(4));
	}


}
