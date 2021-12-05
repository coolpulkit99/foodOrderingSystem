package com.weekday.orderingsystem.ov1.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
[{"orderId": 12, "meals": ["A", "A"], "distance": 5},
{"orderId": 21, "meals": ["A", "M"], "distance": 1},
{"orderId": 14, "meals": ["M", "M", "M", "M", "A", "A", "A"], "distance": 10},
{"orderId": 32, "meals": ["M"], "distance": 0.1},
{"orderId": 22, "meals": ["A"], "distance": 3}]
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    int orderId;
    List<Character> meals;
    double distance;
}
