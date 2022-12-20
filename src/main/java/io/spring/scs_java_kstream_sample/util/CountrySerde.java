package io.spring.scs_java_kstream_sample.util;

import org.springframework.kafka.support.serializer.JsonSerde;

import io.spring.scs_java_kstream_sample.dto.address.Country;

public class CountrySerde extends JsonSerde<Country> {
}
