package com.weekday.orderingsystem.ov1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CookingSlot {
    int orderId;
    double orderCompletionTime;
    int slotsUsed;   
}
