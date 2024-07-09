package gov.hhs.fda.shield;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.elk.owlapi.ElkReasoner;
import org.semanticweb.elk.owlapi.ElkReasonerConfiguration;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.elk.owlapi.OwlOntologyLoader;
import org.semanticweb.elk.reasoner.ReasonerFactory;
import org.semanticweb.elk.reasoner.config.ReasonerConfiguration;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.IllegalConfigurationException;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElkReasonerSHIELD extends ElkReasoner {

	private static final Logger LOGGER_ = LoggerFactory.getLogger(SubsumptionNormalFormBuilderSHIELD.class);

	private boolean isBufferingMode_;
	private OWLReasonerConfiguration owlReasonerConfig_;  
	private StatementClassifierSHIELD classifier; 

	public void main(String[] args)  {
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		try {
			OWLOntology originalOnt = ontologyManager.loadOntologyFromOntologyDocument(
					new File("C:/Users/wsuja/Dropbox/FDA/Research/SWEC-Classification/SWEC-Ontology-Example.owx"));
// TEST System.out.println("THE ORIGINAL ONTOLOGY");
// TEST System.out.println(originalOnt);

			OWLReasonerFactory reasonerFactory = new ElkReasonerFactorySHIELD();
			OWLReasoner reasonerSHIELD = reasonerFactory.createNonBufferingReasoner(originalOnt);
			} catch (OWLOntologyCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
	public ElkReasonerSHIELD (OWLOntology loadedOntology, boolean isBufferingMode, OWLReasonerConfiguration config) throws IllegalConfigurationException {                        
/* TEST IN */	super(loadedOntology,
				                     isBufferingMode, 
				                     convertToElkConfig(config));
		this.isBufferingMode_ = isBufferingMode;
		this.owlReasonerConfig_ = config;  
//System.out.println("REASONER: Completed creation of ElkReasoner super class");
		
		// An empty OWLOntology instance is created to subsequently (and temporarily) hold the statement
		// axioms that will be moved from the loadedOntology.
// NOW DONE IN recreateReasoner		OWLOntology statementOntology = createEmptyOwlOntology(OWLManager.createOWLOntologyManager());  // Will be populated with statement axioms by moveStatementAxioms

//System.out.println("REASONER: Completed creation of empty OWLOntology");	
		
		// The statement axioms (rooted at the first parameter of the call) are all moved from the loadedOntology
		// into the statementOntology.  The resulting loadedOntology is assigned the name kernelOntology.  It will
		// ultimately also contain the correctly classified statement concepts.  Initially, it only
		// contains kernel concepts.
// JUST REMOVED loadedOntology.getOWLOntologyManager().removeOntology(owlOntology_);
/*TEST	OUT   this.owlOntology_ = moveStatementAxioms("Statement-Concept", loadedOntology, statementOntology, true, OntologyType.SOURCE);
*/
// NOW DONE IN recreateReasoner 		moveStatementAxioms("Statement-Concept", loadedOntology, statementOntology, false, OntologyType.SOURCE);

//System.out.println("REASONER: Completed moving of axioms from loaded to statement ontology");	
		// The kernel ontology is loaded into and classified by a standard ElkReasoner.  This classification
		// of all kernel concepts is needed for the subsequent classification of the statement concepts
		// within the kernel reasoner's taxonomy (because statement concepts are defined with respect to
		// kernel concepts)
// NOW DONE IN recreateReasoner		ElkReasonerFactory elkReasonerFactory = new ElkReasonerFactory(); 
// NOW DONE IN recreateReasoner		ElkReasoner kernelElkReasoner = (ElkReasoner) elkReasonerFactory.createElkReasoner(this.owlOntology_,
		// NOW DONE IN				                              isBufferingMode, 
		// NOW DONE IN                                            config);	
//System.out.println("REASONER: Completed creation of ElkReasoner for kernelOntology");
// DEBUG System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN CONSTRUCTOR");
// DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);
// DEBUG  System.out.println("ORIGINAL KERNEL ONTOLOGY - IN CONSTRUCTOR: ");
// DEBUG  System.out.println(this.owlOntology_); 

//DEBUG System.out.println("ORIGINAL STATEMENT ONTOLOGY - IN CONSTRUCTOR: ");
//DEBUG System.out.println(statementOntology); 

// DEBUG System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN CONSTRUCTOR");
// DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);
	
		// A StrucuralReasoner instance is created to represent the stated hierarchy for the 
		// statementOntology. This hierarchy is needed for the later classification of the statement 
		// concepts into the kernel reasoner's taxonomy.
// NOW DONE IN recreateReasoner		StructuralReasonerFactory structuralReasonerFactory = new StructuralReasonerFactory(); 
// NOW DONE IN recreateReasoner		OWLReasoner statementOwlReasoner = structuralReasonerFactory.createNonBufferingReasoner(statementOntology);
		
// DEBUG System.out.println("ORIGINAL STATEMENT REASONER TAXONOMY - IN CONSTRUCTOR");
// DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((StructuralReasoner) statementOwlReasoner, false);
		
		// Instantiation of a StatementClassifierSHIELD classifier creates subsumption-normal-form 
		// representations of each statement in the statementOntology.  These representations are 
		// needed by the subsequent classifyStatementConcepts operation.
// NOW DONE IN recreateReasoner		this.classifier = new StatementClassifierSHIELD(this.owlOntology_, statementOntology, kernelElkReasoner, statementOwlReasoner);
// NOW DONE IN recreateReasoner		classifier.classifyStatementConcepts(this.owlOntology_, statementOntology, kernelElkReasoner, statementOwlReasoner);
		// After all classification, move the statement axioms back to the originalOntology, because it has been
		// set as the rootOntology of the ElkReasonerSHIELD instance (so the originally loaded ontology is returned by 
		// the inherited ElkReasoner.getRootOntology()) method.  THIS REQUIRES THE ELK REASONER CREATED FOR THE KERNEL
		// ONTOLOGY TO BE BUFFERING! )
/* TEST OUT 		this.owlOntology_ = moveStatementAxioms("Statement-Concept", statementOntology, this.owlOntology_, false, OntologyType.DESTINATION);	
*/
		// DEBUG System.out.println("REASONER'S FINAL ROOT ONTOLOGY - IN CONSTRUCTOR: ");
		// DEBUG System.out.println(this.getRootOntology());  // Test to see if the root ontology gets modified back to original ontology
		
		// After all statements have been classified within the taxonomy of the kernelReasoner, we set
		// it as the internal reasoner for this instance of ElkReasonerSHIELD.  
//statementOwlReasoner.dispose();
// NOW DONE IN recreateReasoner		this.reasoner_ = kernelElkReasoner.getInternalReasoner();
		
//System.out.println("POST-MIGRATION KERNEL REASONER TAXONOMY - IN CONSTRUCTOR");
//ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) this, false);		
	}	

	
	// Removes all axioms defining classes that are sub-types of the 
	// targetClassName from the sourceOntology and puts them in the destination ontology
/* TEST OUT	public synchronized OWLOntology moveStatementAxioms(String targetClassName, OWLOntology sourceOntology, OWLOntology destinationOntology, Boolean removeFromSourceOntology, OntologyType ontologyToReturn) {
*/

/* TEST IN */	public synchronized void moveStatementAxioms(String targetClassName, OWLOntology sourceOntology, OWLOntology destinationOntology, Boolean removeFromSourceOntology, OntologyType ontologyToReturn) {
		OWLOntologyManager ontologyManager = destinationOntology.getOWLOntologyManager();  // Very important that it's OWLOntologyManager from *desintationOntology*, not sourceOntology
	//		OWLOntologyManager ontologyManager = sourceOntology.getOWLOntologyManager();
		StructuralReasonerFactory structuralReasonerFactory = new StructuralReasonerFactory();
		OWLReasoner structuralReasoner = structuralReasonerFactory.createNonBufferingReasoner(sourceOntology);
		Set<OWLClass> subClassSet = ReasonerExplorer.buildSubClassSet(targetClassName, sourceOntology, structuralReasoner, true); // ORIG
		for (OWLClass owlClass : subClassSet) {  //NEW
			Set<OWLClassAxiom> defnAxioms = sourceOntology.getAxioms(owlClass,Imports.INCLUDED);		
				for (Iterator iterator = defnAxioms.iterator(); iterator.hasNext();) {  
					OWLAxiom owlAxiom = (OWLAxiom) iterator.next();  
System.out.println("REASONER: Removing/Adding the  Definition Axiom for: " + owlClass);
					if (removeFromSourceOntology) {
						ontologyManager.removeAxiom(sourceOntology, owlAxiom);
System.out.println("REASONER: Removed");
					}
					ontologyManager.addAxiom(destinationOntology, owlAxiom);
System.out.println("REASONER: Added");
				}
				Set<OWLDeclarationAxiom> declAxioms = sourceOntology.getDeclarationAxioms(owlClass);	//NEW
System.out.println("REASONER: Got declaratons for: " + owlClass);
				for (Iterator iterator = declAxioms.iterator(); iterator.hasNext();) {  //NEW
					OWLAxiom owlAxiom = (OWLAxiom) iterator.next(); 
System.out.println("REASONER: Removing/Adding the  Declaration Axiom for: " + owlClass);
					if (removeFromSourceOntology) {
						ontologyManager.removeAxiom(sourceOntology, owlAxiom);
					}
					ontologyManager.addAxiom(destinationOntology, owlAxiom);
				}
		}
/* TEST OUT	if (ontologyToReturn == OntologyType.SOURCE) 
		return sourceOntology;
	else 
		return destinationOntology;
 */
}
	

	
	// *** OLD VERSION - NOT USED **** Removes all axioms defining classes that are sub-types of the 
	// targetClassName from the sourceOntology and puts them in the destination ontology
	public OWLOntology moveStatementAxioms_ORIG(String targetClassName, OWLOntology sourceOntology, OWLOntology destinationOntology, Boolean removeFromSourceOntology, OntologyType ontologyToReturn) {
		OWLOntologyManager ontologyManager = sourceOntology.getOWLOntologyManager();
		StructuralReasonerFactory structuralReasonerFactory = new StructuralReasonerFactory();
		OWLReasoner structuralReasoner = structuralReasonerFactory.createNonBufferingReasoner(sourceOntology);
		Set<OWLAxiom> axioms = sourceOntology.getAxioms(Imports.INCLUDED);  // ORIG
		Set<OWLClass> subClassSet = ReasonerExplorer.buildSubClassSet(targetClassName, sourceOntology, structuralReasoner, true); // ORIG
		for (Iterator iterator = axioms.iterator(); iterator.hasNext();) {  // ORIG
		OWLAxiom owlAxiom = (OWLAxiom) iterator.next();  // ORIG
			if (owlAxiom instanceof OWLEquivalentClassesAxiom) {
				Set<OWLClassExpression> class_exprs = ((OWLEquivalentClassesAxiom) owlAxiom).getClassExpressions();
				for (Iterator iterator2 = class_exprs.iterator(); iterator2.hasNext();) {
					OWLClassExpression owlClassExpression = (OWLClassExpression) iterator2.next();
					if (owlClassExpression instanceof OWLClass) {
						if (subClassSet.contains(owlClassExpression)) {
// DEBUG	System.out.println("Removing/Adding the OWLEquivalentClassesAxiom for: " + owlClassExpression);
							if (removeFromSourceOntology) {
								ontologyManager.removeAxiom(sourceOntology, owlAxiom);
							}
							ontologyManager.addAxiom(destinationOntology, owlAxiom);
						}
					}
				}
			}
			else if (owlAxiom instanceof OWLDeclarationAxiom) {
				Set<OWLClass> declaredClasses = owlAxiom.getClassesInSignature();
				for (Iterator iterator3 = declaredClasses.iterator(); iterator3.hasNext();) {
					OWLClass owlClass = (OWLClass) iterator3.next();
						if (subClassSet.contains(owlClass)) {
// DEBUG	System.out.println("Removing/Adding the OWLDeclarationAxiom for: " + owlClass);
							ontologyManager.addAxiom(destinationOntology, owlAxiom);
							if (removeFromSourceOntology) {
								ontologyManager.removeAxiom(sourceOntology, owlAxiom);
							}

						}
					}
			}
			else if (owlAxiom instanceof OWLSubClassOfAxiom) {
				OWLClassExpression owlClass = ((OWLSubClassOfAxiom) owlAxiom).getSubClass();
					if (subClassSet.contains(owlClass)) {
// DEBUG	System.out.println("Removing/Adding the OWLSubClassOfAxiom for: " + owlClass);
						ontologyManager.addAxiom(destinationOntology, owlAxiom);
						if (removeFromSourceOntology) {
							ontologyManager.removeAxiom(sourceOntology, owlAxiom);
						}
					}			
			}
		}
		if (ontologyToReturn == OntologyType.SOURCE) 
			return sourceOntology;
		else 
			return destinationOntology;
	}
	

	public void precomputeInferences(InferenceType... inferenceTypes)
			throws ReasonerInterruptedException, TimeOutException,
			InconsistentOntologyException {
/** DO NOTHING, SO THE REASONER IS NOT MESSED UP WHEN STARTED BY PROTEGE... **/
 		if (LOGGER_.isDebugEnabled())
 
			LOGGER_.debug("precomputeInferences(InferenceType...)");
// ?Implement		checkInterrupted();
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
/*****/
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
 		moveStatementAxioms("Statement-Concept", owlOntology_, statementOntology, false, OntologyType.SOURCE);

 		// DEBUG System.out.println("REASONER IN preComputeHierarchy: Completed moving of axioms from loaded to statement ontology");	
 		
		// The kernel ontology is loaded into and classified by a standard ElkReasoner.  This classification
		// of all kernel concepts is needed for the subsequent classification of the statement concepts
		// within the kernel reasoner's taxonomy (because statement concepts are defined with respect to
		// kernel concepts)
		// NOT NEEDED ElkReasonerFactory elkReasonerFactory = new ElkReasonerFactory(); 
		// NOT NEEDED ElkReasoner kernelElkReasoner = (ElkReasoner) elkReasonerFactory.createElkReasoner(this.owlOntology_,
		//		                                                                           isBufferingMode, 
		//		                                                                           config);	
System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN preComputeHierarchy");
ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) this, false);

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
		this.classifier = new StatementClassifierSHIELD(this.owlOntology_, statementOntology, this, statementOwlReasoner);
		classifier.classifyStatementConcepts(this.owlOntology_, statementOntology, this, statementOwlReasoner);
		
		// After all statements have been classified within the taxonomy of the kernelReasoner, we set
		// it as the internal reasoner for this instance of ElkReasonerSHIELD.  
//statementOwlReasoner.dispose();
//		this.reasoner_ = kernelElkReasoner.getInternalReasoner();
		
System.out.println("POST-MIGRATION KERNEL REASONER TAXONOMY - IN preComputeHierarchy");
ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) this, false);		

/*** ORIGINAL VERSION  
		OWLOntology statementOntology = createEmptyOwlOntology(owlOntology_.getOWLOntologyManager());  // Will be populated with statement axioms by moveStatementAxioms
		moveStatementAxioms("Statement-Concept", owlOntology_, statementOntology, true, OntologyType.SOURCE);
		ElkReasonerFactory elkReasonerFactory = new ElkReasonerFactory(); 
		ElkReasoner kernelElkReasoner = (ElkReasoner) elkReasonerFactory.createElkReasoner(owlOntology_,
				                                                                           isBufferingMode_, 
				                                                                           owlReasonerConfig_);
// DEBUG		System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN PRECOMPUTE");
// DEBUG		ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);
			
				StructuralReasonerFactory structuralReasonerFactory = new StructuralReasonerFactory(); 
				OWLReasoner statementOwlReasoner = structuralReasonerFactory.createNonBufferingReasoner(statementOntology);
				
// DEBUG		System.out.println("ORIGINAL STATEMENT REASONER TAXONOMY - IN PRECOMPUTE");
// DEBUG		ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);

		classifier.classifyStatementConcepts(owlOntology_, statementOntology, kernelElkReasoner, statementOwlReasoner);
// NOT NEEDED		this.owlOntology_ = moveStatementAxioms("Statement-Concept", statementOntology, kernelOntology, false, OntologyType.DESTINATION);	
		// Above so that OWLReasoner.getRootOntology() will work correctly

// DEBUG				System.out.println("REASONER'S FINAL ROOT ONTOLOGY - IN PRECOMPUTE: ");
// DEBUG				System.out.println(this.getRootOntology());  // Test to see if the root ontology gets modified back to original ontology

				this.reasoner_ = kernelElkReasoner.getInternalReasoner();
				
// DEBUG				System.out.println("POST-RECOMPUTATION KERNEL REASONER TAXONOMY - IN PRECOMPUTE");
// DEBUG				ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) this, false);	
	
   END ORIGINAL VERSION  ***/
	}
	
	
	/***   TODO:  Change back to protected after testing; this just needs to be called by flush(), I believe  */
	@Override
	public void reCreateReasoner() {
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
 		moveStatementAxioms("Statement-Concept", owlOntology_, statementOntology, false, OntologyType.SOURCE);
System.out.println("REASONER: Completed moving of axioms from loaded to statement ontology");	
 		
		// The kernel ontology is loaded into and classified by a standard ElkReasoner.  This classification
		// of all kernel concepts is needed for the subsequent classification of the statement concepts
		// within the kernel reasoner's taxonomy (because statement concepts are defined with respect to
		// kernel concepts)
		// NOT NEEDED ElkReasonerFactory elkReasonerFactory = new ElkReasonerFactory(); 
		// NOT NEEDED ElkReasoner kernelElkReasoner = (ElkReasoner) elkReasonerFactory.createElkReasoner(this.owlOntology_,
		//		                                                                           isBufferingMode, 
		//		                                                                           config);	
//NOT NEEDED System.out.println("REASONER: Completed creation of ElkReasoner for kernelOntology");
System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN reCreateReasoner");
ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) this, false);

//DEBUG System.out.println("ORIGINAL STATEMENT ONTOLOGY - IN reCreateReasoner: ");
//DEBUG System.out.println(statementOntology); 

// DEBUG System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN reCreateReasoner");
// DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);
	
		// A StrucuralReasoner instance is created to represent the stated hierarchy for the 
		// statementOntology. This hierarchy is needed for the later classification of the statement 
		// concepts into the kernel reasoner's taxonomy.
		StructuralReasonerFactory structuralReasonerFactory = new StructuralReasonerFactory(); 
		OWLReasoner statementOwlReasoner = structuralReasonerFactory.createNonBufferingReasoner(statementOntology);
		
//System.out.println("ORIGINAL STATEMENT REASONER TAXONOMY - IN reCreateReasoner");
//ReasonerExplorer.printCurrentReasonerTaxonomy((StructuralReasoner) statementOwlReasoner, false);
		
		// Instantiation of a StatementClassifierSHIELD classifier creates subsumption-normal-form 
		// representations of each statement in the statementOntology.  These representations are 
		// needed by the subsequent classifyStatementConcepts operation.
		this.classifier = new StatementClassifierSHIELD(this.owlOntology_, statementOntology, this, statementOwlReasoner);
		classifier.classifyStatementConcepts(this.owlOntology_, statementOntology, this, statementOwlReasoner);
		
		// After all statements have been classified within the taxonomy of the kernelReasoner, we set
		// it as the internal reasoner for this instance of ElkReasonerSHIELD.  
//statementOwlReasoner.dispose();
//		this.reasoner_ = kernelElkReasoner.getInternalReasoner();
		
System.out.println("POST-MIGRATION KERNEL REASONER TAXONOMY - IN reCreateReasoner");
ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) this, false);		

		

/*** NO OP so it will not be called by the constructor for the superclass ElkReasoner (which is called by the constructor for this class)
		OWLOntology statementOntology = createEmptyOwlOntology(owlOntology_.getOWLOntologyManager());  // Will be populated with statement axioms by moveStatementAxioms
		OWLOntology kernelOntology = moveStatementAxioms("Statement-Concept", owlOntology_, statementOntology, true, OntologyType.SOURCE);
		ElkReasonerFactory elkReasonerFactory = new ElkReasonerFactory(); 
		ElkReasoner kernelElkReasoner = (ElkReasoner) elkReasonerFactory.createElkReasoner(kernelOntology,
				                                                                           isBufferingMode_, 
				                                                                           config_);
// DEBUG		System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN RECREATE");
// DEBUG		ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);
			
				StructuralReasonerFactory structuralReasonerFactory = new StructuralReasonerFactory(); 
				OWLReasoner statementOwlReasoner = structuralReasonerFactory.createNonBufferingReasoner(statementOntology);
				
// DEBUG		System.out.println("ORIGINAL STATEMENT REASONER TAXONOMY - IN RECREATE");
// DEBUG		ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);

		if (classifier != null)
			classifier.classifyStatementConcepts(kernelOntology, statementOntology, kernelElkReasoner, statementOwlReasoner);
		this.owlOntology_ = moveStatementAxioms("Statement-Concept", statementOntology, kernelOntology, false, OntologyType.DESTINATION);	

// DEBUG		System.out.println("REASONER'S FINAL ROOT ONTOLOGY - IN RECREATE: ");
// DEBUG		System.out.println(this.getRootOntology());  // Test to see if the root ontology gets modified back to original ontology

				this.reasoner_ = kernelElkReasoner.getInternalReasoner();
				
// DEBUG		System.out.println("POST-MIGRATION KERNEL REASONER TAXONOMY - IN RECREATE");
// DEBUG		ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) this, false);		

	
		this.reasoner_ = kernelElkReasoner.getInternalReasoner();
		this.reasoner_.setAllowFreshEntities(isAllowFreshEntities);
		// use the secondary progress monitor by default, when necessary, we
		// switch to the primary progress monitor; this is to avoid bugs with
		// progress monitors in Protege
		this.reasoner_.setProgressMonitor(this.secondaryProgressMonitor_);
****/
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

	static ElkReasonerConfiguration convertToElkConfig(OWLReasonerConfiguration owlConfig) {
//System.out.println("OWLReasonerConfiguration: " + owlConfig);
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
//System.out.println("ELKReasonerConfiguration Num Workers: " + elkReasonerConfig.getElkConfiguration().getConfiguration().getParameter(ReasonerConfiguration.NUM_OF_WORKING_THREADS));
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
	
	
	private enum OntologyType {
	    SOURCE,
	    DESTINATION
	  }
	
	

}
