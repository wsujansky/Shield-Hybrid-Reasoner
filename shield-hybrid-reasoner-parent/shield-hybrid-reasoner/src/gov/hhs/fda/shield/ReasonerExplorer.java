package gov.hhs.fda.shield;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNode;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.search.EntitySearcher;

/****** Class implements various print routines to aid in debugging  ******/

public class ReasonerExplorer {
	
	OWLOntology owlOntology;

	public ReasonerExplorer() {
		// TODO Auto-generated constructor stub
	}


	public static void printHierarchy(Node<OWLClass> node, int level, OWLReasoner reasoner) {
		if (node.isBottomNode()) 
		     { } 
		else {
			for (int i = 0; i < level*2; i++) 
			   {System.out.print(" ");}
			System.out.println(node);
		}
		// Now recurse
		Set<OWLClass> entities = node.getEntities();
		for (Iterator<OWLClass> iterator = entities.iterator(); iterator.hasNext();) {
				OWLClass owlClass = (OWLClass) iterator.next();
				NodeSet<OWLClass> subNodesNodeSet = reasoner.getSubClasses(owlClass, true);
				for (Iterator<Node<OWLClass>> iterator2 = subNodesNodeSet.iterator(); iterator2.hasNext();) {
					Node<OWLClass> owlSubClass = (Node<OWLClass>) iterator2.next();
					printHierarchy(owlSubClass, level+1, reasoner);
				}
		}
	}

	
	public static void printCurrentReasonerTaxonomy(ElkReasoner reasoner, OWLOntology ontology, boolean makeChange, boolean statementHierarchyOnly) {
		try {
		ElkReasoner elkReasoner = (ElkReasoner) reasoner;
		Reasoner internalReasoner = elkReasoner.getInternalReasoner();
		Taxonomy<ElkClass> taxonomy = internalReasoner.getTaxonomy();
		TaxonomyNode<ElkClass> topNode = taxonomy.getTopNode();
		printTaxonomy(topNode, 0, true, (ConcurrentClassTaxonomy) taxonomy, ontology, makeChange, statementHierarchyOnly);

		} catch (ElkInconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ElkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static void printCurrentReasonerTaxonomy(ElkReasoner reasoner, boolean makeChange) {
		try {
		ElkReasoner elkReasoner = (ElkReasoner) reasoner;
		Reasoner internalReasoner = elkReasoner.getInternalReasoner();
		Taxonomy<ElkClass> taxonomy = internalReasoner.getTaxonomy();
		TaxonomyNode<ElkClass> topNode = taxonomy.getTopNode();
		printTaxonomy(topNode, 0, true, (ConcurrentClassTaxonomy) taxonomy, makeChange);

		} catch (ElkInconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ElkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void printCurrentReasonerTaxonomySHIELD(OWLReasoner reasoner, boolean makeChange) {
		try {
		ElkReasoner elkReasoner = (ElkReasoner) reasoner;
		Reasoner internalReasoner = elkReasoner.getInternalReasoner();
		Taxonomy<ElkClass> taxonomy = internalReasoner.getTaxonomy();
		TaxonomyNode<ElkClass> topNode = taxonomy.getTopNode();
		printTaxonomy(topNode, 0, true, (ConcurrentClassTaxonomy) taxonomy, makeChange);
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
		printTaxonomy(topNode, 0, true, (ConcurrentClassTaxonomy) taxonomy, makeChange);
		} catch (ElkInconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ElkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static synchronized void printTaxonomy(TaxonomyNode<ElkClass> node, int level, boolean alreadyDone, 
			                                      ConcurrentClassTaxonomy taxonomy, boolean makeChange) {  
		if (node instanceof UpdateableBottomNode) {  // don't print and stop recursion  
			}
		else {
			ElkClass elkClass = node.getMembers().iterator().next();
			System.out.println(indentSpaces(level) + getShortFormElkClassName(elkClass));
			Set<UpdateableTaxonomyNode<ElkClass>> subNodes = ((NonBottomClassNode) node).getDirectUpdateableSubNodes(); 
			LinkedList<UpdateableTaxonomyNode<ElkClass>> sortedSubNodes = sortNodesElk(subNodes);
			for (Iterator<UpdateableTaxonomyNode<ElkClass>> iterator = sortedSubNodes.iterator(); iterator.hasNext();) {
				TaxonomyNode<ElkClass> elkSubNode = (TaxonomyNode<ElkClass>) iterator.next();
				printTaxonomy(elkSubNode, level+1, alreadyDone, taxonomy, makeChange);  
				}
		}
	}
	
	public static synchronized void printTaxonomy(TaxonomyNode<ElkClass> node, int level, boolean alreadyDone,
			ConcurrentClassTaxonomy taxonomy, OWLOntology ontology, boolean makeChange) {
		if (node instanceof UpdateableBottomNode) { // don't print and stop recursion
		} else {
			ElkClass elkClass = node.getMembers().iterator().next();
			System.out.println(indentSpaces(level) + getShortFormElkClassName(elkClass) + getAnnnotationLabel(elkClass,ontology));
			Set<UpdateableTaxonomyNode<ElkClass>> subNodes = ((NonBottomClassNode) node).getDirectUpdateableSubNodes();
			LinkedList<UpdateableTaxonomyNode<ElkClass>> sortedSubNodes = sortNodesElk(subNodes);
			for (Iterator<UpdateableTaxonomyNode<ElkClass>> iterator = sortedSubNodes.iterator(); iterator.hasNext();) {
				TaxonomyNode<ElkClass> elkSubNode = (TaxonomyNode<ElkClass>) iterator.next();
				printTaxonomy(elkSubNode, level + 1, alreadyDone, taxonomy, ontology, makeChange);
			}
		}
	}
	
	private static String getAnnnotationLabel(ElkClass elkClass, OWLOntology ontology) {
		OWLClass owlClass = ElkConverter.getInstance().convert(elkClass);
	    Collection<OWLAnnotation> annotations = EntitySearcher.getAnnotations(owlClass, ontology);
	    for (Iterator<OWLAnnotation> iterator = annotations.iterator(); iterator.hasNext();) {
	    	OWLAnnotation owlAnnotation = (OWLAnnotation) iterator.next();
	    	if (owlAnnotation.getProperty().getIRI().toString().equals("http://www.w3.org/2000/01/rdf-schema#label")) {
	    		String quotedValue = owlAnnotation.getValue().toString();
	    		return "   (" + quotedValue.substring(1, quotedValue.length() - 4) + ")"; // remove beginning/ending double quotes
	    	}
	    }
		return "";
	}

	
	public synchronized void printTaxonomy(Node<OWLClass> node, int level, boolean alreadyDone, StructuralReasoner reasoner, boolean makeChange) {  
		if (node.equals(reasoner.getBottomClassNode()))  {  // don't print and stop recursion
			}
		else {
			OWLClass owlClass = node.getEntities().iterator().next();  
			System.out.println(indentSpaces(level) + owlClass.getIRI());
			// Now recurse
			OWLClass thisClass = node.getEntities().iterator().next();
			NodeSet<OWLClass> subNodes = reasoner.getSubClasses(thisClass, true);  
			for (Iterator<Node<OWLClass>> iterator = subNodes.iterator(); iterator.hasNext();) {
				Node<OWLClass> owlSubNode = (Node<OWLClass>) iterator.next();
				printTaxonomy(owlSubNode, level+1, alreadyDone, reasoner, makeChange);  
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
			LinkedList<Node<OWLClass>> sortedSubNodes = sortNodesOwl(subNodes);
			for (Iterator iterator = sortedSubNodes.iterator(); iterator.hasNext();) {
				Node<OWLClass> owlSubNode = (Node<OWLClass>) iterator.next();
				printOwlTaxonomy(owlSubNode, level+1, reasoner);  
				}
		}
	}
	
	public static synchronized void printTaxonomy(TaxonomyNode<ElkClass> node, int level, 
			                                      boolean alreadyDone, ConcurrentClassTaxonomy taxonomy, 
			                                      OWLOntology ontology, boolean makeChange, boolean statementHierarchyOnly) {  
		if (!statementHierarchyOnly)
			printTaxonomy(node, level, alreadyDone, taxonomy, ontology, makeChange);  
		else {
			printTaxonomy(breadthFirstSearchForStatementRootInKernelTaxonomy(node, DefaultProperties.STATEMENT_CONCEPT_NAMESPACE, DefaultProperties.STATEMENT_CONCEPT_NAME), 
					      level, alreadyDone, taxonomy, ontology, makeChange);
		}
	}
	
	private static TaxonomyNode<ElkClass> breadthFirstSearchForStatementRootInKernelTaxonomy(TaxonomyNode<ElkClass> taxonomyTopElkNode, 
																					  String statementConceptNamespace, String statementConceptName) {
		OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();
		OWLClass targetNamedClass = factory
				.getOWLClass(IRI.create(statementConceptNamespace + statementConceptName));
		OWLClassNode targetOwlNode = new OWLClassNode(targetNamedClass);
		List<TaxonomyNode<ElkClass>> nodeList = new ArrayList<TaxonomyNode<ElkClass>>();
		nodeList.add(taxonomyTopElkNode);
		int index = 0;
		while (index < nodeList.size()) {
			TaxonomyNode<ElkClass> currentTaxonomyElkNode = nodeList.get(index);
			for (ElkClass currentElkClass : currentTaxonomyElkNode.getMembers()) { // iterate through each member ElkClass of the node
				if (ElkConverter.getInstance().convert(currentElkClass)
						.equals(targetOwlNode.getEntities().iterator().next())) {
					return currentTaxonomyElkNode;
				}
			}  // if currentTaxonomyElkNode != targetOwlNode, then add children ofcurrentTaxonomyElkNode to end of nodeList and continue 
			   // iterating through nodeList (breadth-first search)
			nodeList.addAll(currentTaxonomyElkNode.getDirectSubNodes());
			index++;
		} // if targetOwlNode not found anywhere in the kernelReasoner taxonomy, return null (although this search will take a long time with a large ontology...)
		return null;
	}
	
	private Node<OWLClass> getNamedOwlClassNodeFromStatementReasoner(String targetNodeIri,
			OWLOntology statementOntology, OWLReasoner statementOwlReasoner) {
			// First, retrieve the OWLClass node that represents the root concept of the statement hierarchy
			// from the statementOwlReasoner ("Statement-Concept", in the default version of the statement sub-ontology)
			OWLDataFactory factory = statementOntology.getOWLOntologyManager().getOWLDataFactory();
			OWLClass targetNamedClass = factory
					.getOWLClass(IRI.create(targetNodeIri));
			OWLClass topStatementReasonerClassNode = statementOwlReasoner.getTopClassNode().getEntities().iterator()
					.next();
			NodeSet<OWLClass> subNodes = statementOwlReasoner.getSubClasses(topStatementReasonerClassNode, false);
			for (Iterator<Node<OWLClass>> iterator = subNodes.iterator(); iterator.hasNext();) {
				Node<OWLClass> subNode = (Node<OWLClass>) iterator.next();
				if (subNode.getEntities().iterator().next().equals(targetNamedClass)) {
					return subNode;

				}
			}
			return null;
	}



	
	public synchronized static void printOwlTaxonomyFullIri(Node<OWLClass> node, int level, OWLReasoner reasoner) {  
		if (node.equals(reasoner.getBottomClassNode()))  {
			printFullIri(level, node.getEntities().iterator().next().getIRI());
			}
		else {
			OWLClass owlClass = node.getEntities().iterator().next();  
			printFullIri(level, node.getEntities().iterator().next().getIRI());
			// Now recurse
			OWLClass thisClass = node.getEntities().iterator().next();
			NodeSet<OWLClass> subNodes = reasoner.getSubClasses(thisClass, true); 
			for (Iterator iterator = subNodes.iterator(); iterator.hasNext();) {
				Node<OWLClass> owlSubNode = (Node<OWLClass>) iterator.next();
				printOwlTaxonomyFullIri(owlSubNode, level+1, reasoner);  
				}
		}
	}
	
	private static void printFullIri(int level, IRI iri) {
		System.out.println(indentSpaces(level) + "FullIRI: " + iri.getNamespace() + " ||  " + iri.getRemainder());
	}



	public static String indentSpaces(int level) {
		String indentString = "";
		for (int i = 0; i < level*2; i++) 
		   {indentString = indentString + " ";}
		return indentString;
	}
	
	private static String getShortFormElkClassName(ElkClass elkClass) {
		OWLClass owlClass = ElkConverter.getInstance().convert(elkClass);
		return owlClass.getIRI().getShortForm();
	}
	
	private static String getShortFormOwlClassName(OWLClass owlClass) {
		return owlClass.getIRI().getShortForm();
	}

	
	private static LinkedList<UpdateableTaxonomyNode<ElkClass>> sortNodesElk(Set<UpdateableTaxonomyNode<ElkClass>> inputSet) {
		LinkedList<UpdateableTaxonomyNode<ElkClass>> list = new LinkedList<UpdateableTaxonomyNode<ElkClass>>(inputSet);

    list.sort( new Comparator<UpdateableTaxonomyNode<ElkClass>>(){
    @Override
        public int compare(UpdateableTaxonomyNode<ElkClass> node1, UpdateableTaxonomyNode<ElkClass> node2){
            return Collator.getInstance().compare(
            		getShortFormElkClassName(node1.getMembers().iterator().next()),
            		getShortFormElkClassName(node2.getMembers().iterator().next()));
        }
    });
    return list;
	}
	
	private static LinkedList<Node<OWLClass>> sortNodesOwl(NodeSet<OWLClass> inputNodeSet) {
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

	public static void pause(String msg) {
		 BufferedReader reader = new BufferedReader(
		            new InputStreamReader(System.in));

		        // Reading data using readLine
		        try {
		        	System.out.print("Pausing at " + msg + " <enter something>:");
					String name = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	
}
