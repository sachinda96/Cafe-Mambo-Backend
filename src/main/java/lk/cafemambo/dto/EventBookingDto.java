package lk.cafemambo.dto;

import java.util.Date;

public class EventBookingDto {

    private String id;
    private String userId;
    private Date bookDate;
    private String packageId;
    private DeliveryDto delivery;
    private PaymentDto payment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public DeliveryDto getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryDto delivery) {
        this.delivery = delivery;
    }

    public PaymentDto getPayment() {
        return payment;
    }

    public void setPayment(PaymentDto payment) {
        this.payment = payment;
    }
}
