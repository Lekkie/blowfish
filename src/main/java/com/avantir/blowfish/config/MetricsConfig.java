package com.avantir.blowfish.config;

import com.avantir.blowfish.interceptors.KafkaMetricWriter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.*;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.boot.actuate.endpoint.MetricsEndpointMetricReader;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lekanomotayo on 21/10/2017.
 */

@Configuration
public class MetricsConfig {

    //@Autowired
    //KafkaGaugeWriter kafkaGaugeWriter;
    //@Autowired
    //KafkaMetricWriter kafkaMetricWriter;
    final MetricRegistry metricRegistry = new MetricRegistry();
    final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();


    @Bean
    public MetricsEndpointMetricReader metricsEndpointMetricReader(MetricsEndpoint metricsEndpoint) {
        return new MetricsEndpointMetricReader(metricsEndpoint);
    }


    /*
    @Bean
    @ExportMetricWriter
    public GaugeWriter messageChannelGaugeWriter() {
        //return new KafkaGaugeWriter();
        return kafkaGaugeWriter;
    }
    */

    @Bean
    @ExportMetricWriter
    public MetricWriter kafkaMetricWriter() {
        return new KafkaMetricWriter();
        //return kafkaMetricWriter;
    }

    // Add a metric to registry
    @Bean
    public MetricRegistry metricRegistry() {
        // JVM stats
        metricRegistry.register("jvm.memory",new MemoryUsageGaugeSet());
        metricRegistry.register("jvm.thread-states",new ThreadStatesGaugeSet());
        metricRegistry.register("jvm.garbage-collector",new GarbageCollectorMetricSet());
        metricRegistry.register("jvm.classloading",new ClassLoadingGaugeSet());
        metricRegistry.register("jvm.file-descriptor",new FileDescriptorRatioGauge());
        //HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        //healthCheckRegistry.register("health-check.file-descriptor",new Heal());
        //metricRegistry.histogram("histogram");
        return metricRegistry;
    }

    @Bean
    public HealthCheckRegistry healthCheckRegistry(){
        return healthCheckRegistry;
    }



}
