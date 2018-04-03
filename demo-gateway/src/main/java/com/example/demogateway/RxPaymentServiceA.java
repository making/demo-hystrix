package com.example.demogateway;

import java.time.Duration;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.netty.channel.ChannelOption;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.RxReactiveStreams;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Component
public class RxPaymentServiceA {
	private final WebClient webClient;

	public RxPaymentServiceA(WebClient.Builder builder, GatewayProperties props) {
		this.webClient = builder.baseUrl(props.getServiceA())
				.clientConnector(new ReactorClientHttpConnector(
						opts -> opts.sslHandshakeTimeout(Duration.ofMinutes(1))
								.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 180_000)))
				.build();
	}

	@HystrixCommand(commandKey = "rxPaymentA", //
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"), //
					@HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "30"), //
			})
	public Observable<String> payment() {
		Mono<String> mono = this.webClient.post() //
				.uri("/payment") //
				.retrieve() //
				.onStatus(HttpStatus::isError,
						e -> Mono.error(new ResponseStatusException(e.statusCode()))) //
				.bodyToMono(String.class);
		return RxReactiveStreams.toObservable(mono);
	}
}
