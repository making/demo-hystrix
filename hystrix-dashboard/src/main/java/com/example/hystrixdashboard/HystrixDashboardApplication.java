package com.example.hystrixdashboard;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.netflix.turbine.TurbineProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableHystrixDashboard
@EnableTurbine
public class HystrixDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystrixDashboardApplication.class, args);
	}

	@Bean
	public CommandLineRunner clr(DiscoveryClient discoveryClient,
			TurbineProperties props) {
		return a -> props.getAppConfigList().forEach(app -> {
			System.out.println(app);
			discoveryClient.getInstances(app)
					.forEach(s -> System.out.println("* " + s.getUri()));
		});
	}

	@Bean
	@Profile("cloud")
	public DiscoveryClient discoveryClient() {
		return new CloudFoundryAppServiceDiscoveryClient();
	}
}
