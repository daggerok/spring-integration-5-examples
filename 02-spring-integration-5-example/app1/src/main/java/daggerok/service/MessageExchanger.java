package daggerok.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class MessageExchanger {

  MessagingTemplate messagingTemplate;

  public <T> Message sendAndReceive(final String destinationName, final T payload) {
    return messagingTemplate.sendAndReceive(destinationName, MessageBuilder.withPayload(payload).build());
  }

  public <T> void send(final String destinationName, final T payload) {
    messagingTemplate.send(destinationName, MessageBuilder.withPayload(payload).build());
  }

  public Message<?> receive(final String destination) {
    return messagingTemplate.receive(destination);
  }

  public <T> T receiveAndConvert(final String destination, final Class<T> type) {
    return messagingTemplate.receiveAndConvert(destination, type);
  }
}
