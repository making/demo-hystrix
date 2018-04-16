package com.example.hystrixdashboard;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class CloudFoundryAppServiceDiscoveryClient implements DiscoveryClient {
	private final Logger log = LoggerFactory
			.getLogger(CloudFoundryAppServiceDiscoveryClient.class);

	@Override
	public String description() {
		return "CF App Service Discovery Client";
	}

	@Override
	public List<ServiceInstance> getInstances(String serviceId) {
		String hostname = serviceId + ".apps.internal";
		try {
			List<ServiceInstance> serviceInstances = new ArrayList<>();
			InetAddress[] addresses = InetAddress.getAllByName(hostname);
			if (addresses != null) {
				for (InetAddress address : addresses) {
					DefaultServiceInstance serviceInstance = new DefaultServiceInstance(
							serviceId, address.getHostAddress(), 8080, false);
					serviceInstances.add(serviceInstance);
				}
			}
			return serviceInstances;
		}
		catch (UnknownHostException e) {
			log.warn("{}", e.getMessage());
			return Collections.emptyList();
		}
	}

	@Override
	public List<String> getServices() {
		return Collections.emptyList();
	}
}
