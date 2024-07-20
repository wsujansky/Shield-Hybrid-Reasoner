package gov.hhs.fda.shield;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.semanticweb.elk.owl.exceptions.ElkException;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owlapi.ElkConverter;
import org.semanticweb.elk.owlapi.ElkReasoner;
import org.semanticweb.elk.owlapi.wrapper.OwlConverter;
import org.semanticweb.elk.reasoner.ElkInconsistentOntologyException;
import org.semanticweb.elk.reasoner.taxonomy.ConcurrentClassTaxonomy;
import org.semanticweb.elk.reasoner.taxonomy.NonBottomClassNode;
import org.semanticweb.elk.reasoner.taxonomy.model.TaxonomyNode;
import org.semanticweb.elk.reasoner.taxonomy.model.UpdateableTaxonomyNode;
import org.semanticweb.elk.reasoner.taxonomy.model.UpdateableBottomNode;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class StatementClassifierSHIELD {
	
	@SuppressWarnings("unused")
	private OWLOntology statementOntology_;
	private OWLReasoner kernelOwlReasoner_;
	private OWLReasoner statementOwlReasoner_;
	private SubsumptionNormalFormBuilderSHIELD subsumptionNormalFormBuilder;
	private CustomSubsumptionTesterSHIELD subsumptionTester;
	private String statementConceptNamespace;
	private String statementConceptName;
	private String temporalAnnotationOwlIRI;
	private String owlThingIRI;
	private String owlNothingIRI;




		public StatementClassifierSHIELD(OWLOntology kernelOntology, OWLOntology statementOntology, OWLReasoner kernelOwlReasoner, OWLReasoner statementOwlReasoner) {
		this.statementOntology_ = statementOntology;
		this.kernelOwlReasoner_ = kernelOwlReasoner;
		this.statementOwlReasoner_ = statementOwlReasoner;
		this.statementConceptNamespace = DefaultProperties.STATEMENT_CONCEPT_NAMESPACE;
		this.statementConceptName = DefaultProperties.STATEMENT_CONCEPT_NAME;
		this.temporalAnnotationOwlIRI = DefaultProperties.TEMPORAL_ANNOTATION_OWL_IRI;
		this.owlThingIRI = DefaultProperties.OWL_NOTHING_IRI;
		this.owlNothingIRI = DefaultProperties.OWL_NOTHING_IRI;
		subsumptionNormalFormBuilder = new SubsumptionNormalFormBuilderSHIELD(kernelOntology,
				 																 statementOntology,
				 																 kernelOwlReasoner,
				 																 statementOwlReasoner);	
		subsumptionNormalFormBuilder.init();
		subsumptionNormalFormBuilder.generate();
		subsumptionTester = new CustomSubsumptionTesterSHIELD(kernelOntology, this.statementConceptNamespace, this.statementConceptName, 
															  this.temporalAnnotationOwlIRI, this.owlThingIRI, this.owlNothingIRI);
	}
		
		public StatementClassifierSHIELD(OWLOntology kernelOntology, OWLOntology statementOntology, OWLReasoner kernelOwlReasoner, 
				OWLReasoner statementOwlReasoner, String statementConceptNamespace, String statementConceptName, String temporalAnnotationOwlIRI,
				String owlThingIRI, String OwlNothingIRI) {
		this.statementOntology_ = statementOntology;
		this.kernelOwlReasoner_ = kernelOwlReasoner;
		this.statementOwlReasoner_ = statementOwlReasoner;
		this.statementConceptNamespace = statementConceptNamespace;
		this.statementConceptName = statementConceptName;
		this.temporalAnnotationOwlIRI = temporalAnnotationOwlIRI;
		this.owlThingIRI = owlThingIRI;
		this.owlNothingIRI = owlNothingIRI;
		subsumptionNormalFormBuilder = new SubsumptionNormalFormBuilderSHIELD(kernelOntology,
				 																 statementOntology,
				 																 kernelOwlReasoner,
				 																 statementOwlReasoner);	
		subsumptionNormalFormBuilder.init();
		subsumptionNormalFormBuilder.generate();
		subsumptionTester = new CustomSubsumptionTesterSHIELD(kernelOntology, this.statementConceptNamespace, this.statementConceptName, 
				                                              this.temporalAnnotationOwlIRI, this.owlThingIRI, this.owlNothingIRI);
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	
	public void classifyStatementConcepts(OWLOntology kernelOntology, OWLOntology statementOntology, ElkReasoner kernelElkReasoner, OWLReasoner statementOwlReasoner) {
		try {
			ConcurrentClassTaxonomy destinationTaxonomy = (ConcurrentClassTaxonomy) kernelElkReasoner.getInternalReasoner().getTaxonomy();
//DEBUG System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN CLASSIFIER - BEFORE REMOVAL OF STATEMENT-CONCEPT");
//DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);
			boolean removed = removeStatementHierachyFromKernelTaxonomy("Statement-Concept", statementOntology, statementOwlReasoner, destinationTaxonomy); 
			if (!removed) {
				throw new RuntimeException("Cannot remove Statement hierarchy from Kernel reasoner taxonomy; hierarchy could not be found.");
			}
//DEBUG System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN CLASSIFIER - AFTER REMOVAL OF STATEMENT-CONCEPT");
//DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);

			TaxonomyNode<ElkClass> rootKernelTaxonomyNodeForClassification = copyNamedNodeFromStatementToKernelReasoner("Statement-Concept", statementOntology, statementOwlReasoner, kernelElkReasoner);
//DEBUG System.out.println("ORIGINAL KERNEL REASONER TAXONOMY - IN CLASSIFIER");
//DEBUG ReasonerExplorer.printCurrentReasonerTaxonomy((ElkReasoner) kernelElkReasoner, false);
			NodeSet<OWLClass> subNodes = getSubsumedNodesFromOwlReasoner("Statement-Concept", statementOwlReasoner, statementOntology);

// DEBUG for (Node<OWLClass> node : subNodes) {
// DEBUG	System.out.println("****TRYING TO CLASSIFY: "+ node.getEntities().iterator().next().getIRI().toString());
// DEBUG}
			
			for (Iterator<Node<OWLClass>> iterator = subNodes.iterator(); iterator.hasNext();) {
				Node<OWLClass> candidateNode = (Node<OWLClass>) iterator.next();
				if (!candidateNode.isBottomNode() && 
					logicallyDefinedInOntology(candidateNode.getEntities().iterator().next(), statementOntology)) {
						Node<OWLClass> rootKernelTaxonomyNodeForClassificationAsOwlNode = 
								ElkConverter.getInstance().convertClassNode(rootKernelTaxonomyNodeForClassification);
						if (rootKernelTaxonomyNodeForClassificationAsOwlNode.equals(kernelElkReasoner.getTopClassNode())) {
							throw new RuntimeException("Cannot migrate Statement Concepts to beneath top node of Kernel Concepts; must migrate to lower 'Statement-Concept' node instead");
						}
						TaxonomyNode<ElkClass> newCandidateElkClassNode = createElkTaxonomyReasonerNode(candidateNode, destinationTaxonomy);
						OWLClass candidateClass = candidateNode.getEntities().iterator().next();
						SubsumptionNormalFormSHIELD candidateSNF = subsumptionNormalFormBuilder.getSNF(candidateClass);
// DEBUG System.out.println("****TRYING TO CLASSIFY: "+ newCandidateElkClassNode.getMembers().iterator().next().getIri().toString());
						classifyStatementConcept(newCandidateElkClassNode, candidateSNF, rootKernelTaxonomyNodeForClassification, destinationTaxonomy);
						}
			}
		} catch (ElkInconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ElkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  Note:  This method assumes that the root of the statement hierarchy is a direct child of the kernelElkReasoner taxonomy top node
	//  TODO:  Generalize this method not to make the assumption above, but to use a breadth-first search for the root in the entire kernelElkReasoner taxonomy
	private boolean removeStatementHierachyFromKernelTaxonomy(String targetName, OWLOntology statementOntology, OWLReasoner statementOwlReasoner, ConcurrentClassTaxonomy kernelElkReasonerTaxonomy) {
		Node<OWLClass> targetNode = getNamedOwlClassNodeFromStatementReasoner(targetName, statementOntology, statementOwlReasoner);
		TaxonomyNode<ElkClass> taxonomyTopNode = kernelElkReasonerTaxonomy.getTopNode();  
		for (TaxonomyNode<ElkClass> childOfTaxonomyTopNode : taxonomyTopNode.getDirectSubNodes()) {  // iterate through children
			for (ElkClass currentElkClass : childOfTaxonomyTopNode.getMembers()) { // iterate through each member of the child (an ElkClass)
				if(ElkConverter.getInstance().convert(currentElkClass).equals(targetNode.getEntities().iterator().next())) { 
					removeTaxonomySubTree(childOfTaxonomyTopNode, taxonomyTopNode, kernelElkReasonerTaxonomy);
					return true;
				}
			}
		}
		return false;  // if the targetName node is not found as a child of the taxonomyTopNode
	}

	private void removeTaxonomySubTree(TaxonomyNode<ElkClass> taxonomyNode, TaxonomyNode<ElkClass> parentTaxonomyNode,
			ConcurrentClassTaxonomy kernelElkReasonerTaxonomy) {
		List<TaxonomyNode<ElkClass>> list = new ArrayList<>(taxonomyNode.getDirectSubNodes()); // convert to List so it
																								// will be mutable
		for (TaxonomyNode<ElkClass> childOfTaxonomyNode : list) { // iterate through children
			if (childOfTaxonomyNode instanceof NonBottomClassNode)  // prevents attempts to remove BottomClassNodes, which throw error if updated, and will have no child nodes
				removeTaxonomySubTree(childOfTaxonomyNode, taxonomyNode, kernelElkReasonerTaxonomy); // perform
																										// depth-first
																										// removal of
																										// all nodes in
																										// sub-tree
		}
		disconnectNodes((NonBottomClassNode) taxonomyNode, (NonBottomClassNode) parentTaxonomyNode);
		kernelElkReasonerTaxonomy.removeNode((UpdateableTaxonomyNode<ElkClass>) taxonomyNode);
	}


	public boolean classifyStatementConcept(TaxonomyNode<ElkClass> candidateNode, SubsumptionNormalFormSHIELD candidateSNF, TaxonomyNode<ElkClass> predicateNode, ConcurrentClassTaxonomy destinationTaxonomy) {
		 boolean successfullyClassified = false;
//DEBUG if (candidateNode.getMembers().iterator().next().getIri().toString().contains("Temporally")) {
//DEBUG 	   System.out.println("Candidate:  " + candidateNode.getMembers().iterator().next().getIri().toString());
//DEBUG       System.out.println("Predicate:  " + predicateNode.getMembers().iterator().next().getIri().toString());
//DEBUG  }
		 if (predicateNode.equals( ((ElkReasoner)kernelOwlReasoner_).getTopClassNode())) {
			 throw new RuntimeException("Cannot classify statement concepts directly below the kernel taxonomy top node.");
			 // THAT SITUATION CANNOT BE HANDLED B/C THERE'S NO SNF FOR THE TOP-CLASS NODE (OWL:THING CONCEPT), AND WE DON'T
			 // WANT THE STATEMENT CLASSIFIER SEARCHING THE LARGE SET OF KERNEL CONCEPTS FOR POTENTIAL SUBSUMEES.  IT MUST START AT THE
			 // "STATEMENT-CONCEPT" SUBNODE, WHICH WE KNOW WILL SUBSUME A TAXONOMY SUB-TREE THAT IS DISJOINT FROM THE KERNEL TAXONOMY/ONTOLOGY.
		}
		 if (predicateNode instanceof UpdateableBottomNode) {  // If bottom reached, stop recursion and return false -- candidate can't be subsumed by bottom ("nothing") node
			 return false;
			}
		 ElkConverter elkConverter = ElkConverter.getInstance();
		 OWLClass predicateClass = elkConverter.convert(predicateNode.getMembers().iterator().next());
		 SubsumptionNormalFormSHIELD predicateSNF = subsumptionNormalFormBuilder.getSNF(predicateClass);
// DEBUG if (	predicateNode.getMembers().iterator().next().getIri().toString()
// DEBUG		.contains("Statement-of-Finding")	) {
// DEBUG     		System.out.println("Candidate:  " + candidateNode.getMembers().iterator().next().getIri().toString());
// DEBUG     		System.out.println("Predicate:  " + predicateNode.getMembers().iterator().next().getIri().toString());
// DEBUG }
		 if (subsumptionTester.isSubsumedBy(candidateSNF, predicateSNF, kernelOwlReasoner_, statementOwlReasoner_) &&
			 !candidateNode.equals(predicateNode)) { // We know the candidate 
			 for (TaxonomyNode<ElkClass> predicateChildNode : predicateNode.getDirectSubNodes()) {
				 // Recurses here
				 if ( classifyStatementConcept(candidateNode, candidateSNF, predicateChildNode, destinationTaxonomy) ) {
				 successfullyClassified = true; 
				 }
			 }
			 if (successfullyClassified == false) {  // candidate was not subsumed by any of the predicates' descendants, so connect it as a new child of the predicate itself
				 if (!nodeAlreadyConnectedTo(candidateNode, predicateNode)) { // Could occur if the candidateNode can arrive at the predicateNode via multiple paths (i.e., if multiple inheritance exists)
				     // connect candidate to predicate, then go on to find most general subsumees of the candidate node
					connectNodes((NonBottomClassNode) candidateNode, (NonBottomClassNode) predicateNode);
// DEBUG	System.out.println("Classifying " + candidateNode.getMembers().iterator().next().getIri() + " below " + predicateNode.getMembers().iterator().next().getIri());
					findAndConnectMostGeneralSubsumees(candidateNode, candidateSNF, predicateNode, destinationTaxonomy);  // We know that, if any exist, they must be descendants of the predicate node
				 }
			 }
			 return true;
		 }
		 else return false;
	}

	private void findAndConnectMostGeneralSubsumees(TaxonomyNode<ElkClass> predicateNode, SubsumptionNormalFormSHIELD predicateSNF, TaxonomyNode<ElkClass> parentCandidateNode, ConcurrentClassTaxonomy destinationTaxonomy) {
		Set<? extends TaxonomyNode<ElkClass>> set = parentCandidateNode.getDirectSubNodes();
		List<TaxonomyNode<ElkClass>> list = new ArrayList<>(set);  // Converted to List so it will be mutable
		for (TaxonomyNode<ElkClass> candidateNode : list) {  // for each child of the parentCandidateNode, check if it is subsumed by the predicateNode
			if (candidateNode instanceof UpdateableBottomNode) {  
				 // Do nothing and exit; no need to connect the bottom node to the predicate node -- it already has a bottom node below it by default if it has no children
				}
			 else {
				 OWLClass candidateClass = ElkConverter.getInstance().convert(candidateNode.getMembers().iterator().next());  
				 SubsumptionNormalFormSHIELD candidateSNF = subsumptionNormalFormBuilder.getSNF(candidateClass);  // Check if the current child of parentCandidateNode is subsumed by the predicateNode
				 if (subsumptionTester.isSubsumedBy(candidateSNF, predicateSNF, kernelOwlReasoner_, statementOwlReasoner_) &&
				     ! candidateNode.equals(predicateNode) ) {  // Only want properly subsumed concepts, not self-subsumed
					 if (!nodeAlreadySubsumedBy(candidateNode, predicateNode)) {  // Another path may already exist from predicateNode to candidateNode if multiple inheritance
						 connectNodes((NonBottomClassNode) candidateNode, (NonBottomClassNode) predicateNode);  // Want to connect the *top-most* subsumee, so don't recurse below here
// DEBUG System.out.println("Classifying " + predicateNode.getMembers().iterator().next().getIri() + " above " + candidateNode.getMembers().iterator().next().getIri());

					 	if (predicateNodeAlsoHasParentCandidateNodeAsASuperClass(predicateNode, parentCandidateNode)) {  // i.e., if candidateNode and predicateNode are currently siblings
						disconnectNodes((NonBottomClassNode) candidateNode, (NonBottomClassNode) parentCandidateNode);  // the existing link provides no additional information and is redundant
// DEBUG System.out.println("Disconnecting link between " + candidateNode.getMembers().iterator().next().getIri() + " and its parent " + parentCandidateNode.getMembers().iterator().next().getIri());
					 	}
					 }
				 }
				 else findAndConnectMostGeneralSubsumees(predicateNode, predicateSNF, candidateNode, destinationTaxonomy);  // if the current child of parentCandidateNode is not subsumed by the predicateNode, recurse to the current child's children and continue looking
			 }
		 }
	}
	
	public static TaxonomyNode<ElkClass> createElkTaxonomyReasonerNode(Node<OWLClass> sourceNode, ConcurrentClassTaxonomy destinationElkTaxonomy) {
		Set<ElkClass> membersToMove = new HashSet<ElkClass>();
		OwlConverter converter = OwlConverter.getInstance();
		for (OWLClass clazz : sourceNode.getEntities()) 
			membersToMove.add(converter.convert(clazz));
		TaxonomyNode<ElkClass> newDestinationNode = destinationElkTaxonomy.getCreateNonBottomClassNode(membersToMove);
		return newDestinationNode;
	}

	private void connectNodes(NonBottomClassNode subNode, NonBottomClassNode superNode) {
		superNode.addDirectSubNode(subNode);
		subNode.addDirectSuperNode(superNode);				

	}
	
	private void disconnectNodes(NonBottomClassNode subNode, NonBottomClassNode superNode) {
		superNode.removeDirectSubNode(subNode);
		subNode.removeDirectSuperNode(superNode);				

	}

	private boolean predicateNodeAlsoHasParentCandidateNodeAsASuperClass(TaxonomyNode<ElkClass> predicateNode, TaxonomyNode<ElkClass> parentCandidateNode) {
		if (predicateNode.getDirectSuperNodes().contains(parentCandidateNode)) {
			return true;
		}
		else return false;
	}
	
	private boolean nodeAlreadyConnectedTo(TaxonomyNode<ElkClass> candidateNode, TaxonomyNode<ElkClass> predicateNode) {
		if (candidateNode.getDirectSuperNodes().contains(predicateNode)) {
			return true;
		}
		else return false;
	}
	
	private boolean nodeAlreadySubsumedBy(TaxonomyNode<ElkClass> candidateNode, TaxonomyNode<ElkClass> predicateNode) {
		if (candidateNode.getAllSuperNodes().contains(predicateNode)) {
			return true;
		}
		else return false;
	}
	
	

	private NodeSet<OWLClass> getSubsumedNodesFromOwlReasoner(String conceptName, OWLReasoner reasoner, OWLOntology ontology) {
		NodeSet<OWLClass> taxonomyNodesToReturn = null; // initialize outside try/catch block
		OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
	    OWLClass namedClass = factory.getOWLClass(
	       IRI.create("http://www.hhs.fda.org/shield/SWEC-Ontology#" + conceptName));
	    taxonomyNodesToReturn = reasoner.getSubClasses(namedClass, false);	
		return taxonomyNodesToReturn;
	}

	private TaxonomyNode<ElkClass> copyNamedNodeFromStatementToKernelReasoner(String targetNodeName,
			OWLOntology statementOntology, OWLReasoner statementOwlReasoner, ElkReasoner kernelElkReasoner) {
		TaxonomyNode<ElkClass> newDestinationElkClassNode = null;
		try {
			// First, retrieve the OWLClass node that represents the root concept of the statement hierarchy
			// from the statementOwlReasoner ("Statement-Concept", in the default version of the statement sub-ontology)
			Node<OWLClass> targetNode = getNamedOwlClassNodeFromStatementReasoner(targetNodeName,
				statementOntology, statementOwlReasoner);
			if (targetNode == null) {
				throw new RuntimeException("Named node to move to kernelReasoner not fount in the StatementReasoner: "
						+ "http://www.hhs.fda.org/shield/SWEC-Ontology#" + targetNodeName);
			}
			if (targetNode.isBottomNode()) {
				throw new RuntimeException(
						"Named node to move to under the top of the kernelReasoner is the bottom node.  That operation not allowed.");
			}
			// Now, create a new TaxonomyNode under the top node of the destinationTaxonomy, and put the target OWLClass node
			// within it.  This will be the root of the Statement sub-hierarchy in the destinationTaxonomy.
			ConcurrentClassTaxonomy destinationTaxonomy = (ConcurrentClassTaxonomy) kernelElkReasoner
					.getInternalReasoner().getTaxonomy();
			TaxonomyNode<ElkClass> destinationTaxonomyTopNode = destinationTaxonomy.getTopNode();
// DEBUG System.out.println("Node Class to Migrate: " + targetNode.getEntities().iterator().next().getIRI());
			newDestinationElkClassNode = createElkTaxonomyReasonerNode(targetNode, destinationTaxonomy);
			((NonBottomClassNode) destinationTaxonomyTopNode)
					.addDirectSubNode((UpdateableTaxonomyNode<ElkClass>) newDestinationElkClassNode);
				((NonBottomClassNode) newDestinationElkClassNode)
						.addDirectSuperNode((UpdateableTaxonomyNode<ElkClass>) destinationTaxonomyTopNode);
		} catch (ElkInconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ElkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newDestinationElkClassNode;
	}
	
	private Node<OWLClass> getNamedOwlClassNodeFromStatementReasoner(String targetNodeName,
			OWLOntology statementOntology, OWLReasoner statementOwlReasoner) {
			// First, retrieve the OWLClass node that represents the root concept of the statement hierarchy
			// from the statementOwlReasoner ("Statement-Concept", in the default version of the statement sub-ontology)
			OWLDataFactory factory = statementOntology.getOWLOntologyManager().getOWLDataFactory();
			OWLClass targetNamedClass = factory
					.getOWLClass(IRI.create("http://www.hhs.fda.org/shield/SWEC-Ontology#" + targetNodeName));
			OWLClass topStatementReasonerClassNode = statementOwlReasoner.getTopClassNode().getEntities().iterator()
					.next();
			NodeSet<OWLClass> subNodes = statementOwlReasoner.getSubClasses(topStatementReasonerClassNode, false);
			Node<OWLClass> targetNode = null; // will be assigned below if a node containing the targetNodeName class is
										      // among the nodes in the statementOwlReasoner taxonomy
			for (Iterator<Node<OWLClass>> iterator = subNodes.iterator(); iterator.hasNext();) {
				Node<OWLClass> subNode = (Node<OWLClass>) iterator.next();
				if (subNode.getEntities().iterator().next().equals(targetNamedClass)) {
					targetNode = subNode;
				}
			}
			return targetNode;
	}

	@SuppressWarnings("deprecation")
	public static boolean logicallyDefinedInOntology(OWLClass candidateClass, OWLOntology ontology) {
		Set<OWLClassAxiom> classAxioms = ontology.getAxioms(candidateClass);
		boolean isLogicallyDefined = classAxioms.stream().anyMatch(x -> x.isLogicalAxiom());
		return isLogicallyDefined;
	}

}
 