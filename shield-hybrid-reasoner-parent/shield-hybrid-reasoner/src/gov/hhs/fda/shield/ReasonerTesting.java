package gov.hhs.fda.shield;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;



public class ReasonerTesting {
	
	/****** Class used for various testing during development; can remove it once proper JUnit testing has been implemented ********/

	public ReasonerTesting() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws OWLOntologyStorageException, OWLOntologyCreationException {
///TEMP
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		OWLOntologyManager outputOntologyManager = OWLManager.createOWLOntologyManager();
		OWLOntologyManager statementOntologyManager = OWLManager.createOWLOntologyManager();

		// Load ontology.
		OWLOntology originalOnt = ontologyManager.loadOntologyFromOntologyDocument(
//		new File("C:/Users/wsuja/Documents/ConsultingEngagements/FDA/Research/SWEC-Classification/SWEC-Ontology-Example-PROPERTY-TESTING-YES-CLASSIFIED-NO-ANNOTATION.owx"));
//		new File("C:/Users/wsuja/Documents/ConsultingEngagements/FDA/Research/SWEC-Classification/SWEC-Ontology-Example-PROPERTY-TESTING-NO-CLASSIFIED-YES-ANNOTATION.owx"));
//		new File("C:/Users/wsuja/Documents/ConsultingEngagements/FDA/Research/SWEC-Classification/SWEC-Ontology-Example-PROPERTY-TESTING-NO-CLASSIFIED-YES-ANNOTATION-CHANGED-ONTOLOGY-PROPERTIES.owx"));
		new File("C:/Users/wsuja/Documents/ConsultingEngagements/FDA/Research/SWEC-Classification/SWEC-Ontology-Example-9.owx"));
//		new File("C:/Users/wsuja/Documents/ConsultingEngagements/FDA/Research/SWEC-Classification/SWEC-Ontology-Example-9-CHANGED-ONTOLOGY-PROPERTIES.owx"));
// DEBUG		System.out.println("THE ORIGINAL ONTOLOGY");
// DEBUG		System.out.println(originalOnt);
// DEBUG 		OntologyExplorer.printOntologyParsedAxioms(originalOnt);

/***** CREATE ElkReasoner TEMP *****
		ElkReasonerFactory reasonerFactory = new ElkReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(originalOnt); 
		System.out.println("POST-RECREATION ELK REASONER TAXONOMY");
		ReasonerExplorer.printCurrentReasonerTaxonomySHIELD(reasoner, false);
****/
		
/***** CREATE ElkReasonerSHIELD ******/
		ElkReasonerFactorySHIELD reasonerFactory = new ElkReasonerFactorySHIELD();
		OWLReasoner reasonerSHIELD = reasonerFactory.createBufferingReasoner(originalOnt);  // Only buffering reasoners will be allowed
//System.out.println("POST-INSTANTIATION ElkReasonerSHIELD ELK TAXONOMY");
//ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasonerSHIELD) reasonerSHIELD, false);		
//System.out.println();
		System.out.println("POST-INSTANTIATION ElkReasonerSHIELD OWL TAXONOMY");
		ReasonerExplorer.printOwlTaxonomy(reasonerSHIELD.getTopClassNode(), 0, reasonerSHIELD);
		System.out.println();
		System.out.println("Disposing...");
		reasonerSHIELD.dispose();
		// DEBUG		reasonerSHIELD.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		// DEBUG		System.out.println("POST-RECOMPUTATION KERNEL REASONER TAXONOMY - IN PRECOMPUTE");
		// DEBUG		ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasonerSHIELD) reasonerSHIELD, false);		

		// DEBUG		((ElkReasonerSHIELD) reasonerSHIELD).reCreateReasoner();  // Just for testing; reCreateReasoner is not part of OWLReasoner interface
		// DEBUG		System.out.println("POST-RECREATION KERNEL REASONER TAXONOMY - IN RECREATE");
		// DEBUG		ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasonerSHIELD) reasonerSHIELD, false);		
		
/**************************************/		
		
		
//System.out.println("SHIELD REASONER ONTOLOGY");
//OntologyExplorer.printOntologyAxioms(originalOnt);
//		System.out.println("SHIELD REASONER TAXONOMY");
//		ReasonerExplorer.printCurrentReasonerTaxonomySHIELD(reasonerSHIELD, false);

/** Now tested in the class ElkReasonerSHIELDOwlApiTester
		CustomSubsumptionTesterSHIELD tester = new CustomSubsumptionTesterSHIELD();
		OWLClass superClass = tester.BuildOWLClassSWEC("Statement-of-Absence", ontologyManager);
		OWLClass subClass = tester.BuildOWLClassSWEC("Statement-of-Finding", ontologyManager);
		System.out.println("Does " + superClass + " subsume " + subClass + ": "
				+ tester.classExpressionIsSubsumedBy(subClass, superClass, reasonerSHIELD));
**/
		/*
		 * StructuralReasonerFactory structuralReasonerFactory = new
		 * StructuralReasonerFactory(); OWLReasoner structuralReasoner =
		 * structuralReasonerFactory.createNonBufferingReasoner(originalOnt);
		 * structuralReasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		 * 
		 * //System.out.println("HIERARCHY:"); //ReasonerExplorer.printHierarchy(top, 0,
		 * structuralReasoner);
		 * 
		 * 
		 * //ReasonerExplorer.moveStatementAxioms("Statement-Concept",
		 * structuralReasoner, originalOnt, statementOnt, ontologyManager, true);
		 * 
		 * //System.out.println("ORIGINAL ONTOLOGY");
		 * //OntologyExplorer.printOntologyAxioms(originalOnt);
		 * //System.out.println("STATEMENT ONTOLOGY");
		 * //OntologyExplorer.printOntologyAxioms(statementOnt);
		 * 
		 * OWLReasonerFactory elkReasonerFactory = new ElkReasonerFactory(); OWLReasoner
		 * originalReasoner = reasonerFactory.createNonBufferingReasoner(originalOnt);
		 * OWLReasoner statementReasoner =
		 * reasonerFactory.createNonBufferingReasoner(statementOnt); OWLReasoner
		 * classifiedReasoner =
		 * reasonerFactory.createNonBufferingReasoner(ontologyManager.createOntology());
		 * OWLReasoner kernelReasoner =
		 * reasonerFactory.createNonBufferingReasoner(originalOnt);
		 * kernelReasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		 * //ReasonerExplorer.printCurrentReasonerTaxonomy(kernelReasoner, false);
		 * 
		 * 
		 * 
		 * //System.out.println("STATEMENT REASONER TAXONOMY");
		 * //ReasonerExplorer.printCurrentReasonerTaxonomy(statementReasoner, false);
		 * 
		 * 
		 * //System.out.println("CLASSIFIED REASONER TAXONOMY");
		 * //ReasonerExplorer.printCurrentReasonerTaxonomy(classifiedReasoner, false);
		 * 
		 * 
		 * StatementClassifierSHIELD statementClassifier = new
		 * StatementClassifierSHIELD();
		 * statementClassifier.classifyStatementOntology(statementOnt, kernelReasoner,
		 * statementReasoner, classifiedReasoner);
		 **/

		/**
		 * CustomSubsumptionTesterSHIELD tester = new CustomSubsumptionTesterSHIELD();
		 * String superClass = "Statement"; String subClass = "Twin-Brother";
		 * //Past-Finding-in-Twin-Brother System.out.println("Does " + superClass + "
		 * subsume " + subClass + ": " + tester.classExpressionIsSubsumedBy(subClass,
		 * superClass, originalOnt, kernelReasoner, classifiedReasoner));
		 **/

//Taxonomy<ElkClass> originalTaxonomy = originalReasoner.getTaxonomy();
//TaxonomyNode<ElkClass> topNode = originalTaxonomy.getTopNode();
//Node<OWLClass> top = structuralReasoner.getTopClassNode();

		/**
		 * System.out.println("ORIGINAL REASONER TAXONOMY");
		 * ReasonerExplorer.printCurrentReasonerTaxonomy(kernelReasoner, false);
		 **/

		
/**************** PREVIOUS TESTING OF TAXONOMY NODE MIGRATION
OWLOntology statementOntology = ontologyManager.createOntology(); // initially empty
ElkReasonerSHIELD.moveStatementAxioms("Statement-Concept", originalOnt, statementOntology, true, ElkReasonerSHIELD.OntologyType.SOURCE);

StructuralReasonerFactory structuralReasonerFactory = new StructuralReasonerFactory(); 
OWLReasoner statementReasoner = structuralReasonerFactory.createNonBufferingReasoner(statementOntology);

System.out.println("ORIGINAL STATEMENT REASONER TAXONOMY");
ReasonerExplorer.printCurrentReasonerTaxonomy((StructuralReasoner) statementReasoner, false);

OWLReasonerFactory elkReasonerFactory = new ElkReasonerFactory(); 
OWLReasoner kernelReasoner = elkReasonerFactory.createNonBufferingReasoner(originalOnt);

System.out.println("ORIGINAL KERNEL REASONER TAXONOMY");
ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelReasoner, false);

StatementClassifierSHIELD.migrateReasonerTaxonomy(statementReasoner, kernelReasoner, "Statement-Concept");

System.out.println("POST-MIGRATION KERNEL REASONER TAXONOMY");
ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelReasoner, false);
******/

		/**
		 * TESTING System.out.println("KERNEL REASONER TAXONOMY");
		 * ReasonerExplorer.printCurrentReasonerTaxonomy(kernelReasoner, false);
		 * 
		 * 
		 * System.out.println("STATEMENT REASONER TAXONOMY");
		 * ReasonerExplorer.printCurrentReasonerTaxonomy(statementReasoner, false);
		 * 
		 * 
		 * System.out.println("CLASSIFIED REASONER TAXONOMY");
		 * ReasonerExplorer.printCurrentReasonerTaxonomy(classifiedReasoner, false);
		 **/

		/**
		 * REMOVED DURING TESTING OF CODE ABOVE OWLReasonerFactory reasonerFactory = new
		 * CustomReasonerFactory(); OWLReasoner reasoner =
		 * reasonerFactory.createNonBufferingReasoner(originalOnt);
		 * 
		 * //Reasoner intReasoner = ((ElkReasoner) reasoner).getInternalReasoner();
		 * 
		 * 
		 * 
		 * // Classify the ontology
		 * reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY); //this is where
		 * the reasoner inference happens, and gets saved inside the reasoner...
		 * 
		 * ReasonerExplorer.printCurrentCustomReasonerHierarchy((CustomReasoner)
		 * reasoner,"INFERRED STATEMENT ONTOLOGY HIERARCHY:");
		 * ReasonerExplorer.printCurrentReasonerTaxonomy(kernelReasoner, false);
		 * 
		 * System.out.println("COMPLETED!"); BufferedReader br = new BufferedReader(new
		 * InputStreamReader(System.in)); System.out.print("Paused. Enter something: ");
		 * 
		 * /* try { String s = br.readLine(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * try { ReasonerExplorer.printCurrentReasonerTaxonomy(reasoner, false); } catch
		 * (ElkInconsistentOntologyException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (ElkException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } REMOVED DURING TESTING OF CODE ABOVE
		 **/

//ReasonerExplorer.updateCurrentReasonerHierarchy(reasoner,"PRESENT_History-of-Asthma-Twin-Brother-in-Past");
//ReasonerExplorer.printCurrentCustomReasonerHierarchy((CustomReasoner) reasoner,"INFERRED STATEMENT ONTOLOGY HIERARCHY:");

	}

}
