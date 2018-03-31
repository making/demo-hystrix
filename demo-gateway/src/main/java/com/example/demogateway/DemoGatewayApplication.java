package com.example.demogateway;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import io.micrometer.core.instrument.binder.hystrix.HystrixMetricsBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

@SpringBootApplication
@EnableCircuitBreaker
public class DemoGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoGatewayApplication.class, args);
	}

	@Bean
	public RestTemplateBuilder restTemplateBuilder(
			List<RestTemplateCustomizer> customizers) {
		Logger log = LoggerFactory.getLogger(DefaultResponseErrorHandler.class);
		return new RestTemplateBuilder()
				.requestFactory(SimpleClientHttpRequestFactory::new) //
				.setReadTimeout(10000) //
				.setConnectTimeout(5000) //
				.customizers(customizers) //
				.errorHandler(new DefaultResponseErrorHandler() {
					@Override
					protected void handleError(ClientHttpResponse response,
							HttpStatus statusCode) throws IOException {
						String body = StreamUtils.copyToString(response.getBody(),
								StandardCharsets.UTF_8);
						log.info("{}", body);
						throw new ResponseStatusException(statusCode, body);
					}
				});
	}

	@Bean
	public HystrixMetricsBinder hystrixMetricsBinder() {
		return new HystrixMetricsBinder();
	}
}
