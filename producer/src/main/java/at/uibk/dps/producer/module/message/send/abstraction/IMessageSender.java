package at.uibk.dps.producer.module.message.send.abstraction;

import at.uibk.dps.producer.module.message.send.dto.SendMessageRequestDto;

public interface IMessageSender {
    void send(SendMessageRequestDto dto);
}
