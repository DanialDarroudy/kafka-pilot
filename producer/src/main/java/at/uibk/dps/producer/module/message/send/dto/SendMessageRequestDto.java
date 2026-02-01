package at.uibk.dps.producer.module.message.send.dto;

import lombok.Data;

@Data
public class SendMessageRequestDto {
    private String topic;
    private String key;
    private byte[] payload;
}
