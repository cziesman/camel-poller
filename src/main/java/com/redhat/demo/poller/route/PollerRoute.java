package com.redhat.demo.poller.route;

import com.redhat.demo.poller.db.Message;
import com.redhat.demo.poller.db.PollingStatus;
import com.redhat.demo.poller.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@Component
public class PollerRoute extends RouteBuilder {

    @Value("${com.redhat.demo.batch-size}")
    private int batchSize;

    @Value("${com.redhat.demo.number-of-threads}")
    private int numberOfThreads;

    @Autowired
    private MessageService messageService;

    private static final String URI =
            "jpa://com.redhat.demo.poller.db.Message" +
                    "?nativeQuery=select * from polled_message where polling_status = 'UNREAD'" +
                    "&delay=1000" +
                    "&maximumResults=%d" +
                    "&consumeDelete=false" +
                    "&consumeLockEntity=true" +
                    "&skipLockedEntity=true";

    @Override
    public void configure() throws Exception {

        from(String.format(URI, batchSize))
                .routeId("jpa-poller-route")
                .log(LoggingLevel.DEBUG, "\n" + body())
                .convertBodyTo(Message.class)
                .threads(numberOfThreads)
                .to("direct:handle-message");

        from("direct:handle-message")
                .routeId("handle-message")
                .transacted()
                .process(exchange -> {

                    Message message = exchange.getIn().getBody(Message.class);
                    LOG.debug("{}", message);

                    message.setPollingStatus(PollingStatus.READ.name());
                    message.setTimestamp(OffsetDateTime.now());

                    message = messageService.save(message);
                    LOG.debug("{}", message);
                });
    }
}
