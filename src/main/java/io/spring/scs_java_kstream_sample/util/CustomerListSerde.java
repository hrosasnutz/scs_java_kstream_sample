package io.spring.scs_java_kstream_sample.util;

import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.List;

import io.spring.scs_java_kstream_sample.dto.customer.Customer;

public class CustomerListSerde extends JsonSerde<List<Customer>> {
}
