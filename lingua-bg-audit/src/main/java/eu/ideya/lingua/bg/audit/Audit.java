package eu.ideya.lingua.bg.audit;

public interface Audit<T> {
	AuditId id();
	AuditCategory category();
	String description();
	AuditResult run(T model);
}
