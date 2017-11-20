package com.movilizer.mds.connector;


import com.movilizer.mds.connector.exceptions.MovilizerConnectorException;
import com.movilizer.mds.webservice.Movilizer;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.time.Duration;

@Configuration
@ComponentScan(basePackages = "com.movilizer.mds.connector")
@EnableConfigurationProperties
@ConfigurationProperties(prefix="movilizer")
public class MovilizerConnectorConfig {
    private static final Integer DEFAULT_SINK_BUFFER_SIZE = 512; // It has to be a power of 2

    private String appName;
    private String endpoint;
    private Long systemId;
    private String password;

    private Duration pollingInterval;
    private Integer pollNumResponses;

    private Duration pushingInterval;
    private Integer pushConsolidatedElementsSize;

    private Integer metricsReportInterval;
    private String metricsEndpoint;
    private Integer metricsPort;

    public MovilizerConnectorConfig() {
    }

    public MovilizerDistributionService createMdsInstance() {
        try {
            return Movilizer.buildConf()
                    .setEndpoint(getEndpoint())
                    .getService();
        } catch (MalformedURLException e) {
            throw new MovilizerConnectorException(e);
        }
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPushConsolidatedElementsSize() {
        Integer out = pushConsolidatedElementsSize;
        if (out == null) {
            out = DEFAULT_SINK_BUFFER_SIZE;
        }
        return out;
    }

    public void setPushConsolidatedElementsSize(Integer pushConsolidatedElementsSize) {
        assert pushConsolidatedElementsSize % 2 == 0;
        this.pushConsolidatedElementsSize = pushConsolidatedElementsSize;
    }

    public Duration getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(Duration pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public void setPollingInterval(Integer pollingIntervalInSeconds) {
        this.pollingInterval = Duration.ofSeconds(pollingIntervalInSeconds);
    }

    public Duration getPushingInterval() {
        return pushingInterval;
    }

    public void setPushingInterval(Duration pushingInterval) {
        this.pushingInterval = pushingInterval;
    }

    public void setPushingInterval(Integer pushingIntervalInSeconds) {
        this.pushingInterval = Duration.ofSeconds(pushingIntervalInSeconds);
    }

    public Integer getPollNumResponses() {
        return pollNumResponses;
    }

    public void setPollNumResponses(Integer pollNumResponses) {
        this.pollNumResponses = pollNumResponses;
    }

    public Integer getMetricsReportInterval() {
        return metricsReportInterval;
    }

    public void setMetricsReportInterval(Integer metricsReportInterval) {
        this.metricsReportInterval = metricsReportInterval;
    }

    public String getMetricsEndpoint() {
        return metricsEndpoint;
    }

    public void setMetricsEndpoint(String metricsEndpoint) {
        this.metricsEndpoint = metricsEndpoint;
    }

    public Integer getMetricsPort() {
        return metricsPort;
    }

    public void setMetricsPort(Integer metricsPort) {
        this.metricsPort = metricsPort;
    }
}
