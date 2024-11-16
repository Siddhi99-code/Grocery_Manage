package com.project.grocery.Controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.project.grocery.Entity.Product;
import com.project.grocery.Exception.ApiResponse;
import com.project.grocery.Service.ProductService;
import com.project.grocery.utils.StatusMessageUtil;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ProductController {

        private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

        @Autowired
        private ProductService productService;

        @PostMapping("/product")
        public ResponseEntity<?> createProduct(@Validated @RequestBody Product product) {
                // Log the incoming request with product details
                logger.debug("Received createProduct request with product: {}", product);

                // Call service layer to add the product
                Product addedProduct = productService.addProduct(product);

                // Log successful addition of the product
                logger.info("Product added successfully: {}", addedProduct);

                // Prepare response with added product details
                ApiResponse response = new ApiResponse(
                                StatusMessageUtil.getStatusMessage("product_added_success"), true,
                                HttpStatus.OK.value(), addedProduct);

                // Return ResponseEntity with success status and added product details
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/products")
        public ResponseEntity<?> getAllProducts() {
                // Log the incoming request
                logger.debug("Received getAllProducts request");

                // Retrieve all products from the service
                List<Product> products = productService.getAllProducts();

                // Log successful retrieval
                logger.info("Retrieved all products successfully");

                // Prepare response with retrieved products
                ApiResponse response = new ApiResponse(
                                StatusMessageUtil.getStatusMessage("products_retrieved_success"), // Success message
                                true, // Operation status
                                HttpStatus.OK.value(), // HTTP status code
                                products); // List of products

                // Return ResponseEntity with response body and HTTP status OK
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/product/{productId}")
        public ResponseEntity<?> getProductById(@PathVariable Long productId) {
                // Log the request received for product ID
                logger.debug("Received getProductById request for product ID: {}", productId);

                // Retrieve the product details from the service layer
                Product product = productService.getProductById(productId);

                // Log the successful retrieval of product details
                logger.info("Retrieved product with ID {}: {}", productId, product);

                // Prepare response with retrieved product details
                ApiResponse response = new ApiResponse(
                                StatusMessageUtil.getStatusMessage("product_retrieved_success"), // Retrieve success
                                                                                                 // message
                                true, // Indicate operation success
                                HttpStatus.OK.value(), // Set HTTP status code to 200 OK
                                product // Include retrieved product in the response body
                );

                // Return ResponseEntity with success status and product details
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @PutMapping("/product/{productId}")
        public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
                logger.debug("Received updateProduct request for product ID: {}", productId); // Log the request

                ResponseEntity<?> updatedProductResponse = productService.updateProduct(productId, product); // Update
                                                                                                             // the
                                                                                                             // product

                logger.info("Product details updated successfully for product ID: {}", productId); // Log success

                // Prepare response with updated product details
                ApiResponse response = new ApiResponse(
                                StatusMessageUtil.getStatusMessage("product_updated_success"), // Success message
                                true, // Operation success indicator
                                HttpStatus.OK.value(), // HTTP status code
                                updatedProductResponse.getBody()); // Updated product details from response

                return new ResponseEntity<>(response, HttpStatus.OK); // Return success response
        }

        @DeleteMapping("/product/{productId}") // Ensure the path variable name matches the method parameter
        public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
                logger.debug("Received deleteProduct request for product ID: {}", productId); // Log the request
                ResponseEntity<?> response; // Initialize response

                ResponseEntity<?> newResponse = productService.deleteProduct(productId); // Delete the product
                logger.info("Product with ID {} deleted successfully", productId); // Log success
                response = new ResponseEntity<>(
                                new ApiResponse(StatusMessageUtil.getStatusMessage("product_deleted_success"),
                                                true,
                                                HttpStatus.OK.value(), newResponse.getBody()),
                                HttpStatus.OK); // Return success response

                return response; // Return the response
        }

        @GetMapping("/products/v1")
        public void generateProductPdf(HttpServletResponse response) {
                try {
                        // Set the content type and attachment header
                        response.setContentType("application/pdf");
                        response.setHeader("Content-Disposition", "attachment; filename=products.pdf");

                        // Retrieve the list of products from the service layer
                        List<Product> productList = productService.getAllProducts();

                        // Log the request to generate the PDF
                        logger.debug("Received request to generate PDF for products");

                        // Generate the PDF using the provided list of products
                        productService.generateProductPdf(productList, response);

                        // Log successful PDF generation
                        logger.info("Successfully generated PDF for products");

                } catch (DocumentException | IOException e) {
                        // Log the exception
                        logger.error("Error occurred while generating PDF: {}", e.getMessage());
                        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
        }
}
