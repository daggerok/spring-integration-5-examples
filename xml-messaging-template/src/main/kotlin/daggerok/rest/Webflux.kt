package daggerok.rest

import daggerok.App
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.core.MessagingTemplate
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.reactive.function.server.ServerResponse.accepted
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import java.util.Map

@Configuration
@ComponentScan(basePackageClasses = [App::class])
@ImportResource("classpath:/config/spring-integration.xml")
class WebfluxRoutesConfig(val messageChannel: DirectChannel,
                          val messagingTemplate: MessagingTemplate) {
  @Bean
  fun routes() = router {

    ("/").nest {

      contentType(MediaType.APPLICATION_JSON_UTF8)

      GET("/**") {
        val map = mapOf("hello" to "world")
        ok().body(
            Mono.just(map), map.javaClass
        )
      }

      POST("/**") {
        accepted().body(
            it.bodyToMono(String::class.java)
                .map {
                  MessageBuilder
                      .withPayload(it)
                      .build()
                }
                .map {
                  val message = messagingTemplate.sendAndReceive(messageChannel, it) as Message<String>
                  val response = message.headers.toMutableMap()
                  response["payload"] = message.payload
                  response
                }, ParameterizedTypeReference.forType(Map::class.java)
        )
      }
    }
  }
}

class MyTransformer {
  fun transform(message: Message<*>): Message<String> {

    println("original headers:")
    println(message.headers)
    println("original payload:")
    println(message.payload)

    return MessageBuilder
        .withPayload("response of ${message.payload}")
        //.setHeader("id", message.headers["id"]) // id header is read-only
        .setHeader("correlationId", message.headers["id"])
        .build()
  }
}
