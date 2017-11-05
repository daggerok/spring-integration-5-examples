package daggerok.rest;

import daggerok.service.MessageExchanger;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class MessageResource {

  MessageExchanger messageExchanger;

  @PostMapping
  public Message<?> send(@RequestBody final InputMessage message) {
    return messageExchanger.sendAndReceive("input", message.getPayload());
  }

  @Data
  @Accessors(chain = true)
  @FieldDefaults(level = PRIVATE)
  public static class InputMessage {
    String payload;
  }
}
