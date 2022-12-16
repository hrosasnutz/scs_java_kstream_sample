package io.spring.scs_java_kstream_sample.producer;

import com.github.javafaker.Faker;

import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.spring.scs_java_kstream_sample.dto.customer.Customer;
import io.spring.scs_java_kstream_sample.dto.customer.Type;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CustomerProducer {
    
    private StreamBridge streamBridge;
    
    private Faker faker;
    
    public CustomerProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
        this.faker = new Faker();
    }
    
    @Bean
    public ApplicationRunner customersFactory() {
        return (args) -> {
            Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
                var customer = Customer.builder()
                                       .id(UUID.randomUUID())
                                       .name(faker.name().name())
                                       .type(Type.random())
                                       .active(faker.bool().bool())
                                       .country(faker.address().countryCode())
                                       .build();
                var message = MessageBuilder.withPayload(customer)
                                            .setHeader(KafkaHeaders.MESSAGE_KEY, customer.getId().toString().getBytes())
                                            .build();
                log.debug("send customer message: {}", message);
                streamBridge.send("customerGenerator-out-0", message);
            }, 10L, 15L, TimeUnit.SECONDS);
        };
    }
}
