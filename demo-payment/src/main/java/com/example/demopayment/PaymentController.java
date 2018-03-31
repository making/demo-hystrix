package com.example.demopayment;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
	private final BlockingQueue<Boolean> queue;
	private final DemoProperties props;
	private final Logger log = LoggerFactory.getLogger(PaymentController.class);

	public PaymentController(DemoProperties props) {
		this.queue = new ArrayBlockingQueue<>(props.getMaxInFlight());
		this.props = props;
	}

	@PostMapping(path = "payment")
	public ResponseEntity payment() {
		boolean success = false;
		try {
			success = this.queue.offer(true, this.props.getTimeout().toMillis(),
					TimeUnit.MILLISECONDS);
			Thread.sleep(this.props.getSleep().toMillis());
			if (!success) {
				log.info("BUSY");
				return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("BUSY");
			}
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.info("CANCELED");
			return ResponseEntity.status(HttpStatus.GONE).body("CANCELED");
		}
		finally {
			if (success) {
				this.queue.poll();
			}
		}
		log.info("OK");
		return ResponseEntity.ok("OK");
	}
}
