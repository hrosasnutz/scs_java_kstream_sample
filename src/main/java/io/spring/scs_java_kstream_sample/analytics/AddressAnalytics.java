package io.spring.scs_java_kstream_sample.analytics;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

import io.spring.scs_java_kstream_sample.dto.address.Address;
import io.spring.scs_java_kstream_sample.dto.address.Country;
import io.spring.scs_java_kstream_sample.util.CountrySerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AddressAnalytics {
    
    @Bean
    public Function<KStream<byte[], Address>, KStream<String, Country>> reduceAddressesToCountry() {
        return addresses -> addresses
                .map((k, v) -> new KeyValue<>(v.getCountry().getCode(), v.getCountry()))
                .groupByKey(Grouped.with(Serdes.String(), new CountrySerde()))
                .reduce((v1, v2) -> v1)
                .toStream()
                .peek((k, v) -> log.info("Country: {}", v));
    }
}
