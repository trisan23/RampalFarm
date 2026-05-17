package com.rampal.farm2home.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {
    private int productId;
    private String productName;
    private String description;
    private int stockQuantity;
    private BigDecimal price;
    private String imageUrl;
    private Integer categoryId;
    private String categoryName;
    private Timestamp createdAt;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        if (productId == 23 || "Mustard Oil 1L".equals(productName)) {
            return "Mustard Seed";
        }
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        if (productId == 23 || "Pure mustard cooking oil".equals(description)) {
            return "Pure mustard seed";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDisplayImageUrl() {
        if (imageUrl != null && !imageUrl.isBlank()) {
            if (imageUrl.startsWith("uploads/") || imageUrl.startsWith("images/products/")) {
                return imageUrl;
            }
            return buildCatalogImagePath(imageUrl, productName);
        }
        return buildCatalogImagePath(null, productName);
    }

    private String buildCatalogImagePath(String originalImageUrl, String fallbackName) {
        String imageKey = extractImageKey(originalImageUrl, fallbackName);
        String mappedImage = mapCatalogImage(imageKey);
        if (mappedImage != null) {
            return mappedImage;
        }
        return "images/products/" + imageKey + ".png";
    }

    private String mapCatalogImage(String imageKey) {
        return switch (imageKey) {
            case "apple" -> "images/products/apple.png";
            case "banana" -> "images/products/banana.png";
            case "bitter-gourd" -> "images/products/bitter-gourd.png";
            case "black-pepper" -> "images/products/black-pepper.png";
            case "broccoli" -> "images/products/broccoli.jpeg";
            case "buckwheat" -> "images/products/buckwheat.png";
            case "butter" -> "images/products/butter.jpeg";
            case "cabbage" -> "images/products/cabbage.png";
            case "capsicum" -> "images/products/capsicum.jpeg";
            case "cardamom" -> "images/products/cardamom.png";
            case "carrot" -> "images/products/carrot.png";
            case "cauliflower" -> "images/products/cauliflower.jpeg";
            case "cheese" -> "images/products/cheese.png";
            case "chickpeas" -> "images/products/chickpeas.png";
            case "chili-powder", "chill-powder" -> "images/products/chili-powder.png";
            case "cloves" -> "images/products/cloves.png";
            case "coriander-powder" -> "images/products/coriander-powder.png";
            case "cream" -> "images/products/cream.png";
            case "cucumber" -> "images/products/cucumber.jpeg";
            case "cumin" -> "images/products/cumin.jpeg";
            case "curd" -> "images/products/curd.jpeg";
            case "eggplant" -> "images/products/eggplant.png";
            case "ghee" -> "images/products/ghee.png";
            case "ginger" -> "images/products/ginger.jpeg";
            case "grapes" -> "images/products/grapes.png";
            case "green-beans" -> "images/products/green-beans.png";
            case "guava", "gauva" -> "images/products/guava.jpeg";
            case "khoya" -> "images/products/khoya.png";
            case "kidney-beans" -> "images/products/kidney-beans.png";
            case "lassi" -> "images/products/lassi.png";
            case "lentils" -> "images/products/lentils.jpeg";
            case "maize" -> "images/products/maize.png";
            case "mango" -> "images/products/mango.png";
            case "milk" -> "images/products/milk.png";
            case "millet" -> "images/products/millet.png";
            case "mustard-seed", "mustard-oil" -> "images/products/mustard-seed.jpeg";
            case "orange" -> "images/products/orange.png";
            case "paneer" -> "images/products/paneer.png";
            case "papaya" -> "images/products/papaya.jpeg";
            case "pineapple" -> "images/products/pineapple.png";
            case "pomegranate" -> "images/products/pomegranate.png";
            case "potato" -> "images/products/potato.png";
            case "radish", "raddish" -> "images/products/radish.png";
            case "rice" -> "images/products/rice.png";
            case "spinach" -> "images/products/spinach.png";
            case "tomato" -> "images/products/tomato.png";
            case "turmeric" -> "images/products/tumeric.png";
            case "watermelon" -> "images/products/watermelon.png";
            case "wheat" -> "images/products/wheat.png";
            default -> null;
        };
    }

    private String extractImageKey(String originalImageUrl, String fallbackName) {
        if (originalImageUrl != null && !originalImageUrl.isBlank()) {
            String fileName = originalImageUrl;
            int slashIndex = fileName.lastIndexOf('/');
            if (slashIndex >= 0) {
                fileName = fileName.substring(slashIndex + 1);
            }
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = fileName.substring(0, dotIndex);
            }
            if (!fileName.isBlank()) {
                return normalizeImageKey(fileName).replaceAll("\\s+", "-");
            }
        }
        if (fallbackName == null || fallbackName.isBlank()) {
            return "farm2home-product";
        }
        String normalized = normalizeImageKey(fallbackName);
        normalized = normalized.replaceAll("\\b\\d+(kg|g|l|ml)\\b", "").trim();
        normalized = normalized.replaceAll("\\s+", "-");
        if (normalized.isBlank()) {
            return "farm2home-product";
        }
        return normalized;
    }

    private String normalizeImageKey(String value) {
        return value.trim().toLowerCase().replaceAll("[^a-z0-9]+", " ").trim();
    }
}
