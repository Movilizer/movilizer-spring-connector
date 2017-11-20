package com.movilizer.mds.connector.utils;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationPropertiesBinding
public class DurationConverter implements Converter<Integer, Duration> {
    @Override
    public Duration convert(Integer source) {
        if (source == null) {
            return null;
        }
        return Duration.ofSeconds(source);
    }
}
