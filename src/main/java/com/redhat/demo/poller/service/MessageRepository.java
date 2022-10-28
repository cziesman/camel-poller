package com.redhat.demo.poller.service;

import com.redhat.demo.poller.db.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

}
