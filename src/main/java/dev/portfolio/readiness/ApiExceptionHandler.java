package dev.portfolio.readiness;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(FeatureDisabledException.class)
	ProblemDetail disabled(FeatureDisabledException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
		problem.setTitle("Service Unavailable");
		problem.setProperty("code", "FEATURE_DISABLED");
		return problem;
	}
}
