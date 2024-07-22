package gov.hhs.fda.shield;

import java.io.File;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class ElkReasonerSHIELDOwlApiTester {

	public ElkReasonerSHIELDOwlApiTester() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		
		OWLOntology ontology = null;
		String ontologyNamespace = "http://www.hhs.fda.org/shield/SWEC-Ontology";  
		
		// Instantiate OntologyManager
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		
		// Load the ontology.
		try {
			ontology = ontologyManager.loadOntologyFromOntologyDocument(new File("C:/Users/wsuja/Documents/ConsultingEngagements/FDA/Research/SWEC-Classification/SWEC-Ontology-Example-9.owx"));
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Create ElkReasonerSHIELD
		ElkReasonerFactorySHIELD reasonerFactory = new ElkReasonerFactorySHIELD();
		OWLReasoner reasoner = reasonerFactory.createBufferingReasoner(ontology);
		
		// Create ElkReasoner (for comparison testing)
//		ElkReasonerFactory reasonerFactory = new ElkReasonerFactory();
//		OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
		
		
		// Print out the classified taxonomy
		System.out.println("SHIELD REASONER CLASSIFIED TAXONOMY");
		ReasonerExplorer.printCurrentReasonerTaxonomySHIELD(reasoner, false);

		
		// Test class subsumption in the reasoner's final classified taxonomy
		CustomSubsumptionTesterSHIELD tester = new CustomSubsumptionTesterSHIELD(ontology);

// Perform a specific subsumption test on any class expression using CustomSubsumptionTesterSHIELD
		String superClassName = "No_family_history_Angina-situation-";
		String subClassName = "No-family-history-Cardiovascular-disease-situation";
		OWLClass superClass = buildOWLClassSWEC(ontologyNamespace, superClassName, ontologyManager);
		OWLClass subClass = buildOWLClassSWEC(ontologyNamespace, subClassName, ontologyManager);
		System.out.println("Does " + superClass + " subsume " + subClass + ": " +
		        tester.isSubsumedBy((OWLClassExpression) subClass, (OWLClassExpression) superClass, reasoner));
		
// dispose()  [Note:  Will shut down the internal Reasoner instance in 1 minute]
		System.out.println("running dispose()...");
		reasoner.dispose();
		
// flush		
		System.out.println("running flush()...");
		reasoner.flush();
		
		
// getBottomClassNode		
		System.out.println("getBottomClassNode(): " + reasoner.getBottomClassNode());

// getBottomDataPropertyNode
		// NOT IMPLEMENTED BY ELK REASONER
		//System.out.println("getBottomDataPropertyNode(): " + reasonerSHIELD.getBottomDataPropertyNode());

// getBottomObjectPropertyNode
		// NOT IMPLEMENTED BY ELK REASONER
		//System.out.println("getBottomObjectPropertyNode(): " + reasonerSHIELD.getBottomObjectPropertyNode());

// getBufferingMode
		System.out.println("getBufferingMode(): " + reasoner.getBufferingMode());
		
// getFreshEntityPolicy()
		System.out.println("getFreshEntityPolicy(): " + reasoner.getFreshEntityPolicy());

// getIndividualNodeSetPolicy()
		System.out.println("getIndividualNodeSetPolicy(): " + reasoner.getIndividualNodeSetPolicy());
				
// getInstances  
		OWLClassExpression classExpression = (OWLClassExpression) buildOWLClassSWEC(ontologyNamespace, "PRESENT_History-of-Asthma-in-Natural-Brother-Recently", ontologyManager);
		System.out.println();
		System.out.println("getInstances(" + classExpression + ")[Direct Only]: " 
				+ reasoner.getInstances(classExpression, true));
		System.out.println("getInstances(" + classExpression + ")[ALL]: " 
				+ reasoner.getInstances(classExpression, false));

		classExpression = (OWLClassExpression) buildOWLClassSWEC(ontologyNamespace, "PRESENT_FH-of-Respiratory-Disease-Past-or-Current", ontologyManager);		
		System.out.println();
		System.out.println("getInstances(" + classExpression + ")[Direct Only]: " 
				+ reasoner.getInstances(classExpression, true));
		System.out.println("getInstances(" + classExpression + ")[ALL]: " 
				+ reasoner.getInstances(classExpression, false));
		
		classExpression = (OWLClassExpression) buildOWLClassSWEC(ontologyNamespace, "Natural-Brother", ontologyManager);
		System.out.println();
		System.out.println("getInstances(" + classExpression + ")[Direct Only]: " 
				+ reasoner.getInstances(classExpression, true));
		System.out.println("getInstances(" + classExpression + ")[ALL]: " 
				+ reasoner.getInstances(classExpression, false));

		classExpression = (OWLClassExpression) buildOWLClassSWEC(ontologyNamespace, "Person", ontologyManager);
		System.out.println();
		System.out.println("getInstances(" + classExpression + ")[Direct Only]: " 
				+ reasoner.getInstances(classExpression, true));
		System.out.println("getInstances(" + classExpression + ")[ALL]: " 
				+ reasoner.getInstances(classExpression, false));


		
// getPendingAxiomAdditions()
		System.out.println("getPendingAxiomAdditions(): " + reasoner.getPendingAxiomAdditions());
		
// getPendingAxiomRemovals()
		System.out.println("getPendingAxiomRemovals(): " + reasoner.getPendingAxiomRemovals());
				
// getPendingChanges()
		System.out.println("getPendingChanges(): " + reasoner.getPendingChanges());
		
// getPrecomputableInferenceTypes
		System.out.println("getPrecomputableInferenceTypes(): " + reasoner.getPrecomputableInferenceTypes());
		
// getReasonerName
		System.out.println("getReasonerName(): " + reasoner.getReasonerName());
		//Note that getReasonerName() returns null for the ElkReasoner also...
		

// getReasonerVersion
		System.out.println("getReasonerVersion(): " + reasoner.getReasonerVersion());

// getRootOntology
		System.out.println("getRootOntology(): " + reasoner.getRootOntology());
		
// getSubClasses
		OWLClassExpression subClassExpression = (OWLClassExpression) buildOWLClassSWEC(ontologyNamespace, "No-family-history-Ischemic-heart-disease-situation", ontologyManager);
		System.out.println("getSubClasses("  + subClassExpression + ") [Direct Only]: " + 
		                    reasoner.getSubClasses(subClassExpression, true));
		System.out.println("getSubClasses("  + subClassExpression + ") [All]: " + 
                reasoner.getSubClasses(subClassExpression, false));

// getSuperClasses
		OWLClassExpression superClassExpression = (OWLClassExpression) buildOWLClassSWEC(ontologyNamespace, "No-family-history-Ischemic-heart-disease-situation", ontologyManager);
		System.out.println("getSuperClasses("  + superClassExpression + ")[Direct Only]: " + 
		                    reasoner.getSuperClasses(superClassExpression, true));
		System.out.println("getSuperClasses("  + superClassExpression + ")[All]: " + 
                reasoner.getSuperClasses(superClassExpression, false));

// getTimeOut
		System.out.println("getTimeOut(): " + reasoner.getTimeOut());

// getTopClassNode
		System.out.println("getTopClassNode(): " + reasoner.getTopClassNode());
		
// getTopDataPropertyNode
		// NOT IMPLEMENTED BY ELK REASONER
		// System.out.println("getTopDataPropertyNode(): " + reasoner.getTopDataPropertyNode());

// getTopObjectPropertyNode
		// NOT IMPLEMENTED BY ELK REASONER
		//System.out.println("getTopObjectPropertyNode(): " + reasoner.getTopObjectPropertyNode());

// getTypes
		OWLNamedIndividual namedIndiv = buildOWLIndividualSWEC(ontologyNamespace, "JohnDoesAsthma", ontologyManager);
		System.out.println("getTypes(" + namedIndiv + ")[Direct Only]: " 
				+ reasoner.getTypes(namedIndiv, true));
		System.out.println("getTypes(" + namedIndiv + ")[All]: " 
				+ reasoner.getTypes(namedIndiv, false));
		
// getUnsatisfiableClass
		System.out.println("getUnsatisfiableClasses(): " + reasoner.getUnsatisfiableClasses());
		
// isConsistent
		System.out.println("isConsistent(): " + reasoner.isConsistent());

// isEntailmentCheckingSupported
		boolean isAnyEntailmentCheckingSupported = false;
		AxiomType testedAxiomType = null;
		Set<AxiomType> axiomTypes = Set.of(
				AxiomType.SUBCLASS_OF, 
				AxiomType.EQUIVALENT_CLASSES, 
				AxiomType.DISJOINT_CLASSES, 
				AxiomType.OBJECT_PROPERTY_DOMAIN, 
				AxiomType.OBJECT_PROPERTY_RANGE,
				AxiomType.FUNCTIONAL_OBJECT_PROPERTY, 
				AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY, 
				AxiomType.DATA_PROPERTY_DOMAIN, 
				AxiomType.DATA_PROPERTY_RANGE,
				AxiomType.FUNCTIONAL_DATA_PROPERTY, 
				AxiomType.DATATYPE_DEFINITION, 
				AxiomType.DISJOINT_UNION, 
				AxiomType.HAS_KEY,
				AxiomType.CLASS_ASSERTION, 
				AxiomType.SAME_INDIVIDUAL, 
				AxiomType.DIFFERENT_INDIVIDUALS, 
				AxiomType.OBJECT_PROPERTY_ASSERTION,
				AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION, 
				AxiomType.DATA_PROPERTY_ASSERTION, 
				AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION,
				AxiomType.TRANSITIVE_OBJECT_PROPERTY, 
				AxiomType.DISJOINT_DATA_PROPERTIES, 
				AxiomType.SUB_DATA_PROPERTY,
				AxiomType.EQUIVALENT_DATA_PROPERTIES, 
				AxiomType.DISJOINT_OBJECT_PROPERTIES, 
				AxiomType.SUB_OBJECT_PROPERTY, 
				AxiomType.EQUIVALENT_OBJECT_PROPERTIES,
				AxiomType.SUB_PROPERTY_CHAIN_OF, 
				AxiomType.INVERSE_OBJECT_PROPERTIES, 
				AxiomType.SYMMETRIC_OBJECT_PROPERTY, 
				AxiomType.ASYMMETRIC_OBJECT_PROPERTY,
				AxiomType.REFLEXIVE_OBJECT_PROPERTY, 
				AxiomType.IRREFLEXIVE_OBJECT_PROPERTY);
		for (AxiomType axiomType : axiomTypes) {
			testedAxiomType = axiomType;
			isAnyEntailmentCheckingSupported = reasoner.isEntailmentCheckingSupported(testedAxiomType);
			if (isAnyEntailmentCheckingSupported)
				break;
		}
		System.out.print("Is entailment checking supported for any AxiomTypes?: " + isAnyEntailmentCheckingSupported);
		if (isAnyEntailmentCheckingSupported)
			System.out.println(" (at least for " + testedAxiomType + ")");
		else 
			System.out.println();

// isEntailed()
		// NOT IMPLEMENTED BY ELK REASONER FOR ANY AXIOM TYPES
		//System.out.println("isEntailed(OWLAxiom): " + reasoner.isEntailed(OWLAxiom));
		//System.out.println("isEntailed(Set<OWLAxiom>): " + reasoner.isEntailed(Set<OWLAxiom>));

// isPrecomputed		
		System.out.println("isPrecomputed( InferenceType.CLASS_HIERARCHY ): " + reasoner.isPrecomputed(InferenceType.CLASS_HIERARCHY));
		
// isSatisfiable
				OWLClassExpression classExpr = (OWLClassExpression) buildOWLClassSWEC(ontologyNamespace, "Twin-Brother", ontologyManager);
				System.out.println("isSatisfiable( " + classExpr + "): " + 
		                reasoner.isSatisfiable(classExpr));
				
// precomputeInferences
		System.out.println("precomputeInferences( InferenceType.CLASS_HIERARCHY, InferenceType.OBJECT_PROPERTY_HIERARCHY, InferenceType.DISJOINT_CLASSES )");
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY, 
											InferenceType.OBJECT_PROPERTY_HIERARCHY, 
											InferenceType.DISJOINT_CLASSES);
			System.out.println("precomputeInferences() completed without error");
			

// interrupt()   [SHOULD BE LAST; WILL STOP PROGRAM]
					System.out.println("interrupt()");
					reasoner.interrupt();
					
					
									
	}
	
	public static OWLClass buildOWLClassSWEC(String classNamespace, String className, OWLOntologyManager ontologyManager) {
		return ontologyManager.getOWLDataFactory().getOWLClass(IRI.create(classNamespace + "#" + className));
	}
	
	public static OWLNamedIndividual buildOWLIndividualSWEC(String classNamespace, String IndividualName, OWLOntologyManager ontologyManager) {
		return ontologyManager.getOWLDataFactory().getOWLNamedIndividual(IRI.create(classNamespace + IndividualName));
	}



}
