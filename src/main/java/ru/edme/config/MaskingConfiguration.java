package ru.edme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.edme.strategy.MaskingStrategyRegistry;

@Configuration
@EnableAspectJAutoProxy
public class MaskingConfiguration {
    
    @Bean
    public MaskingStrategyRegistry maskingStrategyRegistry() {
        return new MaskingStrategyRegistry();
    }
}