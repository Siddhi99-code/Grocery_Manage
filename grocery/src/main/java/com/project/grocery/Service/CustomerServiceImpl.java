package com.project.grocery.Service;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.project.grocery.Entity.Customer;
import com.project.grocery.Exception.ApiRequestFailedException;
import com.project.grocery.Repository.CustomerRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepo;
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

   

    /**
     * Adds a new customer to the repository.
     * Logs the addition of the customer and returns the added customer if
     * successful.
     * Throws an ApiRequestFailedException if an error occurs during addition.
     *
     * @param customer The customer object to be added.
     * @return The customer object that was added.
     * @throws ApiRequestFailedException If an error occurs while adding the
     *                                   customer.
     */
    @Override
    public Customer addCustomer(Customer customer) {
        try {
            // Save the customer to the repository
            Customer addedCustomer = customerRepo.save(customer);

            // Log the successful addition of the customer
            logger.info("Customer added successfully: {}", addedCustomer);

            // Return the added customer
            return addedCustomer;
        } catch (Exception e) {
            // Log the error if addition fails
            logger.error("Failed to add customer: {}", e.getMessage());

            // Throw custom exception with appropriate message
            throw new ApiRequestFailedException("Failed to add customer", e.getMessage());
        }
    }

    /**
     * Retrieves a list of all customers from the repository.
     * Logs the number of customers retrieved and returns the list of customers.
     *
     * @return A list containing all customers retrieved from the repository.
     */
    @Override
    public List<Customer> getAllCustomers() {
        // Retrieve all customers from the repository
        List<Customer> customers = customerRepo.findAll();

        // Log the retrieval of all customers
        logger.info("Retrieved {} customers", customers.size());

        // Return the list of customers
        return customers;
    }

    /**
     * Updates an existing customer in the repository.
     * Retrieves the customer by customerId, updates its details with new data,
     * saves the updated customer, and logs the update operation.
     *
     * @param customerId The ID of the customer to be updated.
     * @param customer   The updated customer object containing new data.
     * @return ResponseEntity containing the updated customer details if successful.
     * @throws ApiRequestFailedException If the customer with the specified ID is
     *                                   not found.
     */
    public ResponseEntity<?> updateCustomer(Long customerId, Customer customer) {
        // Retrieve the customer data from the repository based on customerId
        Optional<Customer> customerData = customerRepo.findById(customerId);

        // Check if the customer exists
        if (!customerData.isPresent()) {
            // Throw an exception if customer is not found
            throw new ApiRequestFailedException("Customer not found with id: " + customerId);
        }

        // Get the existing customer object from Optional
        Customer existingCustomer = customerData.get();

        // Log the update operation
        logger.info("Updating customer with ID: {}", customerId);

        // Update customer details with the new data
        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());

        // Save the updated customer to the repository
        Customer updatedCustomer = customerRepo.save(existingCustomer);

        // Log the successful update
        logger.info("Customer updated successfully: {}", updatedCustomer);

        // Return ResponseEntity with updated customer details
        return ResponseEntity.ok(updatedCustomer);
    }

    /**
     * Deletes a customer from the repository by customer ID.
     * 
     * @param customerId The ID of the customer to delete.
     * @return ResponseEntity indicating success message after deletion.
     */
    @Override
    public ResponseEntity<?> deleteCustomer(Long customerId) {
        // Log the incoming request at debug level
        logger.debug("Received deleteCustomer request for customer ID: {}", customerId);

        // Retrieve the customer data from the repository based on customerId
        Optional<Customer> customerData = customerRepo.findById(customerId);

        // Check if the customer exists
        if (!customerData.isPresent()) {
            // Log error and throw an exception if customer is not found
            logger.error("Customer not found with ID: {}", customerId);
            throw new ApiRequestFailedException("Customer not found with id: " + customerId);
        }

        // Get the existing customer object from Optional
        Customer existingCustomer = customerData.get();

        // Log the successful retrieval of customer details
        logger.info("Customer found and about to be deleted: {}", existingCustomer);

        // Create and save shadow copy of the existing customer for audit purposes
        // createAndSaveCustomerShadow(existingCustomer);

        // Delete the customer from the repository
        customerRepo.deleteById(customerId);

        // Log success message
        logger.info("Customer deleted successfully with ID: {}", customerId);

        // Return ResponseEntity with success message after deletion
        return ResponseEntity.ok("Customer deleted successfully");
    }

    /**
     * Retrieves a customer from the repository by customer ID.
     * Logs the attempt to retrieve the customer, checks if the customer exists,
     * logs the outcome of the retrieval (success or failure), and returns the
     * customer if found. Throws an ApiRequestFailedException if the customer
     * with the specified ID is not found.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return The customer object if found.
     * @throws ApiRequestFailedException If the customer with the specified ID is
     *                                   not found.
     */
    @Override
    public Customer getCustomerById(Long customerId) {
        // Log the attempt to retrieve customer by ID
        logger.debug("Attempting to retrieve customer with ID: {}", customerId);

        // Find customer in repository by ID
        Optional<Customer> customer = customerRepo.findById(customerId);

        // Check if customer exists
        if (customer.isPresent()) {
            // Log successful retrieval
            logger.info("Customer with ID {} found", customerId);
            return customer.get(); // Return the customer if found
        } else {
            // Log failure to find customer
            logger.error("Customer not found with ID: {}", customerId);
            throw new ApiRequestFailedException("Customer not found with id: " + customerId); // Throw exception if
                                                                                              // customer not found
        }
    }

    @Override
    public void generateCustomerPdf(List<Customer> customerList, HttpServletResponse response)
            throws DocumentException, java.io.IOException {
        // Creating the Document instance
        Document document = new Document(PageSize.A4);

        try {
            // Getting an instance of PdfWriter
            PdfWriter.getInstance(document, response.getOutputStream());

            // Opening the document
            document.open();

            // Adding the title
            Font fontTitle = new Font(Font.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("List of Customers", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Adding the customer details table
            addCustomerDetailsToPdf(document, customerList);

            // Closing the document
            document.close();
        } catch (IOException e) {
            // Handle IOException
            logger.error("IOException occurred: {}", e.getMessage());
            throw new DocumentException(e);
        }
    }

    private void addCustomerDetailsToPdf(Document document, List<Customer> customerList)
            throws DocumentException {
        // Creating a table with 4 columns
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // Adding the table header
        addTableHeader(table);

        // Adding customer details to the table
        for (Customer customer : customerList) {
            table.addCell(String.valueOf(customer.getCustomerId()));
            table.addCell(customer.getName());
            table.addCell(customer.getEmail());
            table.addCell(customer.getPhone());
        }

        // Adding the table to the document
        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
        // Adding the table header
        Font fontHeader = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE); // Change Color.WHITE
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(0, 100, 255)); // Use com.lowagie.text.Color
        cell.setPadding(5);

        String[] headers = { "Customer ID", "Full Name", "Email ID", "Mobile No" };
        for (String header : headers) {
            cell.setPhrase(new Phrase(header, fontHeader));
            table.addCell(cell);
        }
    }

}
