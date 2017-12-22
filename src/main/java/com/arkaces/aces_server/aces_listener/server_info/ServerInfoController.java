package com.arkaces.aces_server.aces_listener.server_info;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerInfoController {
    
    private final Environment environment;    
    
    @GetMapping("/")
    public ServerInfo getServerInfo() {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setName(environment.getProperty("serverInfo.name"));        
        serverInfo.setDescription(environment.getProperty("serverInfo.description"));        
        serverInfo.setVersion(environment.getProperty("serverInfo.version"));        
        serverInfo.setWebsiteUrl(environment.getProperty("serverInfo.websiteUrl"));
        
        return serverInfo;
    }
}
