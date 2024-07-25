package gov.hhs.fda.shield;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.elk.owlapi.ElkReasoner;
import org.semanticweb.elk.owlapi.ElkReasonerConfiguration;
import org.semanticweb.elk.owlapi.OwlOntologyLoader;
import org.semanticweb.elk.reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.IllegalConfigurationException;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElkReasonerSHIELD extends ElkReasoner {

	private static final Logger LOGGER_ = LoggerFactory.getLogger(SubsumptionNormalFormBuilderSHIELD.class);

	@SuppressWarnings("unused")
	private boolean isBufferingMode_;
	@SuppressWarnings("unused")
	private OWLReasonerConfiguration owlReasonerConfig_;  
	private StatementClassifierSHIELD classifier; 
	private String statementConceptNamespace;
	private String statementConceptName;
	private String temporalAnnotationOwlIRI;
	private String absenceNamespace;
	private String absenceProperty;
	private String absenceValue;

	// Main class for testing only
	public static void main(String[] args)  {
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		try {
			OWLOntology originalOnt = ontologyManager.loadOntologyFromOntologyDocument(
					new File("C:/Users/wsuja/Documents/ConsultingEngagements/FDA/Research/SWEC-Classification/SWEC-Ontology-Example-9.owx"));
			ElkReasonerFactorySHIELD reasonerFactory = new ElkReasonerFactorySHIELD();
			OWLReasoner reasonerSHIELD = reasonerFactory.createBufferingReasoner(originalOnt);  // Only buffering reasoners will be allowed;  see comment in ElkReasonerFactorySHIELD.java
			System.out.println("POST-INSTANTIATION ElkReasonerSHIELD OWL TAXONOMY:");
			ReasonerExplorer.printOwlTaxonomy(reasonerSHIELD.getTopClassNode(), 0, reasonerSHIELD);
			System.out.println();
			System.out.println("Disposing...");
			reasonerSHIELD.dispose();
			} catch (OWLOntologyCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	// Constructor creates super class ElkReasoner, which classifies the input ontology using standard EL++ classification, and
	// then calls the preComputeInferences() and/or recreateReasoner() methods below, which invoke the hybrid reasoner on all
	// concepts rooted at the concept in the input ontology specified by DefaultProperties.STATEMENT_CONCEPT_NAMESPACE and 
	// DefaultProperties.STATEMENT_CONCEPT_NAME (e.g., "http://www.hhs.fda.org/shield/SWEC-Ontology#Statement-Concept" in the test 
	// ontologies)
	public ElkReasonerSHIELD (OWLOntology loadedOntology, boolean isBufferingMode, OWLReasonerConfiguration config) throws IllegalConfigurationException {                        
		super(loadedOntology, isBufferingMode, convertToElkConfig(config));
		this.isBufferingMode_ = isBufferingMode;
		this.owlReasonerConfig_ = config;  
		this.statementConceptNamespace = DefaultProperties.STATEMENT_CONCEPT_NAMESPACE;
		this.statementConceptName = DefaultProperties.STATEMENT_CONCEPT_NAME;
		this.temporalAnnotationOwlIRI = DefaultProperties.TEMPORAL_ANNOTATION_OWL_IRI;
		this.absenceNamespace = DefaultProperties.ABSENCE_NAMESPACE;
		this.absenceProperty = DefaultProperties.ABSENCE_PROPERTY;
		this.absenceValue = DefaultProperties.ABSENCE_VALUE;
		
//DEBUG System.out.println("REASONER: Completed creation of ElkReasoner super class");	
	}	
	
	
	// Copy all axioms defining classes that are sub-types of the targetClassName from the 
	// sourceOntology and put them in the destination ontology; these classes will get reclassified
	// using the hybrid reasoner logic.  It is assumed that the sub-hierarchy rooted at targetClassName includes
	// no concepts that are referenced by the remainder of the ontology
//	public synchronized void moveStatementAxioms(String targetClassNamespace, String targetClassName, OWLOntology sourceOntology, OWLOntology destinationOntology, Boolean removeFromSourceOntology, OntologyType ontologyToReturn) {
	public synchronized int moveStatementAxioms(String targetClassNamespace, String targetClassName, OWLOntology sourceOntology, OWLOntology destinationOntology) {
		OWLOntologyManager ontologyManager = destinationOntology.getOWLOntologyManager();  // Very important that it's OWLOntologyManager from *desintationOntology*, not sourceOntology
		StructuralReasonerFactory structuralReasonerFactory = new StructuralReasonerFactory();
		// Structural reasoner creates *stated* hierarchy for the input ontology; used to identify all sub-classes
		OWLReasoner structuralReasoner = structuralReasonerFactory.createNonBufferingReasoner(sourceOntology);
//		Set<OWLClass> subClassSet = ReasonerExplorer.buildSubClassSet(targetClassName, sourceOntology, structuralReasoner, true); // ORIG
		Set<OWLClass> subClassSet = buildSubClassSet(targetClassNamespace, targetClassName, sourceOntology, structuralReasoner, true);
		int numAxiomsMoved = 0;
		for (OWLClass owlClass : subClassSet) {  //NEW
			Set<OWLClassAxiom> defnAxioms = sourceOntology.getAxioms(owlClass,Imports.INCLUDED);		
				for (Iterator<OWLClassAxiom> iterator = defnAxioms.iterator(); iterator.hasNext();) {  
					OWLAxiom owlAxiom = (OWLAxiom) iterator.next();  
//DEBUG System.out.println("REASONER: Removing/Adding the  Definition Axiom for: " + owlClass);
//					if (removeFromSourceOntology) {
//						ontologyManager.removeAxiom(sourceOntology, owlAxiom);
//DEBUG System.out.println("REASONER: Removed");
//					}
					ontologyManager.addAxiom(destinationOntology, owlAxiom);
					numAxiomsMoved++;
//DEBUG System.out.println("REASONER: Added");
				}
				Set<OWLDeclarationAxiom> declAxioms = sourceOntology.getDeclarationAxioms(owlClass);	//NEW
//DEBUG System.out.println("REASONER: Got declaratons for: " + owlClass);
				for (Iterator<OWLDeclarationAxiom> iterator = declAxioms.iterator(); iterator.hasNext();) {  //NEW
					OWLAxiom owlAxiom = (OWLAxiom) iterator.next(); 
//DEBUG System.out.println("REASONER: Removing/Adding the  Declaration Axiom for: " + owlClass);
//					if (removeFromSourceOntology) {
//						ontologyManager.removeAxiom(sourceOntology, owlAxiom);
//					}
					ontologyManager.addAxiom(destinationOntology, owlAxiom);
					numAxiomsMoved++;
				}
		}
		return numAxiomsMoved;
}
	

	@Override	
	public void precomputeInferences(InferenceType... inferenceTypes)
			throws ReasonerInterruptedException, TimeOutException,
			InconsistentOntologyException {
 		if (LOGGER_.isDebugEnabled())
 
			LOGGER_.debug("precomputeInferences(InferenceType...)");
// Not implemented; unsure if needed:		checkInterrupted();
		// we use the main progress monitor only here
		this.reasoner_.setProgressMonitor(this.mainProgressMonitor_);
		try {
			for (InferenceType inferenceType : inferenceTypes) {
				if (inferenceType.equals(InferenceType.CLASS_HIERARCHY))
					preComputeHierarchy();
				else if (inferenceType.equals(InferenceType.CLASS_ASSERTIONS))
					throw new RuntimeException(
							"precomputeInferences(inferenceTypes):  InferenceType.CLASS_ASSERTIONS not supported");
			}
		} catch (RuntimeException e) {
			throw e;
		} finally {
			this.reasoner_.setProgressMonitor(this.secondaryProgressMonitor_);
		}
	}
	
	
	protected void preComputeHierarchy() {
		this.reasoner_ = new ReasonerFactory().createReasoner(
				new OwlOntologyLoader(owlOntology_, this.mainProgressMonitor_),
				stageExecutor_, config_);
		this.reasoner_.setAllowFreshEntities(isAllowFreshEntities);
		// use the secondary progress monitor by default, when necessary, we
		// switch to the primary progress monitor; this is to avoid bugs with
		// progress monitors in Protege
		this.reasoner_.setProgressMonitor(this.secondaryProgressMonitor_);
		
		OWLOntology statementOntology = createEmptyOwlOntology(OWLManager.createOWLOntologyManager());  // Will be populated with statement axioms by moveStatementAxioms
		
		// The statement axioms (rooted at the first parameter of the call) are all moved from the loadedOntology
		// into the statementOntology.  The resulting loadedOntology is assigned the name kernelOntology.  It will
		// ultimately also contain the correctly classified statement concepts.  Initially, it only
		// contains kernel concepts.
// 		moveStatementAxioms(this.statementConceptNamespace, this.statementConceptName, owlOntology_, statementOntology, false, OntologyType.SOURCE);
 		int numStatementAxiomsMoved = moveStatementAxioms(DefaultProperties.STATEMENT_CONCEPT_NAMESPACE, DefaultProperties.STATEMENT_CONCEPT_NAME, owlOntology_, statementOntology);
 		if (numStatementAxiomsMoved == 0) 	
 			System.out.println("WARNING:  No Statement Concepts found in ontology (looking for IRI = " + DefaultProperties.STATEMENT_CONCEPT_NAMESPACE + "#" + DefaultProperties.STATEMENT_CONCEPT_NAME);
 		else {

// DEBUG System.out.println("REASONER IN preComputeHierarchy: Completed moving of axioms from original to statement ontology");	
 		
//DEBUG System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN preComputeHierarchy");
//DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) this, false);

 //DEBUG System.out.println("ORIGINAL STATEMENT ONTOLOGY - IN reCreateReasoner: ");
 //DEBUG System.out.println(statementOntology); 

//DEBUG System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN reCreateReasoner");
//DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);
	
 			// A StructuralReasoner instance is created to represent the stated hierarchy for the 
 			// statementOntology. This hierarchy is needed for the later classification of the statement 
 			// concepts into the kernel reasoner's taxonomy.
 			StructuralReasonerFactory structuralReasonerFactory = new StructuralReasonerFactory(); 
 			OWLReasoner statementOwlReasoner = structuralReasonerFactory.createNonBufferingReasoner(statementOntology);
		
// DEBUG  System.out.println("ORIGINAL STATEMENT REASONER TAXONOMY - IN reCreateReasoner");
// DEBUG  ReasonerExplorer.printCurrentReasonerTaxonomy((StructuralReasoner) statementOwlReasoner, false);
		
 			// Instantiation of a StatementClassifierSHIELD classifier creates subsumption-normal-form 
 			// representations of each statement in the statementOntology.  These representations are 
 			// needed by the subsequent classifyStatementConcepts operation.
 			this.classifier = new StatementClassifierSHIELD(this.owlOntology_, statementOntology, this, statementOwlReasoner,
				                                        	DefaultProperties.STATEMENT_CONCEPT_NAMESPACE, DefaultProperties.STATEMENT_CONCEPT_NAME,
				                                        	DefaultProperties.TEMPORAL_ANNOTATION_OWL_IRI, DefaultProperties.ABSENCE_NAMESPACE, 
				                                        	DefaultProperties.ABSENCE_PROPERTY, DefaultProperties.ABSENCE_VALUE);
 			classifier.classifyStatementConcepts(this.owlOntology_, statementOntology, this, statementOwlReasoner);
		
//DEBUG System.out.println("POST-MIGRATION KERNEL REASONER TAXONOMY - IN preComputeHierarchy");
//DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) this, false);	
 		}	
	}
	
	
	/* This method is called by the constructor for the super class "ElkReasoner," which is called by the constructor for this
	 * class "ElkReasonerSHIELD" (see above); 
	 * The method first instantiates an ElkReasoner object ("this.reasoner"), which classifies the input ontology using the standard EL++ reasoning
	 * and classification logic.
	 * The method then copes all of the axioms in the input ontology that define Statement Concepts into a temporary OWLOntology object called
	 * "statementOntology".  This temporary ontology is then loaded into a StructuralOntology object, and each of the Statement Concepts are
	 * classified into a stated hierarchy (i.e., without any logical inference).
	 * The method then calls the method StatementClassifierSHIELD.classifyStatementConcepts, which first normalizes each of the Statement Concepts in
	 * the statementOntology, and then classifies each of these Statement Concepts with respect to each other using hybrid-reasoning algorithms
	 * (which correctly handle absent concepts and concepts with temporal attributes).
	 * As part of the hybrid-reasoning classification process, the correctly classified Statement Concepts are each moved back into the 
	 * originally created class hierarchy in the ElkReasoner object.  This hierarchy (taxonomy) is thenceforth the data struture on which
	 * all of the ElkReasonerSHIELD's methods operate.
	 * Note that the Statement Concepts that had been originally (incorrectly) classified in the class hierarchy of the ElkReasoner object 
	 * get removed from that hierarchy prior to the addition of the correctly classified Statement Concepts.
	 */
	@Override
	protected void reCreateReasoner() {
		this.reasoner_ = new ReasonerFactory().createReasoner(
				new OwlOntologyLoader(owlOntology_, this.mainProgressMonitor_),
				stageExecutor_, config_);
		this.reasoner_.setAllowFreshEntities(isAllowFreshEntities);
		// use the secondary progress monitor by default, when necessary, we
		// switch to the primary progress monitor; this is to avoid bugs with
		// progress monitors in Protege
		this.reasoner_.setProgressMonitor(this.secondaryProgressMonitor_);
		
		OWLOntology statementOntology = createEmptyOwlOntology(OWLManager.createOWLOntologyManager());  // Will be populated with statement axioms by moveStatementAxioms
		
		// The statement axioms (rooted at the first parameter of the call) are all moved from the loadedOntology
		// into the statementOntology.  The resulting loadedOntology is assigned the name kernelOntology.  It will
		// ultimately also contain the correctly classified statement concepts.  Initially, it only
		// contains kernel concepts.
// 		moveStatementAxioms(DefaultProperties.STATEMENT_CONCEPT_NAMESPACE, DefaultProperties.STATEMENT_CONCEPT_NAME, owlOntology_, statementOntology, false, OntologyType.SOURCE);
 		int numStatementAxiomsMoved = moveStatementAxioms(DefaultProperties.STATEMENT_CONCEPT_NAMESPACE, DefaultProperties.STATEMENT_CONCEPT_NAME, owlOntology_, statementOntology);
 		if (numStatementAxiomsMoved == 0) 	
 			System.out.println("WARNING:  No Statement Concepts found in ontology (looking for IRI = " + DefaultProperties.STATEMENT_CONCEPT_NAMESPACE + "#" + DefaultProperties.STATEMENT_CONCEPT_NAME);
 		else {
//DEBUG System.out.println("REASONER: Completed moving of axioms from loaded to statement ontology");	
 		
 			// The kernel ontology is loaded into and classified by a standard ElkReasoner.  This classification
 			// of all kernel concepts is needed for the subsequent classification of the statement concepts
 			// within the kernel reasoner's taxonomy (because statement concepts are defined with respect to
 			// kernel concepts)
//DEBUG System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN reCreateReasoner");
//DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) this, false);

//System.out.println("ORIGINAL STATEMENT ONTOLOGY - IN reCreateReasoner: ");
//System.out.println(statementOntology); 

//DEBUG System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN reCreateReasoner");
//DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);
	
 			// A StrucuralReasoner instance is created to represent the stated hierarchy for the 
 			// statementOntology. This hierarchy is needed for the later classification of the statement 
 			// concepts into the kernel reasoner's taxonomy.
 			StructuralReasonerFactory structuralReasonerFactory = new StructuralReasonerFactory(); 
 			OWLReasoner statementOwlReasoner = structuralReasonerFactory.createNonBufferingReasoner(statementOntology);
		
//DEBUG System.out.println("ORIGINAL STATEMENT REASONER TAXONOMY - IN reCreateReasoner");
//DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((StructuralReasoner) statementOwlReasoner, false);
		
 			// Instantiation of a StatementClassifierSHIELD classifier creates subsumption-normal-form 
 			// representations of each statement in the statementOntology.  These representations are 
 			// needed by the subsequent classifyStatementConcepts operation.
 			this.classifier = new StatementClassifierSHIELD(this.owlOntology_, statementOntology, this, statementOwlReasoner,
                                                        	DefaultProperties.STATEMENT_CONCEPT_NAMESPACE, DefaultProperties.STATEMENT_CONCEPT_NAME,
                                                        	DefaultProperties.TEMPORAL_ANNOTATION_OWL_IRI, DefaultProperties.ABSENCE_NAMESPACE, 
                                                        	DefaultProperties.ABSENCE_PROPERTY, DefaultProperties.ABSENCE_VALUE);
 			classifier.classifyStatementConcepts(this.owlOntology_, statementOntology, this, statementOwlReasoner);
		
//DEBUG System.out.println("POST-MIGRATION KERNEL REASONER TAXONOMY - IN reCreateReasoner");
//DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) this, false);	
 		}
	}




	
	private static OWLOntology createEmptyOwlOntology(OWLOntologyManager ontologyManager) {
		OWLOntology ontology = null;
		try {
			ontology = ontologyManager.createOntology();
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			System.out.println("A null ontology was returned from createEmptyOwlOntology");
			e.printStackTrace();
		}
		return ontology;
	}
	
	private synchronized static Set<OWLClass> buildSubClassSet(String conceptNamespace, String conceptName, OWLOntology ontology, OWLReasoner reasoner, boolean includeRootClass)  {
		OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
	    OWLClass superClass = factory.getOWLClass(
	       IRI.create(conceptNamespace + "#" + conceptName));
	    Set<OWLClass> subClassSet = reasoner.getSubClasses(superClass, false).getFlattened();
	    if (includeRootClass)  {
	        subClassSet.add(superClass);  // include the superclass concept also
	    }
	    return subClassSet;
		}

	static ElkReasonerConfiguration convertToElkConfig(OWLReasonerConfiguration owlConfig) {
//DEBUG System.out.println("OWLReasonerConfiguration: " + owlConfig);
		ElkReasonerConfiguration elkReasonerConfig;
		if (owlConfig != null) {
			if (owlConfig instanceof ElkReasonerConfiguration) {
				elkReasonerConfig = (ElkReasonerConfiguration) owlConfig;
			} else {
				elkReasonerConfig = new ElkReasonerConfiguration(owlConfig);
			}
		} else {
			elkReasonerConfig = new ElkReasonerConfiguration();
		}
//DEBUG System.out.println("ELKReasonerConfiguration Num Workers: " + elkReasonerConfig.getElkConfiguration().getConfiguration().getParameter(ReasonerConfiguration.NUM_OF_WORKING_THREADS));
		return elkReasonerConfig;
	}
	
	@Override
	public String getReasonerName() {
		return this.getClass().toString();
	}
	
	@Override
	public Version getReasonerVersion() {
		return new Version(0, 1, 0, 0);  //TODO:  Change this to dynamically retrieve the version from the build (?)
	}
	
	
//	private enum OntologyType {
//	    SOURCE,
//	    DESTINATION
//	  }
	
	

}
