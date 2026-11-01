package dev.portfolio.readiness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.contributor.Status;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReadinessTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private StartupHealthIndicator indicator;

	@Autowired
	private StartupReadiness readiness;

	@Test
	void healthIsDownUntilTheStartupCheckFinishes() {
		StartupReadiness gate = new StartupReadiness();
		StartupHealthIndicator local = new StartupHealthIndicator(gate);
		assertThat(local.health().getStatus()).isEqualTo(Status.DOWN);
		gate.markReady();
		assertThat(local.health().getStatus()).isEqualTo(Status.UP);
		assertThat(readiness.isReady()).isTrue();
		assertThat(indicator.health().getStatus()).isEqualTo(Status.UP);
	}

	@Test
	void healthEndpointIsUpAfterStartup() throws Exception {
		mvc.perform(get("/actuator/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));
	}

	@Test
	void anonymousNewsletterWithCsrfIsUnavailable() throws Exception {
		mvc.perform(post("/api/newsletter/subscriptions"))
				.andExpect(status().isForbidden());
		mvc.perform(post("/api/newsletter/subscriptions").with(csrf()))
				.andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.code").value("FEATURE_DISABLED"));
	}

	@Test
	void mediaRequiresAuthenticationBeforeTheDisabledGuard() throws Exception {
		mvc.perform(post("/api/media/uploads").with(csrf()))
				.andExpect(status().isUnauthorized());
		mvc.perform(post("/api/media/uploads").with(csrf()).with(user("ada")))
				.andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.code").value("FEATURE_DISABLED"));
	}
}
