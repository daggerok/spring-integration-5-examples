package daggerok.apps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
@Component
public class ServiceActivator1 {

  @ServiceActivator(inputChannel = "input")
  public String serviceActivator1(@Payload final String payload) {
    log.warn("ServiceActivator1\n{}", payload);
    if (isEmpty(payload)) return "";
    return new StringBuilder(payload).reverse().toString();
  }
}
