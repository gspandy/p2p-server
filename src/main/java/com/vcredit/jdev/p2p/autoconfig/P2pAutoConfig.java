package com.vcredit.jdev.p2p.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(ThymeleafAutoConfiguration.class)
public class P2pAutoConfig {

}
