package gov.hhs.fda.shield;


import java.util.Iterator;
import java.util.Set;

import org.semanticweb.elk.owl.exceptions.ElkException;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owlapi.ElkReasoner;
import org.semanticweb.elk.reasoner.ElkInconsistentOntologyException;
import org.semanticweb.elk.reasoner.Reasoner;
import org.semanticweb.elk.reasoner.taxonomy.ConcurrentClassTaxonomy;
import org.semanticweb.elk.reasoner.taxonomy.model.Taxonomy;
import org.semanticweb.elk.reasoner.taxonomy.model.TaxonomyNode;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

public class ReasonerExplorer {

	public ReasonerExplorer() {
		// TODO Auto-generated constructor stub
	}


/** OBSOLETE - May not work if the reasoner taxonomy has been internally changed **/
	public static void printHierarchy(Node<OWLClass> node, int level, OWLReasoner reasoner) {
		if (node.isBottomNode()) 
		     { } 
		else {
			for (int i = 0; i < level*2; i++) 
			   {System.out.print(" ");}
			System.out.println(node);
		}
		if (level%5 == 0) {
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		System.out.print("Paused at level = " + level + ". Enter something: ");
//        try {
//			String s = br.readLine();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

		// Now recurse
		Set<OWLClass> entities = node.getEntities();
		for (Iterator iterator = entities.iterator(); iterator.hasNext();) {
				OWLClass owlClass = (OWLClass) iterator.next();
				NodeSet<OWLClass> subNodesNodeSet = reasoner.getSubClasses(owlClass, true);
				for (Iterator iterator2 = subNodesNodeSet.iterator(); iterator2.hasNext();) {
					Node<OWLClass> owlSubClass = (Node<OWLClass>) iterator2.next();
//					owlSubClass.getRepresentativeElement();
					printHierarchy(owlSubClass, level+1, reasoner);
				}
		}
	}

	
	public static void printCurrentReasonerTaxonomy(ElkReasoner reasoner, boolean makeChange) {
		try {
		ElkReasoner elkReasoner = (ElkReasoner) reasoner;
		Reasoner internalReasoner = elkReasoner.getInternalReasoner();
		Taxonomy<ElkClass> taxonomy = internalReasoner.getTaxonomy();
		TaxonomyNode<ElkClass> topNode = taxonomy.getTopNode();
		TaxonomyEditingTesterSHIELD tester = new TaxonomyEditingTesterSHIELD();		
		ElkClass currentNodeClass = topNode.getMembers().iterator().next();
		tester.printTaxonomy(topNode, 0, true, (ConcurrentClassTaxonomy) taxonomy, makeChange);
		} catch (ElkInconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ElkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	public static void printCurrentReasonerTaxonomy(StructuralReasoner reasoner, boolean makeChange) {
		Node<OWLClass> topNode = reasoner.getTopClassNode();
		TaxonomyEditingTesterSHIELD tester = new TaxonomyEditingTesterSHIELD();		
		OWLClass currentNodeClass = topNode.getEntities().iterator().next();
//System.out.println("TopNode: " + topNode);
		tester.printTaxonomy(topNode, 0, true, reasoner, makeChange);
	}
**/

	
	public static void printCurrentReasonerTaxonomySHIELD(OWLReasoner reasoner, boolean makeChange) {
		try {
		ElkReasoner elkReasoner = (ElkReasoner) reasoner;
		Reasoner internalReasoner = elkReasoner.getInternalReasoner();
		Taxonomy<ElkClass> taxonomy = internalReasoner.getTaxonomy();
		TaxonomyNode<ElkClass> topNode = taxonomy.getTopNode();
		TaxonomyEditingTesterSHIELD tester = new TaxonomyEditingTesterSHIELD();		
		ElkClass currentNodeClass = topNode.getMembers().iterator().next();
		tester.printTaxonomy(topNode, 0, true, (ConcurrentClassTaxonomy) taxonomy, makeChange);
		} catch (ElkInconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ElkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printCurrentReasonerTaxonomySHIELD(Reasoner internalReasoner, boolean makeChange) {
		try {
		Taxonomy<ElkClass> taxonomy = internalReasoner.getTaxonomy();
		TaxonomyNode<ElkClass> topNode = taxonomy.getTopNode();
		TaxonomyEditingTesterSHIELD tester = new TaxonomyEditingTesterSHIELD();		
		tester.printTaxonomy(topNode, 0, true, (ConcurrentClassTaxonomy) taxonomy, makeChange);
		} catch (ElkInconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ElkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	
	public static void updateCurrentReasonerHierarchy(OWLReasoner reasoner, String className) {
		Node<OWLClass> top = reasoner.getTopClassNode();
		updateHierarchy(top, 0, reasoner, className);
	}
	
	public static void updateHierarchy(Node<OWLClass> node, int level, OWLReasoner reasoner, String className) {
		if (node.isBottomNode()) 
		     { } 
//		else {
//			for (int i = 0; i < level*2; i++) 
//			   {System.out.print(" ");}
//			System.out.println(node);
//		}
		// Now recurse
		Set<OWLClass> entities = node.getEntities();
		for (Iterator iterator = entities.iterator(); iterator.hasNext();) {
				OWLClass owlClass = (OWLClass) iterator.next();
				if (owlClass.getIRI().toString().contains(className))
					System.out.println("WILL SUBCLASS ITSELF WITH THIS CLASS: " + owlClass);
				TaxonomyEditingTesterSHIELD.addSubClassNode(node);
				NodeSet<OWLClass> subNodesNodeSet = reasoner.getSubClasses(owlClass, true);
				for (Iterator iterator2 = subNodesNodeSet.iterator(); iterator2.hasNext();) {
					Node<OWLClass> owlSubClass = (Node<OWLClass>) iterator2.next();
					updateHierarchy(owlSubClass, level+1, reasoner, className);
				}
		}
	}

	public static void moveStatementAxioms(String targetClassName, OWLReasoner structuralReasoner, OWLOntology sourceOntology, OWLOntology destinationOntology, OWLOntologyManager ontologyManager, Boolean removeFromSourceOntology) {
		Set<OWLAxiom> axioms = sourceOntology.getAxioms(Imports.INCLUDED);
		Set<OWLClass> subClassSet = buildSubClassSet(targetClassName, sourceOntology, structuralReasoner, true);
		for (Iterator iterator = axioms.iterator(); iterator.hasNext();) {
			OWLAxiom owlAxiom = (OWLAxiom) iterator.next();
//			System.out.println("AXIOM TYPE: " + owlAxiom.getAxiomType());
//			System.out.println(owlAxiom);
			if (owlAxiom instanceof OWLEquivalentClassesAxiom) {
				Set<OWLClassExpression> class_exprs = ((OWLEquivalentClassesAxiom) owlAxiom).getClassExpressions();
				for (Iterator iterator2 = class_exprs.iterator(); iterator2.hasNext();) {
					OWLClassExpression owlClassExpression = (OWLClassExpression) iterator2.next();
					//System.out.println("Expression: " + owlClassExpression);
					if (owlClassExpression instanceof OWLClass) {
//						if ( ((OWLClassImpl)owlClassExpression).getIRI().toString().contains("PRESENT") ||
//						     ((OWLClassImpl)owlClassExpression).getIRI().toString().contains("ABSENT")	||
//						     ((OWLClassImpl)owlClassExpression).getIRI().toString().contains("Statement")) {
//						OWLAxiom subClassAxiom = buildSubClassAxiom(sourceOntology, "Statement-Concept", owlClassExpression);
//						System.out.println("THE SUB-CLASS TO TEST: " + owlClassExpression);
//						System.out.println("Against the subclass set: ");
//						for (Iterator iterator3 = subClassSet.iterator(); iterator3.hasNext();) {
//							OWLClass candidateClassExpression = (OWLClass) iterator3.next();
//							System.out.println(candidateClassExpression);
//						}
						if (subClassSet.contains(owlClassExpression)) {
//							System.out.println("MOVING EQUIVALENT-CLASSES AXIOM");	
							if (removeFromSourceOntology) {
								ontologyManager.removeAxiom(sourceOntology, owlAxiom);
							}
							ontologyManager.addAxiom(destinationOntology, owlAxiom);
							//RemoveAxiom removeAxiom = new RemoveAxiom(ont, owlAxiom);// remove the axiom from the ontology
							//inputOntologyManager.applyChange(removeAxiom);
						}
					}
				}
			}
/** Maybe leave declaration axioms in the source ontology for now; they get created automatically in the target ontology */
			else if (owlAxiom instanceof OWLDeclarationAxiom) {
				Set<OWLClass> declaredClasses = owlAxiom.getClassesInSignature();
				for (Iterator iterator3 = declaredClasses.iterator(); iterator3.hasNext();) {
					OWLClass owlClass = (OWLClass) iterator3.next();
//						System.out.println("THE SUB-CLASS TO TEST: " + owlClass);
//						System.out.println("Against the subclass set: ");
//						for (Iterator iterator3 = subClassSet.iterator(); iterator3.hasNext();) {
//							OWLClass candidateClassExpression = (OWLClass) iterator3.next();
//							System.out.println(candidateClassExpression);
//						}
						if (subClassSet.contains(owlClass)) {
//							System.out.println("MOVING DECLARATION AXIOM");	
							if (removeFromSourceOntology) {
								ontologyManager.removeAxiom(sourceOntology, owlAxiom);
							}
							ontologyManager.addAxiom(destinationOntology, owlAxiom);
							//RemoveAxiom removeAxiom = new RemoveAxiom(ont, owlAxiom);// remove the axiom from the ontology
							//inputOntologyManager.applyChange(removeAxiom);
						}
					}
			}
/**  **/		
			else if (owlAxiom instanceof OWLSubClassOfAxiom) {
				Set<OWLClassExpression> subClasses = ((OWLSubClassOfAxiom) owlAxiom).getNestedClassExpressions();
				for (Iterator iterator4 = subClasses.iterator(); iterator4.hasNext();) {
					OWLClass owlClass = (OWLClass) iterator4.next();
//					if ( owlClass.getIRI().toString().contains("PRESENT") ||
//					     owlClass.getIRI().toString().contains("ABSENT")  ||
//					     owlClass.getIRI().toString().contains("Statement")) {
					if (subClassSet.contains(owlClass)) {
//							System.out.println("MOVING SUB-CLASS AXIOM");	
						if (removeFromSourceOntology) {
							ontologyManager.removeAxiom(sourceOntology, owlAxiom);
						}
							ontologyManager.addAxiom(destinationOntology, owlAxiom);
					}
				}
				
			}
		}
	}

	
	public static void moveStatementAxiomsHardCoded(OWLOntology sourceOntology, OWLOntology destinationOntology, OWLOntologyManager ontologyManager, Boolean removeFromSourceOntology) {
		Set<OWLAxiom> axioms = sourceOntology.getAxioms(Imports.INCLUDED);
		//Set<OWLAxiom> axioms = ont.getAxioms();
		for (Iterator iterator = axioms.iterator(); iterator.hasNext();) {
			OWLAxiom owlAxiom = (OWLAxiom) iterator.next();
//			System.out.println("AXIOM TYPE: " + owlAxiom.getAxiomType());
//			System.out.println(owlAxiom);
			if (owlAxiom instanceof OWLEquivalentClassesAxiom) {
				Set<OWLClassExpression> class_exprs = ((OWLEquivalentClassesAxiom) owlAxiom).getClassExpressions();
				for (Iterator iterator2 = class_exprs.iterator(); iterator2.hasNext();) {
					OWLClassExpression owlClassExpression = (OWLClassExpression) iterator2.next();
					//System.out.println("Expression: " + owlClassExpression);
					if (owlClassExpression instanceof OWLClass) {
						if ( ((OWLClassImpl)owlClassExpression).getIRI().toString().contains("PRESENT") ||
						     ((OWLClassImpl)owlClassExpression).getIRI().toString().contains("ABSENT")	||
						     ((OWLClassImpl)owlClassExpression).getIRI().toString().contains("Statement")) {
//							System.out.println("MOVING EQUIVALENT-CLASSES AXIOM");	
							if (removeFromSourceOntology) {
								ontologyManager.removeAxiom(sourceOntology, owlAxiom);
							}
							ontologyManager.addAxiom(destinationOntology, owlAxiom);
							//RemoveAxiom removeAxiom = new RemoveAxiom(ont, owlAxiom);// remove the axiom from the ontology
							//inputOntologyManager.applyChange(removeAxiom);
						}
					}
				}
			}
			else if (owlAxiom instanceof OWLDeclarationAxiom) { 
				Set<OWLClass> declaredClasses = owlAxiom.getClassesInSignature();
				for (Iterator iterator3 = declaredClasses.iterator(); iterator3.hasNext();) {
					OWLClass owlClass = (OWLClass) iterator3.next();
					if ( owlClass.getIRI().toString().contains("PRESENT") ||
					     owlClass.getIRI().toString().contains("ABSENT")  ||
					     owlClass.getIRI().toString().contains("Statement")) {
//							System.out.println("MOVING DECLARATION-CLASS AXIOM");	
						if (removeFromSourceOntology) {
							ontologyManager.removeAxiom(sourceOntology, owlAxiom);
						}
							ontologyManager.addAxiom(destinationOntology, owlAxiom);
					}
				}
			}
			else if (owlAxiom instanceof OWLSubClassOfAxiom) {
				Set<OWLClassExpression> subClasses = ((OWLSubClassOfAxiom) owlAxiom).getNestedClassExpressions();
				for (Iterator iterator4 = subClasses.iterator(); iterator4.hasNext();) {
					OWLClass owlClass = (OWLClass) iterator4.next();
					if ( owlClass.getIRI().toString().contains("PRESENT") ||
					     owlClass.getIRI().toString().contains("ABSENT")  ||
					     owlClass.getIRI().toString().contains("Statement")) {
//							System.out.println("MOVING SUB-CLASS AXIOM");	
						if (removeFromSourceOntology) {
							ontologyManager.removeAxiom(sourceOntology, owlAxiom);
						}
							ontologyManager.addAxiom(destinationOntology, owlAxiom);
					}
				}
			}
		}
	}

public static OWLAxiom buildSubClassAxiom(OWLOntology ontology, String superClassName, OWLClassExpression subClassExpression) {
	IRI ontologyIRI = IRI.create("http://www.hhs.fda.org/shield/SWEC-Ontology");
	OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
	OWLClass statementConceptClass = factory.getOWLClass(IRI.create(ontologyIRI + "#" + superClassName));
	OWLSubClassOfAxiom ax = factory.getOWLSubClassOfAxiom(subClassExpression, statementConceptClass);
    return ax;
	}

public synchronized static Set<OWLClass> buildSubClassSet(String conceptName, OWLOntology ontology, OWLReasoner reasoner, boolean includeRootClass)  {
	OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
    OWLClass superClass = factory.getOWLClass(
       IRI.create("http://www.hhs.fda.org/shield/SWEC-Ontology" + "#" + conceptName));
    Set<OWLClass> subClassSet = reasoner.getSubClasses(superClass, false).getFlattened();
    if (includeRootClass)  {
        subClassSet.add(superClass);  // include the superclass concept also
    }
    return subClassSet;
	}

public static void tempTestReferencingAxiomsPrinter(OWLOntology owlOntology, OWLReasoner reasoner) {
	try {
	ElkReasoner elkReasoner = (ElkReasoner) reasoner;
	Reasoner internalReasoner = elkReasoner.getInternalReasoner();
	Taxonomy<ElkClass> taxonomy = internalReasoner.getTaxonomy();
	TaxonomyNode<ElkClass> topNode = taxonomy.getTopNode();
	TaxonomyEditingTesterSHIELD tester = new TaxonomyEditingTesterSHIELD();		
//	ElkClass currentNodeClass = topNode.getMembers().iterator().next();
	tester.printTaxonomy(topNode, 0, true, (ConcurrentClassTaxonomy) taxonomy, false);
	} catch (ElkInconsistentOntologyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ElkException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
