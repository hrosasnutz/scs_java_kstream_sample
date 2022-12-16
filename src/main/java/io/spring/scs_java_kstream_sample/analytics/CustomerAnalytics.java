package io.spring.scs_java_kstream_sample.analytics;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

import io.spring.scs_java_kstream_sample.dto.customer.Customer;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CustomerAnalytics {
    
    @Bean
    public Function<KStream<byte[], Customer>, KStream<byte[], Long>> countCustomerPerTypes() {
        return customers -> customers
                .map((k, v) -> new KeyValue<>(v.getType().name().getBytes(), 1L))
                .groupByKey(Grouped.with(Serdes.ByteArray(), Serdes.Long()))
                .count()
                .toStream()
                .peek((k, v) -> log.info("Total {} customers for {} type.", v, new String(k)));
    }
}
