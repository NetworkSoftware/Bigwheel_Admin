package pro.network.bringwheeladmin.shop;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class StockList implements Serializable {
  public String id;
  public String ownertype;
  public String subcategory;
  public String productName;
  public String price;
  public String mrprice;
  public String discountName;
  public String totalstock;
  public String description;
  public String image;
  public String offers;
  public String secondcategory;
  public String sizes;
  public String shopid;
  public String shipCost;
  public String qty;
  public String stock_update;
  public String userId;
  int type;
    public StockList() {
    }

    public StockList(String ownertype, String description,String image,String subcategory, String productName, String price, String mrprice, String discountName, String totalstock,String offers) {
        this.ownertype = ownertype;
        this.subcategory = subcategory;
        this.productName = productName;
        this.price = price;
        this.mrprice = mrprice;
        this.discountName = discountName;
        this.totalstock = totalstock;
        this.description=description;
        this.image=image;
        this.offers=offers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnertype() {
        return ownertype;
    }

    public void setOwnertype(String ownertype) {
        this.ownertype = ownertype;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMrprice() {
        return mrprice;
    }

    public void setMrprice(String mrprice) {
        this.mrprice = mrprice;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getTotalstock() {
        return totalstock;
    }

    public void setTotalstock(String totalstock) {
        this.totalstock = totalstock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public String getSecondcategory() {
        return secondcategory;
    }

    public String getSizes() {
        return sizes;
    }

    public void setSizes(String sizes) {
        this.sizes = sizes;
    }

    public String getShipCost() {
        return shipCost;
    }

    public void setShipCost(String shipCost) {
        this.shipCost = shipCost;
    }

    public void setSecondcategory(String secondcategory) {
        this.secondcategory = secondcategory;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getStock_update() {
        return stock_update;
    }

    public void setStock_update(String stock_update) {
        this.stock_update = stock_update;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}