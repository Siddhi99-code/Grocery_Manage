// package com.project.grocery.Exception;


// public class ApiRequestFailedException extends RuntimeException {

//     @SuppressWarnings("unused")
//     private String resourceName;
//     @SuppressWarnings("unused")
//     private String fieldName;
//     @SuppressWarnings("unused")
//     private long fieldValue;
//     private Object details;

//     private static final long serialVersionUID = 1L;

//     // Constructor with resource name and field value
//     public ApiRequestFailedException(String resourceName, long fieldValue) {
//         super(String.format("%s not found with id: %s", resourceName, fieldValue));
//         this.resourceName = resourceName;
//         this.fieldValue = fieldValue;
//     }

//     // Constructor with resource name, field name, and field value
//     public ApiRequestFailedException(String resourceName, String fieldName, long fieldValue) {
//         super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
//         this.resourceName = resourceName;
//         this.fieldName = fieldName;
//         this.fieldValue = fieldValue;
//     }

//     // Constructor with message and details
//     public ApiRequestFailedException(String message, Object details) {
//         super(message);
//         this.details = details;
//     }

//     // Constructor with only message
//     public ApiRequestFailedException(String message) {
//         super(message);
//     }

//     public Object getDetails() {
//         return details;
//     }
// }

package com.project.grocery.Exception;

public class ApiRequestFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String resourceName;
    private final String fieldName;
    private final long fieldValue;
    private final Object details;

    public ApiRequestFailedException(String resourceName, long fieldValue) {
        super(String.format("%s not found with id: %s", resourceName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = null;
        this.fieldValue = fieldValue;
        this.details = null;
    }

    public ApiRequestFailedException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.details = null;
    }

    public ApiRequestFailedException(String message, Object details) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = 0;
        this.details = details;
    }

    public ApiRequestFailedException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = 0;
        this.details = null;
    }

    public Object getDetails() {
        return details;
    }
}
