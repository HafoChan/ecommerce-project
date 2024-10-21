package com.sohan.user_service.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Data
@Service
@ConfigurationProperties
public class VaultConfig {

    @Value("${vault.db.username}")
    private String username;

    @Value("${vault.db.password}")
    private String password;

    @Value("${vault.key.signerKey}")
    private String signerKey;
}
