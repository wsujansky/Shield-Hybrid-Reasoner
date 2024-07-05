package gov.hhs.fda.shield;


import java.text.Collator;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.semanticweb.elk.owl.exceptions.ElkException;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owlapi.ElkConverter;
import org.semanticweb.elk.owlapi.ElkReasoner;
import org.semanticweb.elk.reasoner.ElkInconsistentOntologyException;
import org.semanticweb.elk.reasoner.Reasoner;
import org.semanticweb.elk.reasoner.taxonomy.ConcurrentClassTaxonomy;
import org.semanticweb.elk.reasoner.taxonomy.NonBottomClassNode;
import org.semanticweb.elk.reasoner.taxonomy.model.Taxonomy;
import org.semanticweb.elk.reasoner.taxonomy.model.TaxonomyNode;
import org.semanticweb.elk.reasoner.taxonomy.model.UpdateableBottomNode;
import org.semanticweb.elk.reasoner.taxonomy.model.UpdateableTaxonomyNode;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;

import uk.ac.manchester.cs.owl.owlapi.OWLEquivalentClassesAxiomImpl;

public class OntologyExplorer {

	public OntologyExplorer() {
		// TODO Auto-generated constructor stub
	}
	
	public static void printOntologySignature(OWLOntology ont) {
		System.out.println("CLASSES IN SIGNATURE:");
		for (OWLClass cls : ont.getClassesInSignature()) {
        System.out.println(cls); 
        } 
	}
	
	public static void printOntologyAxioms(OWLOntology ont) {
		System.out.println("AXIOMS IN ONTOLOGY:");
		Set<OWLAxiom> axioms = ont.getAxioms(Imports.INCLUDED);
		for (Iterator iterator = axioms.iterator(); iterator.hasNext();) {
			OWLAxiom owlAxiom = (OWLAxiom) iterator.next();
			System.out.println("AXIOM TYPE: " + owlAxiom.getAxiomType());
			System.out.println(owlAxiom);
		}
	}
	
	public static void printOntologyLogicalAxioms(OWLOntology ont) {
		System.out.println("AXIOMS IN ONTOLOGY:");
		Set<OWLLogicalAxiom> axioms = ont.getLogicalAxioms(Imports.INCLUDED);
		for (Iterator iterator = axioms.iterator(); iterator.hasNext();) {
			OWLAxiom owlAxiom = (OWLAxiom) iterator.next();
			System.out.println("AXIOM TYPE: " + owlAxiom.getAxiomType());
			System.out.println(owlAxiom);
		}
	}
	
	public static void printOntologyParsedAxioms(OWLOntology ont) {
		System.out.println("AXIOMS IN ONTOLOGY:");
		OntologyAxiomParserAndPrinterVisitor visitor = new OntologyAxiomParserAndPrinterVisitor();
		Set<OWLLogicalAxiom> axioms = ont.getLogicalAxioms(Imports.INCLUDED);
		for (Iterator iterator = axioms.iterator(); iterator.hasNext();) {
			OWLAxiom owlAxiom = (OWLAxiom) iterator.next();
			owlAxiom.accept(visitor);
		}
	}

	public static void printOntologyClassDefinitions(OWLOntology ont) {
		System.out.println("CLASSES DEFINED IN ONTOLOGY:");
		Set<OWLClass> classes = ont.getClassesInSignature();
		for (Iterator iterator = classes.iterator(); iterator.hasNext();) {
			OWLClass cls = (OWLClass) iterator.next();
			System.out.println("  CLASS: " + cls);
			Set<OWLClassAxiom> classAxioms = ont.getAxioms(cls,Imports.INCLUDED);
			for (Iterator iterator2 = classAxioms.iterator(); iterator2.hasNext();) {
				OWLClassAxiom classAxiom = (OWLClassAxiom) iterator2.next();
				if (classAxiom instanceof OWLEquivalentClassesAxiom)
					System.out.print("    EQUIVALENT TO: ");
				else if (classAxiom instanceof OWLSubClassOfAxiom) 
					System.out.print("    SUBCLASS OF: ");
				System.out.println(getClassDefinitionFromAxiom(cls, (OWLAxiom) classAxiom));
			}
		}
	}

	private static OWLClassExpression getClassDefinitionFromAxiom(OWLClass cls, OWLAxiom axiom) {
	if (axiom instanceof OWLEquivalentClassesAxiom) {
		Set<OWLClassExpression> class_exprs = ((OWLEquivalentClassesAxiomImpl) axiom).getClassExpressionsMinus(cls);
		if (class_exprs.size() != 1)
			throw new UnsupportedOperationException("Unexpected: " + class_exprs.size() + " " + class_exprs);
		OWLClassExpression def = class_exprs.iterator().next();
		return def;
	}
	else if (axiom instanceof OWLSubClassOfAxiom) {
		OWLClassExpression def = ((OWLSubClassOfAxiom) axiom).getSuperClass();
		return def;
	}
	else throw new UnsupportedOperationException("Unexpected: " + axiom.getAxiomType());
	}
	
	/**
	private OWLClassExpression getDefinitionTemp(OWLClass concept, OWLClassAxiom axiom) {
		switch (axiom) {
		case OWLEquivalentClassesAxiom x -> {
			Set<OWLClassExpression> class_exprs = x.getClassExpressionsMinus(concept);
			if (class_exprs.size() != 1)
				throw new UnsupportedOperationException("Unexpected: " + class_exprs.size() + " " + class_exprs);
			OWLClassExpression def = class_exprs.iterator().next();
			return def;
		}
		case OWLSubClassOfAxiom x -> {
			OWLClassExpression def = x.getSuperClass();
			return def;
		}
		default -> throw new UnsupportedOperationException("Unexpected: " + axiom.getAxiomType());
		}
	}
**/
	
	@SuppressWarnings("deprecation")
	public static void printOntologySignatureAndReferencingAxioms(OWLOntology ont) {
		System.out.println("REFERENCING AXIOMS:");
		for (OWLClass cls : ont.getClassesInSignature()) {
          System.out.println("  CLASS: " + cls);
          Set<OWLAxiom> referencingAxioms = ont.getReferencingAxioms(cls, true);
          for (Iterator iterator = referencingAxioms.iterator(); iterator.hasNext();) {
			OWLAxiom owlAxiom = (OWLAxiom) iterator.next();
			System.out.println("    AXIOM: " + owlAxiom);
          }
        }
	}
	
	public synchronized static void printCurrentReasonerOwlTaxonomy(OWLReasoner reasoner) {
		Node<OWLClass> topClassNode = reasoner.getTopClassNode();
		printOwlTaxonomy(topClassNode, 0, reasoner);
	}

	public synchronized static void printOwlTaxonomy(Node<OWLClass> node, int level, OWLReasoner reasoner) {  
		if (node.equals(reasoner.getBottomClassNode()))  {
			System.out.println(indentSpaces(level) + node.getEntities().iterator().next().getIRI().getShortForm());
			}
		else {
			OWLClass owlClass = node.getEntities().iterator().next();  
			System.out.println(indentSpaces(level) + owlClass.getIRI().getShortForm());
			// Now recurse
			OWLClass thisClass = node.getEntities().iterator().next();
			NodeSet<OWLClass> subNodes = reasoner.getSubClasses(thisClass, true); 
			LinkedList<Node<OWLClass>> sortedSubNodes = sortNodes(subNodes);
			for (Iterator iterator = sortedSubNodes.iterator(); iterator.hasNext();) {
				Node<OWLClass> owlSubNode = (Node<OWLClass>) iterator.next();
				printOwlTaxonomy(owlSubNode, level+1, reasoner);  
				}
		}
	}
	
	private static String indentSpaces(int level) {
		String indentString = "";
		for (int i = 0; i < level*2; i++) 
		   {indentString = indentString + " ";}
		return indentString;
	}
	
	private static LinkedList<Node<OWLClass>> sortNodes(NodeSet<OWLClass> inputNodeSet) {
		Set<Node<OWLClass>> inputSet = new HashSet();
		for (Iterator iterator = inputNodeSet.iterator(); iterator.hasNext();) {
			Node<OWLClass> node = (Node<OWLClass>) iterator.next();
			inputSet.add(node);
		}
		LinkedList<Node<OWLClass>> list = new LinkedList<Node<OWLClass>>(inputSet);

		list.sort(new Comparator<Node<OWLClass>>() {
			@Override
			public int compare(Node<OWLClass> node1, Node<OWLClass> node2) {
				return Collator.getInstance().compare(
						getShortFormOwlClassName(node1.getEntities().iterator().next()),
						getShortFormOwlClassName(node2.getEntities().iterator().next()));
			}
		});
		return list;
	}
	
	private static String getShortFormOwlClassName(OWLClass owlClass) {
		return owlClass.getIRI().getShortForm();
	}




	
}

