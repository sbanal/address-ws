package com.github.slbb.ws.address.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="api.admin")
@Getter
@Setter
public class ApiAdminUser {
    private String username;
    private String password;
}
