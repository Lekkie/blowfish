package com.avantir.blowfish.instrumentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Component;

/**
 * Created by lekanomotayo on 22/10/2017.
 */

@Component
public class KafkaMetricWriter implements MetricWriter{

    private static final Logger logger = LoggerFactory.getLogger(KafkaMetricWriter.class);

    @Override
    public void increment(Delta<?> delta) {
        // log to kafka
        logger.debug("Name: {}, Time: {}, value: {}", delta.getName(), delta.getTimestamp().getTime(), delta.getValue());
    }

    @Override
    public void set(Metric<?> value) {
        // log to kafka
        logger.debug("Name: {}, Time: {}, value: {}", value.getName(), value.getTimestamp().getTime(), value.getValue());
    }

    @Override
    public void reset(String metricName) {
        // log to kafka
        logger.debug("Name: {}, value: {}", metricName, "0.0");
    }

}
