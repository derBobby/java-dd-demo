package eu.planlos.datadogdemo.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestMetricsService {

    private final Counter successfulRequests;
    private final Counter failedRequests;

    @Autowired
    public RequestMetricsService(MeterRegistry meterRegistry) {
        this.successfulRequests = meterRegistry.counter("http.requests.successful");
        this.failedRequests = meterRegistry.counter("http.requests.failed");
    }

    public void incrementSuccessfulRequests() {
        successfulRequests.increment();
    }

    public void incrementFailedRequests() {
        failedRequests.increment();
    }
}
