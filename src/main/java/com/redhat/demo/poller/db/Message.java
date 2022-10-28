package com.redhat.demo.poller.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.component.jpa.Consumed;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "polled_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    private String body;

    private String pollingStatus;

    @Column(columnDefinition = "TIMESTAMP")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime timestamp;

    @Consumed
    public void updatePollingStatus() {

        setPollingStatus(PollingStatus.READ.name());
    }
}
