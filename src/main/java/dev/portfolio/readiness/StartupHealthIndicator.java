package dev.portfolio.readiness;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class StartupHealthIndicator implements HealthIndicator {

	private final StartupReadiness readiness;

	public StartupHealthIndicator(StartupReadiness readiness) {
		this.readiness = readiness;
	}

	@Override
	public Health health() {
		if (readiness.isReady()) {
			return Health.up().build();
		}
		return Health.down().build();
	}
}
