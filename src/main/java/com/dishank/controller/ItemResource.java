package com.dishank.controller;

import com.dishank.model.Item;
import com.dishank.service.ItemService;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ItemResource extends ServerResource {

    private static final Logger log =
            LoggerFactory.getLogger(ItemResource.class);

    private ItemService service;

    @Override
    protected void doInit() {
        service = new ItemService();
    }

    /* ---------------- POST /items ---------------- */

    @Post("json")
    public Representation create(Representation entity) {
        try {
            Item item = new JacksonRepresentation<>(entity, Item.class).getObject();
            log.info("Create item request: {}", item.getName());

            Item created = service.create(item.getName(), item.getQuantity());
            setStatus(Status.SUCCESS_CREATED);

            return new JacksonRepresentation<>(created);

        } catch (IllegalArgumentException e) {
            log.warn("Bad request: {}", e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());

        } catch (IOException | SQLException e) {
            log.error("Create item failed", e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }
    }

    /* ---------------- GET /items OR /items/{id} ---------------- */

    @Get("json")
    public Representation getItems() {
        try {
            String id = getAttribute("id");

            if (id == null) {
                List<Item> items = service.getAll();
                log.info("Fetched {} items", items.size());
                return new JacksonRepresentation<>(items);
            }

            Item item = service.getById(Integer.parseInt(id));
            if (item == null) {
                log.warn("Item not found: {}", id);
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
            }

            return new JacksonRepresentation<>(item);

        } catch (SQLException e) {
            log.error("Fetch failed", e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }
    }

    /* ---------------- PATCH /items/{id}/add/{qty} ---------------- */

    @Patch("add")
    public Representation addStock() {
        try {
            int id = Integer.parseInt(getAttribute("id"));
            int qty = Integer.parseInt(getAttribute("qty"));

            log.info("Add stock request → itemId={}, qty={}", id, qty);

            Item updated = service.addStock(id, qty);

            if (updated == null) {
                log.warn("Item not found for addStock: {}", id);
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
            }

            return new JacksonRepresentation<>(updated);

        } catch (IllegalArgumentException e) {
            log.warn("Invalid addStock request: {}", e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());

        } catch (SQLException e) {
            log.error("Add stock failed", e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }
    }

    /* ---------------- GET /items/{id} ---------------- */

    @Get("json")
    public Representation getItemById() {
        try {
            String idStr = getAttribute("id");

            // If no ID is provided, fall back to returning all items
            if (idStr == null || idStr.isEmpty()) {
                List<Item> items = service.getAll();
                log.info("No ID provided, returning all {} items", items.size());
                return new JacksonRepresentation<>(items);
            }

            int id = Integer.parseInt(idStr);
            log.info("Fetching item details for ID: {}", id);

            Item item = service.getById(id);

            if (item == null) {
                log.warn("Item with ID {} not found", id);
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Item not found");
            }

            return new JacksonRepresentation<>(item);

        } catch (NumberFormatException e) {
            log.warn("Invalid ID format provided: {}", getAttribute("id"));
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid ID format");
        } catch (SQLException e) {
            log.error("Database error while fetching item by ID", e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }
    }


    /* ---------------- PATCH /items/{id}/remove/{qty} ---------------- */

    @Patch("remove")
    public Representation removeStock() {
        try {
            int id = Integer.parseInt(getAttribute("id"));
            int qty = Integer.parseInt(getAttribute("qty"));

            log.info("Remove stock request → itemId={}, qty={}", id, qty);

            Item updated = service.removeStock(id, qty);

            if (updated == null) {
                log.warn("Item not found for removeStock: {}", id);
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
            }

            return new JacksonRepresentation<>(updated);

        } catch (IllegalStateException e) {
            // Business rule violation (insufficient stock)
            log.warn("Remove stock failed: {}", e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());

        } catch (IllegalArgumentException e) {
            log.warn("Invalid removeStock request: {}", e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());

        } catch (SQLException e) {
            log.error("Remove stock failed", e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }
    }
}
