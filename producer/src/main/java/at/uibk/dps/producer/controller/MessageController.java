package at.uibk.dps.producer.controller;

import at.uibk.dps.producer.module.message.send.abstraction.IMessageSender;
import at.uibk.dps.producer.module.message.send.dto.SendMessageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
    private final IMessageSender messageSender;

    @PostMapping()
    public ResponseEntity<Object> sendMessage(@RequestBody SendMessageRequestDto dto) {
        messageSender.send(dto);
        return ResponseEntity.ok().build();
    }
}
