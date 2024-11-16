package com.project.grocery.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.lowagie.text.DocumentException;
import com.project.grocery.Entity.Customer;

import jakarta.servlet.http.HttpServletResponse;

public interface CustomerService {
    // To add the customers
    Customer addCustomer(Customer customer);

    // To get all products
    List<Customer> getAllCustomers();

    // To update the customer
    ResponseEntity<?> updateCustomer(Long customerId, Customer customer);

    // To delete the customer
    ResponseEntity<?> deleteCustomer(Long customerId);

    // To get customer by id
    Customer getCustomerById(Long customerId);

    // To genrate the pdf of available customers
    public void generateCustomerPdf(List<Customer> customerList, HttpServletResponse response)
            throws DocumentException, IOException;

}
