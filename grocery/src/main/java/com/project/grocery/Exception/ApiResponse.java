// package com.project.grocery.Exception;

// /**
//  * ApiResponse class is used to standardize the structure of API responses.
//  */
// public class ApiResponse {

//     private String message; // Message to convey the response status or information
//     private boolean success; // Indicates whether the request was successful or not
//     private int statusCode; // HTTP status code for the response
//     private Object data; // Any additional data to include in the response

//     /**
//      * Constructor to create an ApiResponse with a message and success status.
//      * 
//      * @param message The message to convey
//      * @param success The success status of the response
//      */
//     public ApiResponse(String message, boolean success) {
//         this.message = message;
//         this.success = success;
//     }

//     /**
//      * Constructor to create an ApiResponse with a message, success status, and status code.
//      * 
//      * @param message The message to convey
//      * @param success The success status of the response
//      * @param statusCode The HTTP status code for the response
//      */
//     public ApiResponse(String message, boolean success, int statusCode) {
//         this.message = message;
//         this.success = success;
//         this.statusCode = statusCode;
//     }

//     /**
//      * Constructor to create an ApiResponse with a message, success status, status code, and additional data.
//      * 
//      * @param message The message to convey
//      * @param success The success status of the response
//      * @param statusCode The HTTP status code for the response
//      * @param data Any additional data to include in the response
//      */
//     public ApiResponse(String message, boolean success, int statusCode, Object data) {
//         this.message = message;
//         this.success = success;
//         this.statusCode = statusCode;
//         this.data = data;
//     }

//     /**
//      * Gets the message of the response.
//      * 
//      * @return The message of the response
//      */
//     public String getMessage() {
//         return message;
//     }

//     /**
//      * Sets the message of the response.
//      * 
//      * @param message The message to set
//      */
//     public void setMessage(String message) {
//         this.message = message;
//     }

//     /**
//      * Checks if the response is successful.
//      * 
//      * @return True if successful, false otherwise
//      */
//     public boolean isSuccess() {
//         return success;
//     }

//     /**
//      * Sets the success status of the response.
//      * 
//      * @param success The success status to set
//      */
//     public void setSuccess(boolean success) {
//         this.success = success;
//     }

//     /**
//      * Gets the HTTP status code of the response.
//      * 
//      * @return The HTTP status code of the response
//      */
//     public int getStatusCode() {
//         return statusCode;
//     }

//     /**
//      * Sets the HTTP status code of the response.
//      * 
//      * @param statusCode The HTTP status code to set
//      */
//     public void setStatusCode(int statusCode) {
//         this.statusCode = statusCode;
//     }

//     /**
//      * Gets the additional data of the response.
//      * 
//      * @return The additional data of the response
//      */
//     public Object getData() {
//         return data;
//     }

//     /**
//      * Sets the additional data of the response.
//      * 
//      * @param data The additional data to set
//      */
//     public void setData(Object data) {
//         this.data = data;
//     }
// }
package com.project.grocery.Exception;


public class ApiResponse {

    private String message;
    private boolean success;
    private int statusCode;
    private Object data;

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResponse(String message, boolean success, int statusCode) {
        this.message = message;
        this.success = success;
        this.statusCode = statusCode;
    }

    public ApiResponse(String message, boolean success, int statusCode, Object data) {
        this.message = message;
        this.success = success;
        this.statusCode = statusCode;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
