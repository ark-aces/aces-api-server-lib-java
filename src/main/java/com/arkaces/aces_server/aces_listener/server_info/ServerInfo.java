package com.arkaces.aces_server.aces_listener.server_info;

import lombok.Data;

@Data
public class ServerInfo {
    private String name;
    private String description;
    private String version;
    private String websiteUrl;
}
