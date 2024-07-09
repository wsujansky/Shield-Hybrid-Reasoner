package gov.hhs.fda.shield;


import java.text.Collator;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owlapi.ElkConverter;
import org.semanticweb.elk.owlapi.wrapper.ElkClassWrap;
import org.semanticweb.elk.reasoner.taxonomy.ConcurrentClassTaxonomy;
import org.semanticweb.elk.reasoner.taxonomy.NonBottomClassNode;
import org.semanticweb.elk.reasoner.taxonomy.model.UpdateableBottomNode;
import org.semanticweb.elk.reasoner.taxonomy.model.TaxonomyNode;
import org.semanticweb.elk.reasoner.taxonomy.model.UpdateableTaxonomyNode;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;


	public class TaxonomyEditingTesterSHIELD {
		
		public TaxonomyEditingTesterSHIELD() {
			// TODO Auto-generated constructor stub
		}
		

		public static void addSubClassNode (Node<OWLClass> node) {
			//no op for now

			Set<OWLClass> entities = node.getEntities();
			for (Iterator<OWLClass> iterator = entities.iterator(); iterator.hasNext();) {
				OWLClass owlClass = (OWLClass) iterator.next();
				@SuppressWarnings("unused")
				ElkClassWrap<OWLClass> ek = new ElkClassWrap<OWLClass>(owlClass);
			}
		}
		
		public synchronized void printTaxonomy(TaxonomyNode<ElkClass> node, int level, boolean alreadyDone, ConcurrentClassTaxonomy taxonomy, boolean makeChange) {  
			
			if (node instanceof UpdateableBottomNode) {  // don't print and stop recursion  
//				ElkClass elkClass = node.getMembers().iterator().next();  
//				System.out.println(indentSpaces(level) + getShortFormElkClassName(elkClass));
				}
			else {
				ElkClass elkClass = node.getMembers().iterator().next();
				System.out.println(indentSpaces(level) + getShortFormElkClassName(elkClass));
				Set<UpdateableTaxonomyNode<ElkClass>> subNodes = ((NonBottomClassNode) node).getDirectUpdateableSubNodes(); 
				LinkedList<UpdateableTaxonomyNode<ElkClass>> sortedSubNodes = sortNodes(subNodes);
				for (Iterator<UpdateableTaxonomyNode<ElkClass>> iterator = sortedSubNodes.iterator(); iterator.hasNext();) {
					TaxonomyNode<ElkClass> elkSubNode = (TaxonomyNode<ElkClass>) iterator.next();
					printTaxonomy(elkSubNode, level+1, alreadyDone, taxonomy, makeChange);  
					}
			}
		}
		
		public synchronized void printTaxonomy(Node<OWLClass> node, int level, boolean alreadyDone, StructuralReasoner reasoner, boolean makeChange) {  
			if (node.equals(reasoner.getBottomClassNode()))  {
//			if (node.isBottomNode()) {  // don't print and stop recursion  
//				printTaxonomy(node, level, alreadyDone, reasoner, makeChange);
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

		public static String indentSpaces(int level) {
			String indentString = "";
			for (int i = 0; i < level*2; i++) 
			   {indentString = indentString + " ";}
			return indentString;
		}
		
		private String getShortFormElkClassName(ElkClass elkClass) {
			OWLClass owlClass = ElkConverter.getInstance().convert(elkClass);
			return owlClass.getIRI().getShortForm();
		}
		
		private LinkedList<UpdateableTaxonomyNode<ElkClass>> sortNodes(Set<UpdateableTaxonomyNode<ElkClass>> inputSet) {
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
	
	}
