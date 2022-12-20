package io.spring.scs_java_kstream_sample.util;

import org.springframework.kafka.support.serializer.JsonSerde;

import io.spring.scs_java_kstream_sample.dto.mv.MVCustomersAndAddresses;

public class MVCustomerSerde extends JsonSerde<MVCustomersAndAddresses> {
}
