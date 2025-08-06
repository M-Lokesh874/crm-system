package com.org.crm.task.consumer;

import com.org.crm.common.events.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumer for task-related events from RabbitMQ
 */
@Component
@Slf4j
public class TaskEventConsumer {

    @RabbitListener(queues = "task.events.queue")
    public void handleTaskEvent(BaseEvent event) {
        log.info("[TaskEventConsumer] Received event: {} - ID: {} - Source: {} - Timestamp: {}", event.getEventType(), event.getEventId(), event.getSource(), event.getTimestamp());
        // You can add business logic here to react to events if needed
    }
}