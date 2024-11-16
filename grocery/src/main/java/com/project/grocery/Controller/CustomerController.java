package com.project.grocery.Controller;

import com.lowagie.text.DocumentException;
import com.project.grocery.Entity.Customer;
import com.project.grocery.Exception.ApiResponse;
import com.project.grocery.Service.CustomerService;
import com.project.grocery.utils.StatusMessageUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    // To add the customer
    @PostMapping("/customer")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {
        // Log the incoming request at debug level
        logger.debug("Received createCustomer request with customer: {}", customer);

        // Call the service method to add the customer
        Customer addedCustomer = customerService.addCustomer(customer);

        // Log the successful addition of the customer at info level
        logger.info("Customer added successfully: {}", addedCustomer);

        // Prepare the response with success message and added customer details
        ApiResponse response = new ApiResponse(StatusMessageUtil.getStatusMessage("customer_added_success"), true,
                HttpStatus.OK.value(), addedCustomer);

        // Return a ResponseEntity with the response body and HTTP status OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // To get all customers
    @GetMapping("/customers")
    public ResponseEntity<?> getAllCustomers() {
        // Log the incoming request at debug level
        logger.info("Received getAllCustomers request");

        // Call the service method to retrieve all customers
        List<Customer> customers = customerService.getAllCustomers();

        // Log successful retrieval of all customers at info level
        logger.info("Retrieved all customers successfully {}", customers);

        // Prepare the response with success message and retrieved customers
        ApiResponse response = new ApiResponse(StatusMessageUtil.getStatusMessage("customers_retrieved_success"), true,
                HttpStatus.OK.value(), customers);

        // Return a ResponseEntity with the response body and HTTP status OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // To get customer by id
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long customerId) {
        // Log the request received for customer ID
        logger.debug("Received getCustomerById request for customer ID: {}", customerId);

        // Retrieve the customer details from the service layer
        Customer customer = customerService.getCustomerById(customerId);

        // Log the successful retrieval of customer details
        logger.info("Retrieved customer with ID {}: {}", customerId, customer);

        // Prepare response with retrieved customer details
        ApiResponse response = new ApiResponse(
                StatusMessageUtil.getStatusMessage("customer_retrieved_success"), // Retrieve success message
                true, // Indicate operation success
                HttpStatus.OK.value(), // Set HTTP status code to 200 OK
                customer // Include retrieved customer in the response body
        );

        // Return ResponseEntity with success status and customer details
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // To update the customer
    @PutMapping("/customer/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long customerId, @RequestBody Customer customer) {
        // Log the incoming request at debug level
        logger.debug("Received updateCustomer request for customer ID: {}", customerId);

        // Call the service method to update the customer
        ResponseEntity<?> newResponse = customerService.updateCustomer(customerId, customer);

        // Log successful update of customer details at info level
        logger.info("Customer details updated successfully for customer ID: {}", customerId);

        // Prepare the response with success message and updated customer details
        ApiResponse response = new ApiResponse(StatusMessageUtil.getStatusMessage("customer_updated_success"), true,
                HttpStatus.OK.value(), newResponse.getBody());

        // Return a ResponseEntity with the response body and HTTP status OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // To delete the customer
    @DeleteMapping("/customer/{customerId}") // Ensure the path variable name matches the method parameter
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId) {
        logger.debug("Received deleteCustomer request for customer ID: {}", customerId); // Log the request
        ResponseEntity<?> response; // Initialize response

        ResponseEntity<?> newResponse = customerService.deleteCustomer(customerId); // Delete the customer
        logger.info("Customer with ID {} deleted successfully", customerId); // Log success
        response = new ResponseEntity<>(
                new ApiResponse(StatusMessageUtil.getStatusMessage("customer_deleted_success"), true,
                        HttpStatus.OK.value(), newResponse.getBody()),
                HttpStatus.OK); // Return success response

        return response; // Return the response
    }

    // To get the pdf
    @GetMapping("/customers/v1")
    public ResponseEntity<?> generateCustomerPdfFile(HttpServletResponse response)
            throws IOException, DocumentException {
        try {
            // Setting up response type
            response.setContentType("application/pdf");

            // Generate current date-time string for unique file name
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String currentDateTime = dateFormat.format(new Date());

            // Setting the Content-Disposition header to force download
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=CustomerList_" + currentDateTime + ".pdf";
            response.setHeader(headerKey, headerValue);

            // Retrieve all customers from the service
            List<Customer> customers = customerService.getAllCustomers();

            // Generate PDF of customer list and write it to HttpServletResponse's
            // OutputStream
            customerService.generateCustomerPdf(customers, response);

            // Log successful PDF generation
            logger.info("PDF generated successfully");

            // Return ResponseEntity with OK status
            return ResponseEntity.ok().build();
        } catch (IOException | DocumentException e) {
            // Log error if PDF generation fails
            logger.error("Error generating PDF: {}", e.getMessage());
            throw e; // Re-throw the exception to be handled globally
        }
    }

}
