package daggerok.rest

import daggerok.App
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource
import org.springframework.http.MediaType
import org.springframework.integration.channel.DirectChannel
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.reactive.function.server.ServerResponse.accepted
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

class MyReceiver {
  fun receive(message: Message<*>) {
    println("headers:")
    println(message.headers)
    println("payload:")
    println(message.payload)
  }
}

@Configuration
@ComponentScan(basePackageClasses = [App::class])
@ImportResource("classpath:/config/spring-integration.xml")
class WebfluxRoutesConfig(val messageChannel: DirectChannel) {

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
                .map { messageChannel.send(it) }
                .map { "done" }, String::class.java
        )
      }
    }
  }
}
