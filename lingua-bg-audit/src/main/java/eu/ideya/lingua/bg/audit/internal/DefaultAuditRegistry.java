package eu.ideya.lingua.bg.audit.internal;

import eu.ideya.lingua.bg.audit.Audit;
import eu.ideya.lingua.bg.audit.AuditRegistry;
import eu.ideya.lingua.bg.audit.rdf.RdfLabelAudit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DefaultAuditRegistry implements AuditRegistry {
	private final List<Audit<?>> audits = new ArrayList<>();

	public DefaultAuditRegistry() {
		audits.add(new RdfLabelAudit());
	}

	@Override
	public Collection<Audit<?>> getAll() {
		return Collections.unmodifiableList(audits);
	}
}
