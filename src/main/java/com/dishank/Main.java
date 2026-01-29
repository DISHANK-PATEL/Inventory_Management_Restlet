package com.dishank;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            log.info("MySQL Driver loaded");

            DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorydb?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "root"
            );
            log.info("Database connection verified");

        } catch (Exception e) {
            log.error("Database startup check failed", e);
            return;
        }

        try {
            Component component = new Component();
            component.getServers().add(Protocol.HTTP, 8080);
            component.getDefaultHost().attach("/inventory", new InventoryApplication());
            component.start();

            log.info("Inventory API running at http://localhost:8080/inventory");

        } catch (Exception e) {
            log.error("Failed to start Restlet server", e);
        }
    }
}
