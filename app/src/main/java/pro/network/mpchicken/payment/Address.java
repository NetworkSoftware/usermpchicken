package pro.network.mpchicken.payment;

import java.io.Serializable;

public class Address implements Serializable {
    String id;
    String name;
    String address;
    String mobile;
    String alternateMobile;
    String landmark;
    String pincode;
    String comments;
    String latlon;

    public Address() {
    }

    public Address(String name, String address, String mobile, String alternateMobile, String landmark, String pincode, String comments) {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.alternateMobile = alternateMobile;
        this.landmark = landmark;
        this.pincode = pincode;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternateMobile() {
        return alternateMobile;
    }

    public void setAlternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLatlon() {
        return latlon;
    }

    public void setLatlon(String latlon) {
        this.latlon = latlon;
    }
}
