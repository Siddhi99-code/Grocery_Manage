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
import com.project.grocery.Entity.Product;
import com.project.grocery.Exception.ApiRequestFailedException;
import com.project.grocery.Repository.ProductRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepo;

    // @Autowired
    // private ProductShadowRepository productShadowRepo;

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    /**
     * Adds a new product to the repository.
     * Logs the addition of the product and returns the added product if successful.
     * Throws an ApiRequestFailedException if an error occurs during addition.
     *
     * @param product The product object to be added.
     * @return The product object that was added.
     * @throws ApiRequestFailedException If an error occurs while adding the
     *                                   product.
     */
    @Override
    public Product addProduct(Product product) {
        try {
            // Save the product to the repository
            Product addedProduct = productRepo.save(product);

            // Log the successful addition of the product
            logger.info("Product added successfully: {}", addedProduct);

            // Return the added product
            return addedProduct;
        } catch (Exception e) {
            // Log the error if addition fails
            logger.error("Failed to add product: {}", e.getMessage());

            // Throw custom exception with appropriate message
            throw new ApiRequestFailedException("Failed to add product", e.getMessage());
        }
    }

    @Override
    /**
     * Retrieves all products from the repository.
     * 
     * @return A list containing all products retrieved from the repository.
     */
    public List<Product> getAllProducts() {
        // Retrieve all products from the repository
        return productRepo.findAll();
    }

    /**
     * Updates an existing product in the repository.
     * Retrieves the product by productId, updates its details with new data,
     * saves the updated product, and logs the update operation.
     *
     * @param productId The ID of the product to be updated.
     * @param product   The updated product object containing new data.
     * @return ResponseEntity containing the updated product details if successful.
     * @throws ApiRequestFailedException If the product with the specified ID is not
     *                                   found.
     */
    @Override
    public ResponseEntity<?> updateProduct(Long productId, Product product) {
        // Retrieve the product data from the repository based on productId
        Optional<Product> productData = productRepo.findById(productId);

        // Check if the product exists
        if (!productData.isPresent()) {
            // Throw an exception if product is not found
            throw new ApiRequestFailedException("Product not found with id: " + productId);
        }

        // Get the existing product object from Optional
        Product existingProduct = productData.get();

        // Create and save shadow copy of the existing product for audit purposes
        // createAndSaveProductShadow(existingProduct);

        // Update product details with the new data
        existingProduct.setName(product.getName());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());

        // Save the updated product to the repository
        Product updatedProduct = productRepo.save(existingProduct);

        // Log the successful update
        logger.info("Product updated successfully: {}", updatedProduct);

        // Return ResponseEntity with updated product details
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Deletes a product from the repository based on the provided productId.
     * Logs the request, retrieves the product, creates and saves a shadow copy,
     * deletes the product from the repository, and returns a response entity
     * indicating success.
     *
     * @param productId The ID of the product to delete.
     * @return ResponseEntity indicating success message after deletion.
     * @throws ApiRequestFailedException If the product with the specified ID is not
     *                                   found.
     */
    @Override
    public ResponseEntity<?> deleteProduct(Long productId) {
        // Log the incoming request
        logger.debug("Received deleteProduct request for product ID: {}", productId);

        // Retrieve the product data from the repository based on productId
        Optional<Product> productData = productRepo.findById(productId);

        // Check if the product exists
        if (!productData.isPresent()) {
            // Throw an exception if product is not found
            throw new ApiRequestFailedException("Product not found with id: " + productId);
        }

        // Get the existing product object from Optional
        // Product existingProduct = productData.get();

        // Create and save shadow copy of the existing product for audit purposes
        // createAndSaveProductShadow(existingProduct);

        // Delete the product from the repository
        productRepo.deleteById(productId);

        // Log success message
        logger.info("Product deleted successfully with ID: {}", productId);

        // Return ResponseEntity with success message after deletion
        return ResponseEntity.ok("Product deleted successfully");
    }

    // private void createAndSaveProductShadow(Product existingProduct) {
    //     ProductShadow productShadow = new ProductShadow();
    //     productShadow.setProductid(existingProduct.getProductid());
    //     productShadow.setName(existingProduct.getName());
    //     productShadow.setQuantity(existingProduct.getQuantity());
    //     productShadow.setPrice(existingProduct.getPrice());
    //     productShadowRepo.save(productShadow);
    // }
/*
 * Retrive the product by id
 */
    @Override
    public Product getProductById(Long productId) {
        /*
         * Retrieve an Optional containing the product with the specified ID from the product repository.
         * If a product with the given ID exists, it will be present in the Optional.
         */
        Optional<Product> product = productRepo.findById(productId);
    
        /*
         * Check if the Optional contains a product.
         * If a product is present, return the product.
         * Otherwise, throw an ApiRequestFailedException with a message indicating that the product was not found.
         */
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new ApiRequestFailedException("Product not found with id: " + productId);
        }
    }
    
@Override
    public void generateProductPdf(List<Product> productList, HttpServletResponse response)
            throws DocumentException, IOException {
        // Creating the Document instance
        Document document = new Document(PageSize.A4);

        try {
            // Getting an instance of PdfWriter
            PdfWriter.getInstance(document, response.getOutputStream());

            // Opening the document
            document.open();

            // Adding the title
            Font fontTitle = new Font(Font.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("List of Products", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Adding the product details table
            addProductDetailsToPdf(document, productList);

            // Closing the document
            document.close();
        } catch (IOException e) {
            // Handle IOException
            logger.error("IOException occurred: {}", e.getMessage());
            throw new DocumentException(e);
        }
    }

    private void addProductDetailsToPdf(Document document, List<Product> productList)
            throws DocumentException {
        // Creating a table with 4 columns
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // Adding the table header
        addTableHeader(table);

        // Adding product details to the table
        for (Product product : productList) {
            table.addCell(String.valueOf(product.getProductid()));
            table.addCell(product.getName());
            table.addCell(String.valueOf(product.getQuantity()));
            table.addCell(String.valueOf(product.getPrice()));
        }

        // Adding the table to the document
        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
        // Adding the table header
        Font fontHeader = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(0, 100, 255));
        cell.setPadding(5);

        String[] headers = { "Product ID", "Name", "Quantity", "Price" };
        for (String header : headers) {
            cell.setPhrase(new Phrase(header, fontHeader));
            table.addCell(cell);
        }
    }
}
