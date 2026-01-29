package com.dishank;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class InventoryApplication extends Application {

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("/items", ItemResource.class);
        router.attach("/items/{id}", ItemResource.class);
        router.attach("/items/{id}/add/{qty}", ItemResource.class);
        router.attach("/items/{id}/remove/{qty}", ItemResource.class);

        return router;
    }
}
