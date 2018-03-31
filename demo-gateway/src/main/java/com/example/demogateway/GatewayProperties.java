package com.example.demogateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {
	private String serviceA;
	private String serviceB;
	private String serviceC;

	public String getServiceA() {
		return serviceA;
	}

	public void setServiceA(String serviceA) {
		this.serviceA = serviceA;
	}

	public String getServiceB() {
		return serviceB;
	}

	public void setServiceB(String serviceB) {
		this.serviceB = serviceB;
	}

	public String getServiceC() {
		return serviceC;
	}

	public void setServiceC(String serviceC) {
		this.serviceC = serviceC;
	}
}
