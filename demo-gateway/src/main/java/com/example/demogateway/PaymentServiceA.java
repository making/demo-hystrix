package com.example.demogateway;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentServiceA {
	private final RestTemplate restTemplate;
	private final Logger log = LoggerFactory.getLogger(PaymentServiceA.class);

	public PaymentServiceA(RestTemplateBuilder restTemplateBuilder,
			GatewayProperties props) {
		this.restTemplate = restTemplateBuilder.rootUri(props.getServiceA()) //
				.build();
	}

	@HystrixCommand(commandKey = "paymentA", //
			threadPoolProperties = { //
					@HystrixProperty(name = "coreSize", value = "15"), //
					@HystrixProperty(name = "maximumSize", value = "30"), //
					@HystrixProperty(name = "allowMaximumSizeToDivergeFromCoreSize", value = "true"), //
			// @HystrixProperty(name = "queueSizeRejectionThreshold", value = "100"), //
			}, //
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"), //
			})
	public String payment() {
		return this.restTemplate.postForObject("/payment", null, String.class);
	}
}
