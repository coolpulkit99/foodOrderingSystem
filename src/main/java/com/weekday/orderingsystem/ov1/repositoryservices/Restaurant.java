package com.weekday.orderingsystem.ov1.repositoryservices;

import java.util.Collection;

import com.weekday.orderingsystem.ov1.dto.Order;

public interface Restaurant {

    double calculatePreprationTime(Order order);
    double calculateDeliveryTime(Order order);
    double calculateFulfillmentTime(Order order);
    double takeOrder(Order order);
}
