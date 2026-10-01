package vn.iotstar.dto;

import java.io.Serializable;

public class CheckoutForm implements Serializable {
    private static final long serialVersionUID = 1L;
    private String receiverName = "";
    private String phone = "";
    private String address = "";
    private String note = "";

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String value) { receiverName = trim(value); }
    public String getPhone() { return phone; }
    public void setPhone(String value) { phone = trim(value); }
    public String getAddress() { return address; }
    public void setAddress(String value) { address = trim(value); }
    public String getNote() { return note; }
    public void setNote(String value) { note = trim(value); }
    private String trim(String value) { return value == null ? "" : value.trim(); }
}
