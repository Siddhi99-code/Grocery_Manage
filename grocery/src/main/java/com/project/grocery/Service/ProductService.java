package com.project.grocery.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.lowagie.text.DocumentException;
import com.project.grocery.Entity.Product;

import jakarta.servlet.http.HttpServletResponse;

public interface ProductService {
        // To add the product
        Product addProduct(Product product);

        // To get all products
        List<Product> getAllProducts();

        // to update the products
        ResponseEntity<?> updateProduct(Long productId, Product product);

        // To delte the products
        ResponseEntity<?> deleteProduct(Long productId);

        // To get product by id
        Product getProductById(Long productId); // Add this line
        // To genrate thee pdf

        public void generateProductPdf(List<Product> productList, HttpServletResponse response)
                        throws DocumentException, IOException;

}
