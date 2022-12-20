package io.spring.scs_java_kstream_sample.dto.address;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private UUID id;
    private UUID customerId;
    private String fullAddress;
    private String street;
    private String zipCode;
    private String city;
    private String state;
    private Country country;
    private LocalDateTime createdAt;
}
