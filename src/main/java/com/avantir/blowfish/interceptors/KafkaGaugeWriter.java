package com.avantir.blowfish.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.GaugeWriter;

/**
 * Created by lekanomotayo on 21/10/2017.
 */
//@Component
public class KafkaGaugeWriter implements GaugeWriter {

    private static final Logger logger = LoggerFactory.getLogger(KafkaGaugeWriter.class);

    @Override
    public void set(Metric<?> value) {
        // log to kafka
        logger.debug("Name: {}, Time: {}, value: {}", value.getName(), value.getTimestamp().getTime(), value.getValue());
    }

}
