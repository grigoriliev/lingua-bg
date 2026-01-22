package eu.ideya.lingua.bg.audit;

public enum AuditCategory {
	// must-pass correctness
	CORE,
	// ontology/data structure issues
	STRUCTURAL,
	// stylistic / best practices
	LINT,
	// counts, ratios, coverage
	METRIC
}
