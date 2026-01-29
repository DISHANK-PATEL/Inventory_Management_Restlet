package com.dishank;

import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemResource extends ServerResource {

    private static final Map<Integer, Item> STORE = new ConcurrentHashMap<>();
    private static final AtomicInteger ID_GEN = new AtomicInteger(1);

    // POST /items
    @Post("json")
    public Representation create(Representation body) {
        try {
            Item input = new JacksonRepresentation<>(body, Item.class).getObject();

            if (input.getName() == null || input.getName().isEmpty()) {
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Name is mandatory");
            }
            if (input.getQuantity() < 0) {
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Quantity cannot be negative");
            }

            int id = ID_GEN.getAndIncrement();
            Item item = new Item(id, input.getName(), input.getQuantity());
            STORE.put(id, item);

            setStatus(Status.SUCCESS_CREATED);
            return new JacksonRepresentation<>(item);

        } catch (Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
        }
    }

    // GET /items OR /items/{id}
    @Get("json")
    public Representation getItems() {
        String id = getAttribute("id");

        if (id == null) {
            return new JacksonRepresentation<>(new ArrayList<>(STORE.values()));
        }

        Item item = STORE.get(Integer.parseInt(id));
        if (item == null) {
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Item not found");
        }

        return new JacksonRepresentation<>(item);
    }

    // PATCH /items/{id}/add/{qty}
    @Patch("json")
    public Representation addStock() {
        int id = Integer.parseInt(getAttribute("id"));
        int qty = Integer.parseInt(getAttribute("qty"));

        if (qty <= 0) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Qty must be > 0");
        }

        Item item = STORE.get(id);
        if (item == null) {
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Item not found");
        }

        item.add(qty);
        return new JacksonRepresentation<>(item);
    }

    // PATCH /items/{id}/remove/{qty}
    @Patch("remove")
    public Representation removeStock() {
        int id = Integer.parseInt(getAttribute("id"));
        int qty = Integer.parseInt(getAttribute("qty"));

        if (qty <= 0) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Qty must be > 0");
        }

        Item item = STORE.get(id);
        if (item == null) {
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Item not found");
        }

        try {
            item.remove(qty);
            return new JacksonRepresentation<>(item);
        } catch (IllegalArgumentException e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
        }
    }
}
