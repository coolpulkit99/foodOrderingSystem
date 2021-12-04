package com.weekday.orderingsystem.ov1.repositoryservices;

import java.util.Collection;

import com.weekday.orderingsystem.ov1.dto.Order;

public interface Restaurant {

    public double calculatePreprationTime(Order order);
    public double calculateDeliveryTime();
    
}
