package org.example.stub.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stub.entity.MessageEntity;
import org.example.stub.repository.MessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "test-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message){
        log.info("Получено сообщение из Kafka: {}", message);

        try {
            JsonNode json = objectMapper.readTree(message);

            String msgId = json.get("msg_id").asText();
            String fullName = json.get("full_name").asText().trim();
            String inn = json.get("inn").asText();


            MessageEntity entity = new MessageEntity();
            entity.setMsgId(msgId);
            entity.setFullName(fullName);
            entity.setInn(inn);
            entity.setTime(LocalDateTime.now());

            messageRepository.save(entity);
            log.info("Сообщение с msgId={} сохранено в БД", msgId);
        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения {}", e.getMessage(), e);


        }
    }
}
