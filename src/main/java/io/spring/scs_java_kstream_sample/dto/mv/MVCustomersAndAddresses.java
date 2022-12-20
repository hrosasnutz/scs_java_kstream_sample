package io.spring.scs_java_kstream_sample.dto.mv;

import io.spring.scs_java_kstream_sample.dto.address.Address;
import io.spring.scs_java_kstream_sample.dto.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MVCustomersAndAddresses {
    private Customer customer;
    private Address address;
}
