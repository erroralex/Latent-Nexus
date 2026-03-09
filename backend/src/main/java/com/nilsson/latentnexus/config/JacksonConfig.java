package com.nilsson.latentnexus.config;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Jackson serialization, specifically addressing Hibernate-related issues.
 * <p>
 * This class provides a `Hibernate6Module` bean, which is essential for proper JSON serialization
 * of Hibernate entities, especially when dealing with lazy-loaded relationships. Without this module,
 * Jackson might encounter problems serializing uninitialized lazy collections or proxies,
 * leading to errors like `LazyInitializationException` or incorrect JSON output.
 * </p>
 * <p>
 * By registering `Hibernate6Module`, Jackson is instructed on how to handle Hibernate-specific
 * object types, ensuring that entities are serialized correctly, even if their lazy-loaded
 * properties are not yet fetched from the database.
 * </p>
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Hibernate6Module hibernate6Module() {
        return new Hibernate6Module();
    }
}
