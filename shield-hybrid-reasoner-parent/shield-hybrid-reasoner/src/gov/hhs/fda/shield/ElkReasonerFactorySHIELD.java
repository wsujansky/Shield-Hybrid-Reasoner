package gov.hhs.fda.shield;

import org.apache.log4j.Logger;
import org.semanticweb.elk.owlapi.ElkReasonerConfiguration;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.IllegalConfigurationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public class ElkReasonerFactorySHIELD implements OWLReasonerFactory {

	public ElkReasonerFactorySHIELD() {
		// TODO Auto-generated constructor stub
	}
	
	// logger for this class
	protected static final Logger LOGGER_ = Logger
			.getLogger(ElkReasonerFactory.class);
	
	@Override
	public String getReasonerName() {
		if (LOGGER_.isTraceEnabled())
			LOGGER_.trace("getReasonerName()");
		return ElkReasonerFactorySHIELD.class.getPackage().getImplementationTitle();
	}


	// Allowing only Buffering reasoner, since non-buffering reasoner may cause errors when the ontology is
	// changed while the hybrid reasoner is running;  May with to further test this, since some other changes were
	// since made to the hybrid reasoner algorithm that may now allow a non-buffering reasoner to work without causing
	// these errors.  This has not yet been tested.
	@Override
	public OWLReasoner createNonBufferingReasoner(OWLOntology ontology) throws RuntimeException {
		if (LOGGER_.isTraceEnabled())
			LOGGER_.trace("createNonBufferingReasoner(OWLOntology)");
		throw new RuntimeException("Cannot create non-buffering ElkReasonerSHIELD reasoner");
	}
	
	public OWLReasoner createBufferingReasoner(OWLOntology ontology) {
		if (LOGGER_.isTraceEnabled())
			LOGGER_.trace("createBufferingReasoner(OWLOntology)");
		return createElkReasoner(ontology, true, null);
	}

	@Override
	public OWLReasoner createReasoner(OWLOntology ontology) {
		if (LOGGER_.isTraceEnabled())
			LOGGER_.trace("createReasoner(OWLOntology)");
		return createElkReasoner(ontology, true, null);
	}

	@Override
	public OWLReasoner createNonBufferingReasoner(OWLOntology ontology,
			OWLReasonerConfiguration config)
			throws RuntimeException {
		if (LOGGER_.isTraceEnabled())
			LOGGER_.trace("createNonBufferingReasoner(OWLOntology, OWLReasonerConfiguration)");
		throw new RuntimeException("Cannot create non-buffering ElkReasonerSHIELD reasoner");
	}

	@Override
	public OWLReasoner createReasoner(OWLOntology ontology,
			OWLReasonerConfiguration config)
			throws IllegalConfigurationException {
		if (LOGGER_.isTraceEnabled())
			LOGGER_.trace("createReasoner(OWLOntology, OWLReasonerConfiguration)");
		return createElkReasoner(ontology, true, config);
	}
	
	
 
	@SuppressWarnings("static-method")
	OWLReasoner createElkReasoner(OWLOntology ontology,
			boolean isBufferingMode, OWLReasonerConfiguration config)
			throws IllegalConfigurationException {
		if (LOGGER_.isTraceEnabled())
			LOGGER_.trace("createElkReasoner(OWLOntology, boolean, OWLReasonerConfiguration)");
		ElkReasonerSHIELD reasoner = null;  // Initialized outside try/catch so it can be returned
		ElkReasonerConfiguration elkReasonerConfig;
		if (config != null) {
			if (config instanceof ElkReasonerConfiguration) {//
				elkReasonerConfig = (ElkReasonerConfiguration) config;
			} else {
				elkReasonerConfig = new ElkReasonerConfiguration(config);
			}
		} else {
			elkReasonerConfig = new ElkReasonerConfiguration();
		}
		reasoner = new ElkReasonerSHIELD(ontology, isBufferingMode, elkReasonerConfig);
		return reasoner;
	}

}
