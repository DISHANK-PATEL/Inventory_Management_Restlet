package com.dishank.service;

import com.dishank.dao.ItemDao;
import com.dishank.model.Item;
import com.dishank.model.ItemStatus;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ItemService {

    private final ItemDao dao = new ItemDao();

    /* ---------- CREATE ---------- */
    public Item create(String name, int quantity) throws SQLException {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is mandatory");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        Item item = new Item();
        item.setName(name);
        item.setQuantity(quantity);
        item.setStatus(quantity > 0 ? ItemStatus.IN_STOCK : ItemStatus.OUT_OF_STOCK);
        item.setCreatedAt(LocalDateTime.now());

        return dao.save(item);
    }

    /* ---------- READ ALL ---------- */
    public List<Item> getAll() throws SQLException {
        return dao.findAll();
    }

    /* ---------- READ BY ID ---------- */
    public Item getById(int id) throws SQLException {
        return dao.findById(id);
    }

    /* ---------- ADD STOCK ---------- */
    public Item addStock(int id, int qty) throws SQLException {

        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Item item = dao.findById(id);
        if (item == null) return null;

        item.setQuantity(item.getQuantity() + qty);
        item.setStatus(ItemStatus.IN_STOCK);

        return dao.update(item);
    }

    /* ---------- REMOVE STOCK ---------- */
    public Item removeStock(int id, int qty) throws SQLException {

        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Item item = dao.findById(id);
        if (item == null) return null;

        if (item.getQuantity() < qty) {
            throw new IllegalStateException("Insufficient stock");
        }

        int newQty = item.getQuantity() - qty;
        item.setQuantity(newQty);
        item.setStatus(newQty == 0 ? ItemStatus.OUT_OF_STOCK : ItemStatus.IN_STOCK);

        return dao.update(item);
    }
}
