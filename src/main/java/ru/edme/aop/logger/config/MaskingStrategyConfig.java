package ru.edme.aop.logger.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import ru.edme.aop.logger.strategy.LocalDateMaskingStrategy;
import ru.edme.aop.logger.strategy.MaskingStrategyRegistry;
import ru.edme.aop.logger.strategy.NoOpMaskingStrategy;
import ru.edme.aop.logger.strategy.StringMaskingStrategy;

@Configuration
@RequiredArgsConstructor
public class MaskingStrategyConfig {
    private final MaskingStrategyRegistry registry;
    private final StringMaskingStrategy stringStrategy;
    private final LocalDateMaskingStrategy localDateStrategy;
    private final NoOpMaskingStrategy<Object> noOpStrategy;
    
    @PostConstruct
    public void registerStrategies() {
        registry.registerStrategy(String.class, stringStrategy);
        registry.registerStrategy(java.time.LocalDate.class, localDateStrategy);
        registry.registerStrategy(Object.class, noOpStrategy);
    }
}
