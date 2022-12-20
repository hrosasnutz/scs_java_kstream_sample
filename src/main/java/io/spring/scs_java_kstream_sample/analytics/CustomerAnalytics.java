package io.spring.scs_java_kstream_sample.analytics;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.spring.scs_java_kstream_sample.dto.address.Address;
import io.spring.scs_java_kstream_sample.dto.customer.Customer;
import io.spring.scs_java_kstream_sample.dto.customer.Type;
import io.spring.scs_java_kstream_sample.dto.mv.MVCustomersAndAddresses;
import io.spring.scs_java_kstream_sample.util.AddressSerde;
import io.spring.scs_java_kstream_sample.util.CustomerListSerde;
import io.spring.scs_java_kstream_sample.util.CustomerSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CustomerAnalytics {
    
    @Bean
    public Function<KStream<byte[], Customer>, KStream<byte[], Long>> countCustomersPerTypes() {
        return customers -> customers
                .map((k, v) -> new KeyValue<>(v.getType().name().getBytes(), 1L))
                .groupByKey(Grouped.with(Serdes.ByteArray(), Serdes.Long()))
                .count()
                .toStream()
                .peek((k, v) -> log.info("Total {} customers for {} type.", v, new String(k)));
    }
    
    @Bean
    public Function<KStream<byte[], Customer>, KStream<String, Long>> countCustomersPerNationalities() {
        return customers -> customers
                .map((k, v) -> new KeyValue<>(v.getNationality(), 1L))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                .count()
                .toStream()
                .peek((k, v) -> log.info("Total {} customers for {} nationality.", v, k));
    }

    @Bean
    public Function<KStream<byte[], Customer>, KStream<byte[], Customer>> filterActiveCustomers() {
        return customers -> customers
                .filter((k, v) -> v.getActive())
                .peek((k, v) -> log.info("Customer active: {}", v));
    }
    
    @Bean
    public Function<KStream<byte[], Customer>, KStream<byte[], Customer>[]> branchPrivilegedCustomers() {
        return customers -> customers
                .split()
                .branch((k, v) -> Type.isPrivileged(v.getType()))
                .defaultBranch()
                .values()
                .toArray(new KStream[0]);
    }
    
    @Bean
    public Function<KStream<byte[], Customer>, KStream<String, List<Customer>>> aggregateCustomersPerNationalities() {
        return customers -> customers
                .map((k, v) -> new KeyValue<>(v.getNationality(), v))
                .groupByKey(Grouped.with(Serdes.String(), new CustomerSerde()))
                .aggregate(
                        () -> new ArrayList<Customer>(),
                        (k, v, a) -> { a.add(v); return a; },
                        Materialized.with(Serdes.String(), new CustomerListSerde()))
                .mapValues(v -> (List<Customer>) v)
                .toStream()
                .peek((k, v) -> log.info("List of {} customers for {} nationality.", v, k));
    }

    @Bean
    public BiFunction<KStream<byte[], Customer>, KStream<byte[], Address>, KStream<byte[], MVCustomersAndAddresses>> materializedViewCustomersAndAddresses() {
        return (customers, addresses) -> customers
                .join(addresses.selectKey((k, v) -> v.getCustomerId().toString().getBytes()),
                        (c, a) -> new MVCustomersAndAddresses(c, a),
                        JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(5)),
                        StreamJoined.with(Serdes.ByteArray(), new CustomerSerde(), new AddressSerde()))
                .peek((k, v) -> log.info("Materialized View Customer And Address: {}", v));
    }
}
