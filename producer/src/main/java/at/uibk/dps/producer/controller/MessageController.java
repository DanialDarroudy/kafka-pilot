package at.uibk.dps.producer.controller;

import at.uibk.dps.producer.module.message.send.abstraction.IMessageSender;
import at.uibk.dps.producer.module.message.send.dto.SendMessageRequestDto;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final IMessageSender messageSender;
    private final LongCounter counter;

    public MessageController(IMessageSender messageSender, Meter meter) {
        this.messageSender = messageSender;
        this.counter = meter.counterBuilder("producer.messages.received")
                .setDescription("Messages received via REST API")
                .build();
    }

    @PostMapping()
    public ResponseEntity<Object> sendMessage(@RequestBody SendMessageRequestDto dto) {
        counter.add(1);
        messageSender.send(dto);
        return ResponseEntity.ok().build();
    }
}
