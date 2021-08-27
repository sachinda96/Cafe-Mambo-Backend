package lk.cafemambo.dto;

import java.util.List;

public class ShopOrderDto {

    private String id;
    private Integer tableId;
    private String customerName;
    private String contactNumber;
    private List<ItemDto> itemDtoList;
    private PaymentDto paymentDto;
    private Double price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public List<ItemDto> getItemDtoList() {
        return itemDtoList;
    }

    public void setItemDtoList(List<ItemDto> itemDtoList) {
        this.itemDtoList = itemDtoList;
    }

    public PaymentDto getPaymentDto() {
        return paymentDto;
    }

    public void setPaymentDto(PaymentDto paymentDto) {
        this.paymentDto = paymentDto;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
