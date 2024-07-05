package gov.hhs.fda.shield.protege;

import org.protege.editor.owl.model.inference.AbstractProtegeOWLReasonerInfo;
import org.semanticweb.elk.owlapi.ElkReasonerConfiguration;

//import java.io.File;

import org.protege.editor.owl.model.inference.AbstractProtegeOWLReasonerInfo;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
//import org.semanticweb.elk.protege.ElkPreferences;
//import org.semanticweb.elk.owlapi.ElkReasonerSHIELD;
import org.semanticweb.elk.protege.ProtegeReasonerFactory;
//import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
//import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
//import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
//import org.snomed.reasoner.SnomedAbsentOWLReasonerFactory;
import gov.hhs.fda.shield.ElkReasonerFactorySHIELD;
import gov.hhs.fda.shield.ReasonerExplorer;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;

/***
public class ShieldAbsentProtegeReasonerFactory extends AbstractProtegeOWLReasonerInfo {

	public static void main(String[] args) throws OWLOntologyStorageException, OWLOntologyCreationException {
			
			OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
			OWLOntologyManager outputOntologyManager = OWLManager.createOWLOntologyManager();
			OWLOntologyManager statementOntologyManager = OWLManager.createOWLOntologyManager();

			// Load ontology.
			OWLOntology originalOnt = ontologyManager.loadOntologyFromOntologyDocument(
					new File("C:/Users/wsuja/Dropbox/FDA/Research/SWEC-Classification/SWEC-Ontology-Example-7.owx"));

	// DEBUG		System.out.println("THE ORIGINAL ONTOLOGY");
	// DEBUG		System.out.println(originalOnt);
	// DEBUG 		OntologyExplorer.printOntologyParsedAxioms(originalOnt);


			

			
			SnomedAbsentProtegeReasonerFactory protegeReasonerFactory = new SnomedAbsentProtegeReasonerFactory();
			OWLReasonerFactory reasonerFactory = protegeReasonerFactory.getReasonerFactory();
			
//			ElkReasonerFactorySHIELD reasonerFactory = new ElkReasonerFactorySHIELD();
//			OWLReasoner reasonerSHIELD = reasonerFactory.createBufferingReasoner(originalOnt);  // Only buffering reasoners will be allowed

			
			OWLReasoner reasonerSHIELD = reasonerFactory.createReasoner(originalOnt);  // Only buffering reasoners will be allowed
			System.out.println("POST-INSTANTIATION ElkReasonerSHIELD TAXONOMY");
			ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasonerSHIELD) reasonerSHIELD, false);		

	}
***/

/**

//		private final OWLReasonerFactory factory_ = new ElkReasonerFactory();
		
		private final OWLReasonerFactory factory_ = new ElkReasonerFactorySHIELD();

		private final ElkPreferences elkProtegePrefs_ = new ElkPreferences();

		@Override
		public BufferingMode getRecommendedBuffering() {
			elkProtegePrefs_.load();
			return elkProtegePrefs_.incrementalMode
					&& elkProtegePrefs_.autoSynchronization ? BufferingMode.NON_BUFFERING
					: BufferingMode.BUFFERING;
		}

		@Override
		public OWLReasonerFactory getReasonerFactory() {
			return factory_;
		}

		@Override
		public ElkReasonerConfiguration getConfiguration(
				ReasonerProgressMonitor monitor) {
			OWLReasonerConfiguration defaultOwlConfig = ElkReasonerConfiguration
					.getDefaultOwlReasonerConfiguration(monitor);
			return new ElkReasonerConfiguration(defaultOwlConfig,
					ElkPreferences.getElkConfig());
		}
	}

**/
public class ShieldAbsentProtegeReasonerFactory extends ProtegeReasonerFactory {
	
	protected final OWLReasonerFactory factory;

	public ShieldAbsentProtegeReasonerFactory() {
		super();
//		ElkReasonerFactory elkReasonerFactory = (ElkReasonerFactory) super.getReasonerFactory();
//		factory = new SnomedAbsentOWLReasonerFactory(elkReasonerFactory);
		factory = new ElkReasonerFactorySHIELD();
//		factory = new ElkReasonerFactory();
	}

	@Override
	public OWLReasonerFactory getReasonerFactory() {
		return factory;
	}
}
