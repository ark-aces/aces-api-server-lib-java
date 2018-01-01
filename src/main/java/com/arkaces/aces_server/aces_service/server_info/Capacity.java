package com.arkaces.aces_server.aces_service.server_info;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Capacity {
    private BigDecimal value;
    private String unit;
    private String displayValue;
}
