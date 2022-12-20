package io.spring.scs_java_kstream_sample.producer;

import com.github.javafaker.Faker;

import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.spring.scs_java_kstream_sample.dto.address.Address;
import io.spring.scs_java_kstream_sample.dto.address.Country;
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
                var customerMessage = createCustomerMessage();
                log.debug("send customer message: {}", customerMessage);
                streamBridge.send("customerGenerator-out-0", customerMessage);
                
                var addressMessage = createAddressMessage(customerMessage.getPayload().getId());
                log.debug("send address message: {}", addressMessage);
                streamBridge.send("addressGenerator-out-0", addressMessage);
            }, 10L, 15L, TimeUnit.SECONDS);
        };
    }
    
    protected Message<Customer> createCustomerMessage() {
        var customer = Customer.builder()
                               .id(UUID.randomUUID())
                               .name(faker.name().name())
                               .type(Type.random())
                               .active(faker.bool().bool())
                               .nationality(faker.country().countryCode2())
                               .birthdate(LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneId.systemDefault()))
                               .createdAt(LocalDateTime.now())
                               .build();
        var message = MessageBuilder.withPayload(customer)
                                    .setHeader(KafkaHeaders.MESSAGE_KEY, customer.getId().toString().getBytes())
                                    .build();
        return message;
    }
    
    protected Message<Address> createAddressMessage(UUID customerId) {
        var fAddress = faker.address();
        var address = Address.builder()
                .id(UUID.randomUUID())
                .customerId(customerId)
                .fullAddress(fAddress.fullAddress())
                .street(fAddress.streetAddress())
                .zipCode(fAddress.zipCode())
                .city(fAddress.city())
                .state(fAddress.state())
                .country(Country.builder()
                                 .code(fAddress.countryCode())
                                 .name(fAddress.country())
                                 .build())
                .createdAt(LocalDateTime.now())
                .build();
        var message = MessageBuilder.withPayload(address)
                                    .setHeader(KafkaHeaders.MESSAGE_KEY, address.getId().toString().getBytes())
                                    .build();
        return message;
    }
}
