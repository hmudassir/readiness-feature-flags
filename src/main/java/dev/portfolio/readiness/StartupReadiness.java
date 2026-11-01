package dev.portfolio.readiness;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

@Component
public class StartupReadiness {

	private final AtomicBoolean ready = new AtomicBoolean(false);

	public void markReady() {
		ready.set(true);
	}

	public boolean isReady() {
		return ready.get();
	}
}
