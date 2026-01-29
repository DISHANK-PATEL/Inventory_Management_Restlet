package com.dishank;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class Main {

    public static void main(String[] args) throws Exception {

        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8080);
        component.getDefaultHost().attach("/inventory", new InventoryApplication());

        component.start();
        System.out.println("Inventory API running at http://localhost:8080/inventory");
    }
}
