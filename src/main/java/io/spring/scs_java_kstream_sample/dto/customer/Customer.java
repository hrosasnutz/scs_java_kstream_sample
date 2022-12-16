package io.spring.scs_java_kstream_sample.dto.customer;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private UUID id;
    private String name;
    private Type type;
    private Boolean active;
    private String country;
}
