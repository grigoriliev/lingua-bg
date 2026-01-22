package eu.ideya.lingua.bg.audit;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface AuditRegistry {
	Collection<Audit<?>> getAll();

	default Stream<Audit<?>> stream() {
		return getAll().stream();
	}

	default Collection<Audit<?>> getByCategory(AuditCategory category) {
		return stream().filter(audit -> audit.category() == category)
			.collect(Collectors.toList());
	}

	default Optional<Audit<?>> getAuditByID(String id) {
		return stream().filter(audit -> audit.id().getName().equals(id)).findFirst();
	}
}
