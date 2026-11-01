package dev.portfolio.readiness;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StartupCheck implements ApplicationRunner {

	private final JdbcTemplate jdbc;

	private final StartupReadiness readiness;

	public StartupCheck(JdbcTemplate jdbc, StartupReadiness readiness) {
		this.jdbc = jdbc;
		this.readiness = readiness;
	}

	@Override
	public void run(ApplicationArguments args) {
		String token = jdbc.queryForObject("select ready_token from app_settings where id = 1", String.class);
		if (!"ok".equals(token)) {
			throw new IllegalStateException("startup check failed");
		}
		readiness.markReady();
	}
}
