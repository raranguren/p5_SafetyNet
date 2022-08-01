package com.ricaragas.safetynet.repository;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Log4j2
public class HttpTraceRepositoryImpl implements HttpTraceRepository {

    private HttpTrace lastTrace;

    // Return only the last HTTP trace when requested in actuator
    @Override
    public List<HttpTrace> findAll() {
        if (lastTrace == null) return List.of();
        return List.of(lastTrace);
    }

    // Log the trace at INFO level
    @Override
    public void add(HttpTrace trace) {
        lastTrace = trace;
        var request = trace.getRequest();
        var response = trace.getResponse();

        log.info("HTTP Request: {} {}", request.getMethod(), request.getUri());
        log.info("HTTP Response: {} {}", response.getStatus(), response.getHeaders());
    }
}
