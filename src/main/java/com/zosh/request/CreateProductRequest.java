package com.zosh.request;

import com.zosh.model.Size;

import java.util.HashSet;
import java.util.Set;

public class CreateProductRequest {

    private String title;
    private String description;
    private int price;

    private int discountedPrice;
    private int discountPersent;

    private int quantity;

    private String brand;

    private String color;

    private Set<Size>size=new HashSet<>();

    private String imageUrl;

    private String toplavelCategory;
    private String secondlavelCategory;
    private String thirdlavelCategory;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(int discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public int getDiscountPersent() {
        return discountPersent;
    }

    public void setDiscountPersent(int discountPersent) {
        this.discountPersent = discountPersent;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<Size> getSize() {
        return size;
    }

    public void setSize(Set<Size> size) {
        this.size = size;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getToplavelCategory() {
        return toplavelCategory;
    }

    public void setToplavelCategory(String toplavelCategory) {
        this.toplavelCategory = toplavelCategory;
    }

    public String getSecondlavelCategory() {
        return secondlavelCategory;
    }

    public void setSecondlavelCategory(String secondlavelCategory) {
        this.secondlavelCategory = secondlavelCategory;
    }

    public String getThirdlavelCategory() {
        return thirdlavelCategory;
    }

    public void setThirdlavelCategory(String thirdlavelCategory) {
        this.thirdlavelCategory = thirdlavelCategory;
    }
}
