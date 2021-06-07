package pro.network.bringwheeladmin.shop;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class Shop implements Serializable {
    String id;
    String shopName;
    String gstNo;
    String productName;
    String phoneone;
    String phonetwo;
    String area;
    String buildingName;
    String landmark;
    String pincode;
    String city;
    String state;
    String status;
    String description;
    String password;

    public Shop() {
    }

    public Shop(String shopName, String gstNo, String productName, String phoneone, String phonetwo, String area, String buildingName, String landmark, String pincode, String city, String state, String status, String description, String password) {
        this.shopName = shopName;
        this.gstNo = gstNo;
        this.productName = productName;
        this.phoneone = phoneone;
        this.phonetwo = phonetwo;
        this.area = area;
        this.buildingName = buildingName;
        this.landmark = landmark;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.status = status;
        this.description = description;
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhoneone() {
        return phoneone;
    }

    public void setPhoneone(String phoneone) {
        this.phoneone = phoneone;
    }

    public String getPhonetwo() {
        return phonetwo;
    }

    public void setPhonetwo(String phonetwo) {
        this.phonetwo = phonetwo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}