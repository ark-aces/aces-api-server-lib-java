package com.arkaces.aces_server.aces_service.server_info;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerInfoController {
    
    private final ServerInfoSettings serverInfoSettings;    
    private final ObjectMapper objectMapper;
    
    @GetMapping("/")
    public ServerInfo getServerInfo() {
        JsonNode inputSchemaJsonNode;
        try {
            inputSchemaJsonNode = objectMapper.readTree(serverInfoSettings.getInputSchema());
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse inputSchema json", e);
        }

        JsonNode outputSchemaJsonNode;
        try {
            outputSchemaJsonNode = objectMapper.readTree(serverInfoSettings.getOutputSchema());
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse outputSchema json", e);
        }

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setName(serverInfoSettings.getName());
        serverInfo.setDescription(serverInfoSettings.getDescription());
        serverInfo.setInstructions(serverInfoSettings.getInstructions());
        serverInfo.setVersion(serverInfoSettings.getVersion());
        serverInfo.setWebsiteUrl(serverInfoSettings.getWebsiteUrl());
        serverInfo.setCapacities(serverInfoSettings.getCapacities());
        serverInfo.setFlatFee(serverInfoSettings.getFlatFee());
        serverInfo.setPercentFee(serverInfoSettings.getPercentFee());
        serverInfo.setInputSchema(inputSchemaJsonNode);
        serverInfo.setOutputSchema(outputSchemaJsonNode);
        serverInfo.setInterfaces(serverInfoSettings.getInterfaces());
    
        return serverInfo;
    }
}
