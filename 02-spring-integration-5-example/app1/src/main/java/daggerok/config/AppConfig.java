package daggerok.config;

import daggerok.AppApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.SubscribableChannel;

@Configuration
@ComponentScan(basePackageClasses = AppApplication.class)
public class AppConfig {

  @Bean
  public SubscribableChannel input() {
    return new DirectChannel();
  }

  @Bean
  public MessagingTemplate messagingTemplate() {
    return new MessagingTemplate();
  }
}
