package auth;

/**
 * Data holder class for appointment information
 * Captures all form inputs and booking details
 */
public class AppointmentData {
    private String fullName;
    private String email;
    private String contactNumber;
    private String carModel;
    private String carPlate;
    private String serviceAddOn;
    private String remarks;
    
    // These are set during the booking process
    private String customerID;
    private String serviceType; // "Normal" or "Major"
    private String bookingDate;
    private String startTime;
    private String endTime;
    private String appointmentID;

    public AppointmentData(String fullName, String email, String contactNumber, 
                          String carModel, String carPlate, String serviceAddOn, String remarks) {
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.carModel = carModel;
        this.carPlate = carPlate;
        this.serviceAddOn = serviceAddOn;
        this.remarks = remarks;
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }
    public String getCarModel() { return carModel; }
    public String getCarPlate() { return carPlate; }
    public String getServiceAddOn() { return serviceAddOn; }
    public String getRemarks() { return remarks; }
    
    public String getCustomerID() { return customerID; }
    public void setCustomerID(String customerID) { this.customerID = customerID; }
    
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    
    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    
    public String getAppointmentID() { return appointmentID; }
    public void setAppointmentID(String appointmentID) { this.appointmentID = appointmentID; }
}
