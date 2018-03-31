package com.example.demogateway;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentServiceB {
	private final RestTemplate restTemplate;
	private final Logger log = LoggerFactory.getLogger(PaymentServiceB.class);

	public PaymentServiceB(RestTemplateBuilder restTemplateBuilder,
			GatewayProperties props) {
		this.restTemplate = restTemplateBuilder.rootUri(props.getServiceB()) //
				.build();
	}

	@HystrixCommand(commandKey = "paymentB", //
			threadPoolProperties = { //
					@HystrixProperty(name = "coreSize", value = "25"), //
					@HystrixProperty(name = "maximumSize", value = "50"), //
					@HystrixProperty(name = "allowMaximumSizeToDivergeFromCoreSize", value = "true"),//
			}, //
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),//
			})
	public String payment() {
		String res = this.restTemplate.postForObject("/payment", null, String.class);
		log.info("{}", res);
		return res;
	}
}
