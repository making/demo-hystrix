package com.example.demogateway;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

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
	private final RxPaymentServiceA rxPaymentServiceA;
	private final RxPaymentServiceB rxPaymentServiceB;
	private final RxPaymentServiceC rxPaymentServiceC;
	private final Logger log = LoggerFactory.getLogger(GatewayController.class);

	public GatewayController(PaymentServiceA paymentServiceA,
			PaymentServiceB paymentServiceB, PaymentServiceC paymentServiceC,
			RxPaymentServiceA rxPaymentServiceA, RxPaymentServiceB rxPaymentServiceB,
			RxPaymentServiceC rxPaymentServiceC) {
		this.paymentServiceA = paymentServiceA;
		this.paymentServiceB = paymentServiceB;
		this.paymentServiceC = paymentServiceC;
		this.rxPaymentServiceA = rxPaymentServiceA;
		this.rxPaymentServiceB = rxPaymentServiceB;
		this.rxPaymentServiceC = rxPaymentServiceC;
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

	@PostMapping(path = "payment/rx/a")
	public Observable<String> rxA() {
		return this.rxPaymentServiceA.payment();
	}

	@PostMapping(path = "payment/rx/b")
	public Observable<String> rxB() {
		return this.rxPaymentServiceB.payment();
	}

	@PostMapping(path = "payment/rx/c")
	public Observable<String> rxC() {
		return this.rxPaymentServiceC.payment();
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
