package com.project.grocery.Service;


import com.lowagie.text.Chunk;

// import com.lowagie.text.Document;
// import com.lowagie.text.DocumentException;
// import com.lowagie.text.Paragraph;
// import com.lowagie.text.pdf.PdfWriter;
// import com.project.grocery.Entity.Bill;
// import com.project.grocery.Entity.BillProduct;
// import com.project.grocery.Entity.Customer;
// import com.project.grocery.Entity.Product;
// import com.project.grocery.Repository.BillRepository;
// import com.project.grocery.Repository.CustomerRepository;
// import com.project.grocery.Repository.ProductRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.io.FileNotFoundException;
// import java.io.FileOutputStream;
// import java.text.SimpleDateFormat;
// import java.util.*;

// @Service
// public class BillService {

//     @Autowired
//     private BillRepository billRepository;

//     @Autowired
//     private ProductRepository productRepository;

//     @Autowired
//     private CustomerRepository customerRepository;

//     @Autowired
//     private ProductService productService;

//     @Autowired
//     private CustomerService customerService;
//     private static final String PDF_DIRECTORY = "/home/siddhia/siddhiii/Bills";


//     public Map<String, Object> generateBill(Bill billRequest) {
//         Long customerId = billRequest.getCustomer().getCustomerId();
//         List<BillProduct> products = billRequest.getBillProducts();

//         Customer customer = customerService.getCustomerById(customerId);
//         Bill bill = createBill(customer, products);
//         double totalBillAmount = calculateTotalBillAmount(bill.getBillProducts());
//         try {
//             generateBillPDF(bill);
//         } catch (FileNotFoundException | DocumentException e) {
//             e.printStackTrace(); // Handle the exception appropriately
//         }

//         return constructResponse(bill, customer, totalBillAmount);
//     }

//     private Bill createBill(Customer customer, List<BillProduct> products) {
//         Bill bill = new Bill();
//         bill.setBillDate(new Date());
//         bill.setCustomer(customer);

//         List<BillProduct> billProducts = new ArrayList<>();
//         for (BillProduct billProduct : products) {
//             Long productId = billProduct.getProduct().getProductid();
//             int quantity = billProduct.getQuantity();

//             Product product = productService.getProductById(productId);
//             if (product.getQuantity() < quantity) {
//                 throw new RuntimeException("Insufficient quantity for product: " + product.getName());
//             }//make catch block
//             // Deduct quantity from product table
//             product.setQuantity(product.getQuantity() - quantity);
//             productRepository.save(product);

//             // Create bill product
//             BillProduct newBillProduct = new BillProduct();
//             newBillProduct.setBill(bill);
//             newBillProduct.setProduct(product);
//             newBillProduct.setProductName(product.getName());
//             newBillProduct.setQuantity(quantity);
//             newBillProduct.setPrice(product.getPrice());

//             billProducts.add(newBillProduct);
//         }

//         // Set bill products and total bill amount
//         bill.setBillProducts(billProducts);
//         bill.setBillAmount(calculateTotalBillAmount(billProducts));

//         // Save bill
//         return billRepository.save(bill);
//     }

//     private double calculateTotalBillAmount(List<BillProduct> billProducts) {
//         return billProducts.stream()
//                 .mapToDouble(bp -> bp.getQuantity() * bp.getPrice())
//                 .sum();
//     }

//     private Map<String, Object> constructResponse(Bill bill, Customer customer, double totalBillAmount) {
//         Map<String, Object> response = new HashMap<>();
//         response.put("billno", bill.getBillId());
//         response.put("billdate", formatDate(bill.getBillDate()));
//         response.put("customername", customer.getName());

//         List<Map<String, Object>> productsList = new ArrayList<>();
//         for (BillProduct billProduct : bill.getBillProducts()) {
//             Map<String, Object> productMap = new HashMap<>();
//             productMap.put("productid", billProduct.getProduct().getProductid());
//             productMap.put("name", billProduct.getProductName());
//             productMap.put("qty", billProduct.getQuantity());
//             productMap.put("price", billProduct.getPrice());
//             productMap.put("billprice", billProduct.getQuantity() * billProduct.getPrice());
//             productsList.add(productMap);
//         }

//         response.put("products", productsList);
//         response.put("billamount", totalBillAmount);

//         return response;
//     }

//     // Utility method to format Date object as String
//     private String formatDate(Date date) {
//         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//         return sdf.format(date);
//     }

//     private void generateBillPDF(Bill bill) throws FileNotFoundException, DocumentException {
//         Document document = new Document();
//         PdfWriter.getInstance(document, new FileOutputStream(PDF_DIRECTORY + "/" + bill.getBillId() + ".pdf"));
//         document.open();

//         document.add(new Paragraph("Bill Details"));
//         document.add(new Paragraph("\n"));

//         // Write bill details to PDF
//         document.add(new Paragraph("Bill ID: " + bill.getBillId()));
//         document.add(new Paragraph("Bill Date: " + formatDate(bill.getBillDate())));
//         document.add(new Paragraph("Customer Name: " + bill.getCustomer().getName()));

//         // Write bill products to PDF
//         List<BillProduct> billProducts = bill.getBillProducts();
//         for (BillProduct billProduct : billProducts) {
//             document.add(new Paragraph("Product Name: " + billProduct.getProductName()));
//             document.add(new Paragraph("Quantity: " + billProduct.getQuantity()));
//             document.add(new Paragraph("Price: " + billProduct.getPrice()));
//             document.add(new Paragraph("\n"));
//         }
//         document.add(new Paragraph("\n"));

//         document.add(new Paragraph("Total Bill Amount: " + bill.getBillAmount()));
//         document.close();
//     }
// }
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;

import java.awt.Color;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

import com.project.grocery.Entity.Bill;
import com.project.grocery.Entity.BillProduct;
import com.project.grocery.Entity.Customer;
import com.project.grocery.Entity.Product;
import com.project.grocery.Repository.BillRepository;
import com.project.grocery.Repository.ProductRepository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import java.io.*;
import java.util.*;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    private static final String PDF_DIRECTORY = "/home/siddhia/siddhiii/Bills";
    private static final String EXCEL_PATH = "/home/siddhia/siddhiii/Bills/BillData.xlsx";

    public Map<String, Object> generateBill(Bill billRequest) {
        Long customerId = billRequest.getCustomer().getCustomerId();
        List<BillProduct> products = billRequest.getBillProducts();
        // Fetch customer information
        Customer customer = customerService.getCustomerById(customerId);
                // Create a new bill based on the provided customer and products
        Bill bill = createBill(customer, products);
                // Calculate the total bill amount
        double totalBillAmount = calculateTotalBillAmount(bill.getBillProducts());
                // Generate PDF and update the Excel sheet with the bill details
        try {
            generateBillPDF(bill);
            appendToBillExcel(bill);
        } catch (DocumentException | IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        // Construct the response to be returned
        return constructResponse(bill, customer, totalBillAmount);
    }

    private Bill createBill(Customer customer, List<BillProduct> products) {
        // Create a new Bill object and set the current date and customer information
        Bill bill = new Bill();
        bill.setBillDate(new Date());
        bill.setCustomer(customer);
    
        // Initialize a list to hold the bill products
        List<BillProduct> billProducts = new ArrayList<>();
    
        // Loop through each product in the provided list of bill products
        for (BillProduct billProduct : products) {
            // Retrieve the product ID and quantity from the current bill product
            Long productId = billProduct.getProduct().getProductid();
            int quantity = billProduct.getQuantity();
    
            // Fetch the product details using the product ID
            Product product = productService.getProductById(productId);
    
            // Check if the product has sufficient quantity in stock
            if (product.getQuantity() < quantity) {
                // Log the error instead of throwing the exception
                System.out.println("Insufficient quantity for product: " + product.getName());
                // You can log more details if needed, like the available quantity
                // System.out.println("Available quantity: " + product.getQuantity());
                // Skip processing this product and continue with the next one
                continue;
            }
    
            // Deduct the requested quantity from the product's stock
            product.setQuantity(product.getQuantity() - quantity);
    
            // Save the updated product information to the repository
            productRepository.save(product);
    
            // Create a new BillProduct object and set its details
            BillProduct newBillProduct = new BillProduct();
            newBillProduct.setBill(bill);
            newBillProduct.setProduct(product);
            newBillProduct.setProductName(product.getName());
            newBillProduct.setQuantity(quantity);
            newBillProduct.setPrice(product.getPrice());
    
            // Add the new BillProduct to the list of bill products
            billProducts.add(newBillProduct);
        }
    
        // Set the list of bill products and the total bill amount for the bill
        bill.setBillProducts(billProducts);
        bill.setBillAmount(calculateTotalBillAmount(billProducts));
    
        // Save the bill to the repository and return the saved bill
        return billRepository.save(bill);
    }
    
    
    private double calculateTotalBillAmount(List<BillProduct> billProducts) {
        // Use Java Streams to calculate the total bill amount
        return billProducts.stream()
                // Map each BillProduct to its total price (quantity * price)
                .mapToDouble(bp -> bp.getQuantity() * bp.getPrice())
                // Sum all the mapped values to get the total bill amount
                .sum();
    }
    

    private Map<String, Object> constructResponse(Bill bill, Customer customer, double totalBillAmount) {
        // Create a new HashMap to hold the response data
        Map<String, Object> response = new HashMap<>();
        
        // Add the bill number to the response map
        response.put("billno", bill.getBillId());
        
        // Format and add the bill date to the response map
        response.put("billdate", formatDate(bill.getBillDate()));
        
        // Add the customer's name to the response map
        response.put("customername", customer.getName());
    
        // Create a list to hold the details of each product in the bill
        List<Map<String, Object>> productsList = new ArrayList<>();
        
        // Iterate through each BillProduct in the bill
        for (BillProduct billProduct : bill.getBillProducts()) {
            // Create a new HashMap to hold the details of a single product
            Map<String, Object> productMap = new HashMap<>();
            
            // Add the product ID to the product map
            productMap.put("productid", billProduct.getProduct().getProductid());
            
            // Add the product name to the product map
            productMap.put("name", billProduct.getProductName());
            
            // Add the quantity of the product to the product map
            productMap.put("qty", billProduct.getQuantity());
            
            // Add the price of the product to the product map
            productMap.put("price", billProduct.getPrice());
            
            // Calculate and add the total price for the product (quantity * price) to the product map
            productMap.put("billprice", billProduct.getQuantity() * billProduct.getPrice());
            
            // Add the product map to the list of products
            productsList.add(productMap);
        }
    
        // Add the list of products to the response map
        response.put("products", productsList);
        
        // Add the total bill amount to the response map
        response.put("billamount", totalBillAmount);
    
        // Return the constructed response map
        return response;
    }
    

    
    public static void generateBillPDF(Bill bill) throws FileNotFoundException, DocumentException {
        // Setting up the document with A4 size and margins
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF_DIRECTORY + "/" + bill.getBillId() + ".pdf"));
    
        // Open the document to start adding content
        document.open();
    
        // Adding a border to the PDF
        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect = new Rectangle(36, 36, 559, 806);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(2);
        rect.setBorderColor(new Color(0, 0, 0));
        canvas.rectangle(rect);
    
        // Adding the shop name at the top of the document
        Font shopNameFont = new Font(Font.HELVETICA, 24, Font.BOLD, new Color(0, 0, 255));
        Paragraph shopName = new Paragraph("Big Mart", shopNameFont);
        shopName.setAlignment(Element.ALIGN_CENTER);
        document.add(shopName);
    
        // Adding a horizontal line below the shop name
        LineSeparator ls = new LineSeparator();
        ls.setLineWidth(2);
        document.add(new Chunk(ls));
        document.add(new Paragraph("\n")); // Adding a newline for spacing
    
        // Adding the Bill Details section title
        Font billDetailsFont = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(0, 0, 0));
        Paragraph billDetails = new Paragraph("Bill Details", billDetailsFont);
        billDetails.setAlignment(Element.ALIGN_CENTER);
        document.add(billDetails);
        document.add(new Paragraph("\n")); // Adding a newline for spacing
    
        // Adding customer and bill information
        Font infoFont = new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(0, 0, 0));
        document.add(new Paragraph("Bill ID: " + bill.getBillId(), infoFont));
        document.add(new Paragraph("Bill Date: " + formatDate(bill.getBillDate()), infoFont));
        document.add(new Paragraph("Customer Name: " + bill.getCustomer().getName(), infoFont));
        document.add(new Paragraph("\n")); // Adding a newline for spacing
    
        // Creating a table to display bill products with 3 columns
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100); // Setting table width to 100% of the page
        table.setSpacingBefore(10f); // Space before the table
        table.setSpacingAfter(10f);  // Space after the table
    
        // Adding table headers with white text on a gray background
        Font tableHeaderFont = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(255, 255, 255));
        PdfPCell header1 = new PdfPCell(new Paragraph("Product Name", tableHeaderFont));
        PdfPCell header2 = new PdfPCell(new Paragraph("Quantity", tableHeaderFont));
        PdfPCell header3 = new PdfPCell(new Paragraph("Price", tableHeaderFont));
    
        header1.setBackgroundColor(new Color(128, 128, 128));
        header2.setBackgroundColor(new Color(128, 128, 128));
        header3.setBackgroundColor(new Color(128, 128, 128));
    
        table.addCell(header1);
        table.addCell(header2);
        table.addCell(header3);
    
        // Adding product details to the table
        List<BillProduct> billProducts = bill.getBillProducts();
        Font tableBodyFont = new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(0, 0, 0));
        for (BillProduct billProduct : billProducts) {
            table.addCell(new PdfPCell(new Paragraph(billProduct.getProductName(), tableBodyFont)));
            table.addCell(new PdfPCell(new Paragraph(String.valueOf(billProduct.getQuantity()), tableBodyFont)));
            table.addCell(new PdfPCell(new Paragraph(String.valueOf(billProduct.getPrice()), tableBodyFont)));
        }
    
        // Adding the table to the document
        document.add(table);
        document.add(new Paragraph("\n")); // Adding a newline for spacing
    
        // Adding the total bill amount at the bottom, aligned to the right
        Font totalAmountFont = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(255, 0, 0));
        Paragraph totalAmount = new Paragraph("Total Bill Amount: " + bill.getBillAmount(), totalAmountFont);
        totalAmount.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalAmount);
    
        // Closing the document
        document.close();
    }
    

    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
   


    // private void appendToBillExcel(Bill bill) throws IOException {
    //     Workbook workbook; // Workbook object for Excel file
    //     Sheet sheet; // Sheet object for Excel file
    //     File excelFile = new File(EXCEL_PATH); // Excel file path
    //     // Check if the Excel file already exists
    //     if (excelFile.exists()) {
    //         // If the file exists, open it for reading
    //         FileInputStream fis = new FileInputStream(excelFile);
    //         workbook = new XSSFWorkbook(fis); // Initialize workbook with existing file
    //         sheet = workbook.getSheetAt(0); // Get the first sheet
    //         fis.close(); // Close the input stream
    //     } else {
    //         // If the file doesn't exist, create a new workbook and sheet
    //         workbook = new XSSFWorkbook(); // Create a new workbook
    //         sheet = workbook.createSheet("Bills"); // Create a new sheet named "Bills"    
    //         // Create header row for the sheet
    //         Row header = sheet.createRow(0); // Create a new row at index 0
    //         header.createCell(0).setCellValue("Bill ID"); // Set cell value for column 0
    //         header.createCell(1).setCellValue("Bill Date"); // Set cell value for column 1
    //         header.createCell(2).setCellValue("Customer ID"); // Set cell value for column 2
    //         header.createCell(3).setCellValue("Customer Name"); // Set cell value for column 3
    //         header.createCell(4).setCellValue("Product ID"); // Set cell value for column 4
    //         header.createCell(5).setCellValue("Product Name"); // Set cell value for column 5
    //         header.createCell(6).setCellValue("Quantity"); // Set cell value for column 6
    //         header.createCell(7).setCellValue("Unit Price"); // Set cell value for column 7
    //         header.createCell(8).setCellValue("Product Total"); // Set cell value for column 8
    //         header.createCell(9).setCellValue("Bill Amount"); // Set cell value for column 9
    //     }
    //     // Get the index of the last row in the sheet
    //     int lastRow = sheet.getLastRowNum();
    //     boolean isFirstRow = true; // Flag to track if it's the first row
    //     // Iterate through each bill product and append to the Excel sheet
    //     for (BillProduct billProduct : bill.getBillProducts()) {
    //         Row row = sheet.createRow(++lastRow); // Create a new row
    //         // If it's the first row, set bill ID, date, customer ID, customer name, and bill amount
    //         if (isFirstRow) {
    //             row.createCell(0).setCellValue(bill.getBillId()); // Set bill ID in column 0
    //             row.createCell(1).setCellValue(formatDate(bill.getBillDate())); // Set bill date in column 1
    //             row.createCell(2).setCellValue(bill.getCustomer().getCustomerId()); // Set customer ID in column 2
    //             row.createCell(3).setCellValue(bill.getCustomer().getName()); // Set customer name in column 3
    //             row.createCell(9).setCellValue(bill.getBillAmount()); // Set bill amount in column 9
    //             isFirstRow = false; // Set the flag to false after processing the first row
    //         }
    //         // Set product ID, name, quantity, price, and total price for each product
    //         row.createCell(4).setCellValue(billProduct.getProduct().getProductid()); // Set product ID in column 4
    //         row.createCell(5).setCellValue(billProduct.getProductName()); // Set product name in column 5
    //         row.createCell(6).setCellValue(billProduct.getQuantity()); // Set quantity in column 6
    //         row.createCell(7).setCellValue(billProduct.getPrice()); // Set price in column 7
    //         row.createCell(8).setCellValue(billProduct.getQuantity() * billProduct.getPrice()); // Set total price in column 8
    //     }
    //     // Write the workbook to the Excel file
    //     FileOutputStream fileOut = new FileOutputStream(EXCEL_PATH); // Create an output stream for the Excel file
    //     workbook.write(fileOut); // Write the workbook to the output stream
    //     fileOut.close(); // Close the output stream
    //     workbook.close(); // Close the workbook
    // }


    private void appendToBillExcel(Bill bill) throws IOException {
        Workbook workbook;
        Sheet sheet;
        File excelFile = new File(EXCEL_PATH);
    
        if (excelFile.exists()) {
            FileInputStream fis = new FileInputStream(excelFile);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Bills");
    
            Row header = sheet.createRow(0);
            String[] headers = {"Bill ID", "Bill Date", "Customer ID", "Customer Name", "Product ID", "Product Name", "Quantity", "Unit Price", "Product Total", "Bill Amount"};
    
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
    
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
        }
    
        int lastRow = sheet.getLastRowNum();
        boolean isFirstRow = true;
    
        CellStyle centerAlignStyle = workbook.createCellStyle();
        centerAlignStyle.setAlignment(HorizontalAlignment.CENTER);
    
        for (BillProduct billProduct : bill.getBillProducts()) {
            Row row = sheet.createRow(++lastRow);
    
            if (isFirstRow) {
                createCenteredCell(row, 0, bill.getBillId(), centerAlignStyle);
                createCenteredCell(row, 1, formatDate(bill.getBillDate()), centerAlignStyle);
                createCenteredCell(row, 2, bill.getCustomer().getCustomerId(), centerAlignStyle);
                createCenteredCell(row, 3, bill.getCustomer().getName(), centerAlignStyle);
                createCenteredCell(row, 9, bill.getBillAmount(), centerAlignStyle);
                isFirstRow = false;
            }
    
            createCenteredCell(row, 4, billProduct.getProduct().getProductid(), centerAlignStyle);
            createCenteredCell(row, 5, billProduct.getProductName(), centerAlignStyle);
            createCenteredCell(row, 6, billProduct.getQuantity(), centerAlignStyle);
            createCenteredCell(row, 7, billProduct.getPrice(), centerAlignStyle);
            createCenteredCell(row, 8, billProduct.getQuantity() * billProduct.getPrice(), centerAlignStyle);
        }
    
        // Autosize columns
        for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
        }
    
        FileOutputStream fileOut = new FileOutputStream(EXCEL_PATH);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
    
    private void createCenteredCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        }
        cell.setCellStyle(style);
    }
    
}    