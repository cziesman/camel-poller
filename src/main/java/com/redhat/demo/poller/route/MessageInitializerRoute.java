package com.redhat.demo.poller.route;

import com.redhat.demo.poller.service.MessageService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adds some messages.
 */
@Component
public class MessageInitializerRoute extends RouteBuilder {

    @Autowired
    private MessageService messageService;

    @Override
    public void configure() {

        from("timer:messageInitializer?repeatCount=1&delay=1s")
                .routeId("Message Initializer")
                .process(exchange -> {

                    for (int i = 0; i < 1000; i++) {
                        messageService.makeMessage(UUID.randomUUID().toString());
                    }
                });
    }
}
