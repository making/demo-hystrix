package com.example.hystrixdashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

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
			Lookup lookup = new Lookup(hostname, Type.A);
			List<ServiceInstance> serviceInstances = new ArrayList<>();
			Record[] records = lookup.run();
			if (records != null) {
				for (Record record : records) {
					DefaultServiceInstance serviceInstance = new DefaultServiceInstance(
							serviceId, record.rdataToString(), 8080, false);
					serviceInstances.add(serviceInstance);
				}
			}
			return serviceInstances;
		}
		catch (TextParseException e) {
			log.warn("{}", e.getMessage());
			return Collections.emptyList();
		}
	}

	@Override
	public List<String> getServices() {
		return Collections.emptyList();
	}
}
