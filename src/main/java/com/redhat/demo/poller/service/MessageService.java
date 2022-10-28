package com.redhat.demo.poller.service;

import com.redhat.demo.poller.db.Message;
import com.redhat.demo.poller.db.PollingStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@Slf4j
public class MessageService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MessageRepository messageRepository;

    public Message makeMessage(String body) {

        Message message = new Message(null, body, PollingStatus.UNREAD.name(), OffsetDateTime.now());

        return save(message);
    }

    public List<Message> save(List<Message> messages, String extension) {

        messageRepository.saveAll(messages);
        em.flush();

        return findAllMessages();
    }

    @Transactional(readOnly = true)
    public List<Message> findAllMessages() {

        return StreamSupport
                .stream(messageRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Message findMessageById(Long id) {

        Objects.requireNonNull(id, "<id> cannot be null");

        return messageRepository.findById(id).orElse(null);
    }

    public Message save(Message message) {

        Objects.requireNonNull(message, "<message> cannot be null");

        Message retval = messageRepository.save(message);
        em.flush();

        return retval;
    }

    public void delete(Message message) {

        Objects.requireNonNull(message, "<message> cannot be null");

        messageRepository.delete(message);
        em.flush();
    }

    public void deleteAll() {

        messageRepository.deleteAll();
        em.flush();
    }
}
