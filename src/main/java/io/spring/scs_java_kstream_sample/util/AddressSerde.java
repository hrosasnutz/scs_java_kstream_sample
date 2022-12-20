package io.spring.scs_java_kstream_sample.util;

import org.springframework.kafka.support.serializer.JsonSerde;

import io.spring.scs_java_kstream_sample.dto.address.Address;

public class AddressSerde extends JsonSerde<Address> {
}
