package dev.portfolio.readiness;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	private final FeatureFlags features;

	public ApiController(FeatureFlags features) {
		this.features = features;
	}

	@PostMapping("/api/newsletter/subscriptions")
	public void subscribe() {
		features.requireMail();
	}

	@PostMapping("/api/media/uploads")
	public void upload() {
		features.requireMedia();
	}
}
