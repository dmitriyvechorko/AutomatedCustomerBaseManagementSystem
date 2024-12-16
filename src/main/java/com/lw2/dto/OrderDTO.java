package com.lw2.dto;

import com.lw2.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDTO {
    private String theme;
    private String details;
    private OrderStatus status;
}
