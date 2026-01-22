package eu.ideya.lingua.bg.audit;

import java.util.Map;

public class AuditResult {
	private final Map<String, Number> metrics;

	public AuditResult(Map<String, Number> metrics) {
		this.metrics = metrics;
	}

	public Map<String, Number> getMetrics() {
		return metrics;
	}
}
