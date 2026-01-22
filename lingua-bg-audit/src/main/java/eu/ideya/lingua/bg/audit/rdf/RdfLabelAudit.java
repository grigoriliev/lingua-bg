package eu.ideya.lingua.bg.audit.rdf;

import eu.ideya.lingua.bg.audit.AuditCategory;
import eu.ideya.lingua.bg.audit.AuditId;
import eu.ideya.lingua.bg.audit.AuditResult;
import eu.ideya.simplicity.util.StreamUtils;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;

public class RdfLabelAudit implements RdfAudit {
	private static final AuditId ID = new AuditId("rdf.labels");

	@Override
	public AuditId id() {
		return ID;
	}

	@Override
	public AuditCategory category() {
		return AuditCategory.METRIC;
	}

	@Override
	public String description() {
		return "Reports statistics on rdfs:label usage in a Jena Model,\n including total counts and breakdowns by language tag.";
	}

	@Override
	public AuditResult run(Model model) {
		final Map<String, Long> result = StreamUtils.stream(model.listStatements()).filter(
			stmt -> stmt.getPredicate().equals(RDFS.label)
		).map(
			stmt -> stmt.getObject().asLiteral().getLanguage()
		).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		final Map<String, Number> map = new TreeMap<>();
        map.putAll(result);
		return new AuditResult(map);
	}
}
