package com.dishank;

import java.time.LocalDateTime;

public class Item {

    private int id;
    private String name;
    private int quantity;
    private ItemStatus status;
    private LocalDateTime createdAt;

    public Item() {}

    public Item(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.status = quantity > 0 ? ItemStatus.IN_STOCK : ItemStatus.OUT_OF_STOCK;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public ItemStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void add(int qty) {
        this.quantity += qty;
        this.status = ItemStatus.IN_STOCK;
    }

    public void remove(int qty) {
        if (qty > quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.quantity -= qty;
        if (this.quantity == 0) {
            this.status = ItemStatus.OUT_OF_STOCK;
        }
    }
}
