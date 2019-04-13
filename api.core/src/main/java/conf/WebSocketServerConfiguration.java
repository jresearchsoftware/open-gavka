package conf;

import java.util.concurrent.Executors;

import org.jresearch.gavka.web.ws.ConsumerWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@SuppressWarnings("static-method")
public class WebSocketServerConfiguration implements WebSocketConfigurer {

	@Autowired
	protected ConsumerWebSocketHandler consumerWebSocketHandler;

	@Bean
	public TaskScheduler defaultSockJsTaskScheduler() {
		return new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor());
	}

	@Override
	public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
		registry.addHandler(consumerWebSocketHandler, "/consumer");
	}

}