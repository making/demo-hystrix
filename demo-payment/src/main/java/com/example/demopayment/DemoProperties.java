package com.example.demopayment;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "demo")
public class DemoProperties {
	private Duration sleep = Duration.ofSeconds(3);
	private Duration timeout = Duration.ofSeconds(1);
	private int maxInFlight = 5;

	public Duration getSleep() {
		return sleep;
	}

	public void setSleep(Duration sleep) {
		this.sleep = sleep;
	}

	public Duration getTimeout() {
		return timeout;
	}

	public void setTimeout(Duration timeout) {
		this.timeout = timeout;
	}

	public int getMaxInFlight() {
		return maxInFlight;
	}

	public void setMaxInFlight(int maxInFlight) {
		this.maxInFlight = maxInFlight;
	}
}
