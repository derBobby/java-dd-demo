package com.example.demo;

import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class MetricsConfig {

    @Autowired
    private MeterRegistry meterRegistry;

    @Value("${management.metrics.export.datadog.tags.service:employee-service}")
    private String service;

    @Value("${management.metrics.export.datadog.tags.env:production}")
    private String environment;

    @Value("${management.metrics.export.datadog.tags.version:1.0.0}")
    private String version;

    public MetricsConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void configureMetrics() {
        meterRegistry.config().commonTags("service", service, "env", environment, "version", version);
    }
}


