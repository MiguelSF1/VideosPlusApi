package org.example;

import org.example.resources.MovieResource;
import org.example.resources.MovieVersionResource;
import org.example.resources.UserResource;
import org.example.resources.UserResourceCMS;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {
    public static void main(String[] args) {
        final ResourceConfig config = new ResourceConfig(UserResourceCMS.class, UserResource.class, MovieVersionResource.class, MovieResource.class);
        JettyHttpContainerFactory.createServer(URI.create("http://192.168.1.103:8080/"), config);
    }
}
