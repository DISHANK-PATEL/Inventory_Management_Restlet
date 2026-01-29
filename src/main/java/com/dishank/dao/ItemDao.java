package com.dishank.dao;

import com.dishank.model.Item;
import com.dishank.model.ItemStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {

    private static final String URL =
            "jdbc:mysql://localhost:3306/inventorydb?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /* ---------- CREATE ---------- */
    public Item save(Item item) throws SQLException {
        String sql =
                "INSERT INTO items(name, quantity, status) VALUES (?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, item.getName());
            ps.setInt(2, item.getQuantity());
            ps.setString(3, item.getStatus().name());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                item.setId(rs.getInt(1));
            }
            return item;
        }
    }

    /* ---------- READ ALL ---------- */
    public List<Item> findAll() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(mapRow(rs));
            }
        }
        return items;
    }

    /* ---------- READ BY ID ---------- */
    public Item findById(int id) throws SQLException {
        String sql = "SELECT * FROM items WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    /* ---------- UPDATE ---------- */
    public Item update(Item item) throws SQLException {
        String sql =
                "UPDATE items SET quantity=?, status=? WHERE id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, item.getQuantity());
            ps.setString(2, item.getStatus().name());
            ps.setInt(3, item.getId());
            ps.executeUpdate();

            return item;
        }
    }

    /* ---------- RESULTSET → ITEM ---------- */
    private Item mapRow(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setStatus(ItemStatus.valueOf(rs.getString("status")));
        item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return item;
    }
}
