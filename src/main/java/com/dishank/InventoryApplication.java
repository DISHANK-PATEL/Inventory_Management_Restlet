package com.dishank;

import com.dishank.controller.ItemResource;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryApplication extends Application {

    private static final Logger log = LoggerFactory.getLogger(InventoryApplication.class);

    @Override
    public synchronized Restlet createInboundRoot() {

        log.info("Registering API routes");

        Router router = new Router(getContext());
        router.attach("/items", ItemResource.class);
        router.attach("/items/{id}", ItemResource.class);
        router.attach("/items/{id}/add/{qty}", ItemResource.class);
        router.attach("/items/{id}/remove/{qty}", ItemResource.class);

        return router;
    }
}
