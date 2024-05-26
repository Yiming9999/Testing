package com.amigoscode.testing.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request) {
        Customer requestCustomer = request.getCustomer();

        Optional<Customer> customerOptional = customerRepository
                .selectCustomerByPhoneNumber(requestCustomer.getPhoneNumber());

        if (customerOptional.isPresent()) {
            if (customerOptional.get().getName().equals(requestCustomer.getName())) {
                return;
            }
            throw new IllegalStateException(String.format("phone number [%s] is taken", requestCustomer.getPhoneNumber()));
        }

        if (request.getCustomer().getId() == null) {
            request.getCustomer().setId(UUID.randomUUID());
        }

        customerRepository.save(requestCustomer);
    }
}
