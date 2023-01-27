package com.banquito.transaction.Utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "spring.variable.account")
@Configuration("accountProperties")
public class Account {

    private String value;
}
