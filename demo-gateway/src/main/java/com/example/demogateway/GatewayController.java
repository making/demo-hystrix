package com.example.demogateway;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {
	private final PaymentServiceA paymentServiceA;
	private final PaymentServiceB paymentServiceB;
	private final PaymentServiceC paymentServiceC;
	private final Logger log = LoggerFactory.getLogger(GatewayController.class);

	public GatewayController(PaymentServiceA paymentServiceA,
			PaymentServiceB paymentServiceB, PaymentServiceC paymentServiceC) {
		this.paymentServiceA = paymentServiceA;
		this.paymentServiceB = paymentServiceB;
		this.paymentServiceC = paymentServiceC;
	}

	@PostMapping(path = "payment/a")
	public String a() {
		return this.paymentServiceA.payment();
	}

	@PostMapping(path = "payment/b")
	public String b() {
		return this.paymentServiceB.payment();
	}

	@PostMapping(path = "payment/c")
	public String c() {
		return this.paymentServiceC.payment();
	}

	@ExceptionHandler(HystrixRuntimeException.class)
	public ResponseEntity<String> hystrix(HystrixRuntimeException e) {
		Throwable cause = e.getCause();
		if (cause instanceof RejectedExecutionException) {
			log.warn("REJECTED");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("REJECTED");
		}
		if (cause instanceof TimeoutException) {
			log.warn("TIMEOUT");
			return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("TIMEOUT");
		}
		log.warn("HYSTRIX({}:{})", cause.getClass().getName(), e.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("HYSTRIX");
	}
}
