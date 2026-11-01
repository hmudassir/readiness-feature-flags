package dev.portfolio.readiness;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlags {

	private final String mail;

	private final String media;

	public FeatureFlags(
			@Value("${poc.mail.delivery:disabled}") String mail,
			@Value("${poc.media.storage:disabled}") String media) {
		this.mail = mail;
		this.media = media;
	}

	public void requireMail() {
		if (!"enabled".equals(mail)) {
			throw new FeatureDisabledException("Email delivery is disabled");
		}
	}

	public void requireMedia() {
		if (!"enabled".equals(media)) {
			throw new FeatureDisabledException("Media storage is disabled");
		}
	}
}
