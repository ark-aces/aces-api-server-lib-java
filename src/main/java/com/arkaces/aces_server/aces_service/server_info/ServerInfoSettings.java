package com.arkaces.aces_server.aces_service.server_info;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ServerInfoSettings {
    private String name;
    private String description;
    private String version;
    private String websiteUrl;
    private String instructions;
    private List<Capacity> capacities;
    private MoneyAmount flatFee;
    private BigDecimal percentFee;
    private String inputSchema;
    private String outputSchema;
    private List<String> interfaces;
}
