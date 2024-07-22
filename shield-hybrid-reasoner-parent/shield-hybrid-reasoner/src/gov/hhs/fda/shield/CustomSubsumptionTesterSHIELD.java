package gov.hhs.fda.shield;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owlapi.ElkConverter;
import org.semanticweb.elk.reasoner.taxonomy.model.TaxonomyNode;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import gov.hhs.fda.shield.temporalreasoning.TemporalExpressionSubsumptionTester;

public class CustomSubsumptionTesterSHIELD {
	OWLOntology ontology_;
	OWLOntologyManager manager_;
    TemporalExpressionSubsumptionTester temporalSubsumptionTester;
	private String statementConceptNamespace;
	private String statementConceptName;
	private String temporalAnnotationOwlIRI;
	private String owlThingIRI;
	private String owlNothingIRI;



	public CustomSubsumptionTesterSHIELD(OWLOntology ontology) {
		this.ontology_ = ontology; 
		this.manager_ = ontology.getOWLOntologyManager();
		this.statementConceptNamespace = DefaultProperties.STATEMENT_CONCEPT_NAMESPACE;
		this.statementConceptName = DefaultProperties.STATEMENT_CONCEPT_NAME;
		this.temporalAnnotationOwlIRI = DefaultProperties.TEMPORAL_ANNOTATION_OWL_IRI;
		this.owlThingIRI = DefaultProperties.OWL_NOTHING_IRI;
		this.owlNothingIRI = DefaultProperties.OWL_NOTHING_IRI;
        this.temporalSubsumptionTester = new TemporalExpressionSubsumptionTester();
	}
	
	public CustomSubsumptionTesterSHIELD(OWLOntology ontology, String statementConceptNamespace, String statementConceptName, String temporalAnnotationOwlIRI,
			                             String owlThingIRI, String OwlNothingIRI) {
		this.ontology_ = ontology; 
		this.manager_ = ontology.getOWLOntologyManager();
		this.statementConceptNamespace = statementConceptNamespace;
		this.statementConceptName = statementConceptName;
		this.temporalAnnotationOwlIRI = temporalAnnotationOwlIRI;
		this.owlThingIRI = owlThingIRI;
		this.owlNothingIRI = owlNothingIRI;
        this.temporalSubsumptionTester = new TemporalExpressionSubsumptionTester();
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
/**	NOT USED, I THINK
	public boolean TaxonomyNodeIsSubsumedBy(TaxonomyNode<ElkClass> subNode, TaxonomyNode<ElkClass> superNode, OWLOntology statementOnt, OWLReasoner kernelReasoner) {
		ElkClass subNodeClassExpression = subNode.getMembers().iterator().next();
		ElkClass superNodeClassExpression = superNode.getMembers().iterator().next();
		// TODO:  ?? Maybe iterate through all members, in case there is more than one per node?
		if (superNodeClassExpression.getIri().toString().equals(owlThingIRI) ||
		        subNodeClassExpression.getIri().toString().equals(owlNothingIRI)) {
				return true;
		}
		else
		{
		OWLClassExpression subNodeClassDefinitionExpression = getNormalizedAxiomDefinition(subNodeClassExpression, statementOnt);
		OWLClassExpression superNodeClassDefinitionExpression = getNormalizedAxiomDefinition(superNodeClassExpression, statementOnt);
		return classExpressionIsSubsumedBy(subNodeClassDefinitionExpression, superNodeClassDefinitionExpression, kernelReasoner);
		}
	}
**/
	
	
	public boolean isSubsumedBy(OWLClassExpression subNodeClassExpression, OWLClassExpression superNodeClassExpression, OWLReasoner owlReasoner) {
		if (pattern(subNodeClassExpression, superNodeClassExpression, Pattern.BOTH_OWLCLASS)) {
			return isSubsumedBy((OWLClass) subNodeClassExpression, (OWLClass) superNodeClassExpression, owlReasoner);
		}
		else if (pattern(subNodeClassExpression, superNodeClassExpression, Pattern.BOTH_OWLOBJECTSOMEVALUES)) {
			return isSubsumedBy((OWLObjectSomeValuesFrom) subNodeClassExpression, (OWLObjectSomeValuesFrom) superNodeClassExpression, owlReasoner);
		}
		else if (pattern(subNodeClassExpression, superNodeClassExpression, Pattern.BOTH_OWLPROPERTY)) {
			return isSubsumedBy((OWLObjectProperty) subNodeClassExpression, (OWLObjectProperty) superNodeClassExpression);
		}
		else if (pattern(subNodeClassExpression, superNodeClassExpression, Pattern.BOTH_OWLOBJECTINTERSECTION)) {
			return isSubsumedBy((OWLObjectIntersectionOf) subNodeClassExpression, (OWLObjectIntersectionOf) superNodeClassExpression, owlReasoner);
		}
		else
		return false;  // Default if the pair of expression types aren't handled above 
					   // (i.e., the OWLClassExpression types can't be compared or are unrecognized)
	}
	
	public boolean isSubsumedBy(OWLObjectIntersectionOf subNodeClassExpression, OWLObjectIntersectionOf superNodeClassExpression, OWLReasoner owlReasoner) {
		Set<OWLClassExpression> subExpressions = subNodeClassExpression.getOperands();
		Set<OWLClassExpression> superExpressions = superNodeClassExpression.getOperands();
		for (OWLClassExpression subExpr : subExpressions) {
			for (OWLClassExpression superExpr : superExpressions) {
				if (subExpr == superExpr)
					return true;
				return superExpressions.stream()
						.allMatch(p2 -> subExpressions.stream().anyMatch(p1 -> isSubsumedBy(p1, p2, owlReasoner)));
			}
		}
		return false;
	}
		

	public boolean isSubsumedBy(OWLObjectSomeValuesFrom subNodeClassExpression, OWLObjectSomeValuesFrom superNodeClassExpression, OWLReasoner owlReasoner) {
        OWLObjectProperty subNodeproperty = subNodeClassExpression.getProperty().getNamedProperty();
        OWLClassExpression subNodeFiller = subNodeClassExpression.getFiller();
        OWLObjectProperty superNodeProperty = superNodeClassExpression.getProperty().getNamedProperty();
        OWLClassExpression superNodeFiller = superNodeClassExpression.getFiller();
//DEBUG System.out.println("Sub property: " + subNodeClassExpression);
//DEBUG System.out.println("Super property " + superNodeClassExpression);
        if (isSubsumedBy(subNodeproperty,superNodeProperty) &&
        	isSubsumedBy(intersectionIfSingleton(subNodeFiller), intersectionIfSingleton(superNodeFiller), owlReasoner) ) {
        	return true;
        }
        else
        {
        return false;
        }
	}

	public boolean isSubsumedBy(OWLObjectProperty subNodePropertyExpression, OWLObjectProperty superNodePropertyExpression) {

//DEBUG if (subNodePropertyExpression.getIRI().toString().contains("STARTS-WITHIN")) {
//DEBUG System.out.println("Sub object property: " + subNodePropertyExpression);
//DEBUG System.out.println("Super object property " + superNodePropertyExpression);
//DEBUG }
		if (subNodePropertyExpression.equals(superNodePropertyExpression))
			return true; // TODO: Later, generalize to handle property hierarchies and chains in the
						 // kernel ontology
		if (hasTemporalDefinition(subNodePropertyExpression) && hasTemporalDefinition(superNodePropertyExpression))
			return isPropertySubsumedByAsInferred(subNodePropertyExpression, superNodePropertyExpression);
		if (!hasTemporalDefinition(subNodePropertyExpression) && !hasTemporalDefinition(superNodePropertyExpression)) 
			return isPropertySubsumedByAsStated(subNodePropertyExpression, superNodePropertyExpression);
		return false;
	}
	
	public boolean isSubsumedBy(OWLClass subNodeOwlClass, OWLClass superNodeOwlClass, OWLReasoner owlReasoner) {
		if (subNodeOwlClass.equals(superNodeOwlClass))
			return true;  // equivalent classes subsume each other
		Set<OWLClass> subClassSet = owlReasoner.getSubClasses(superNodeOwlClass, false).getFlattened();
	    for (Iterator<OWLClass> iterator = subClassSet.iterator(); iterator.hasNext();) {
			OWLClass candidateOwlSubClass = (OWLClass) iterator.next();
			if (candidateOwlSubClass.equals(subNodeOwlClass)) {
				return true;
			}
		}
		return false;  
	}
	

	
	// Reverses subsumption logic if both expressions are negated ("absent"); otherwise, standard logic applies.
	public boolean isSubsumedBy(SubsumptionNormalFormSHIELD subSNF, SubsumptionNormalFormSHIELD superSNF, OWLReasoner kernelReasoner, OWLReasoner statementReasoner) {
		if (subSNF.isAbsent() && superSNF.isAbsent()) 
			return isSubsumedByInternal(superSNF, subSNF, kernelReasoner, statementReasoner);
		else
			return isSubsumedByInternal(subSNF, superSNF, kernelReasoner, statementReasoner);
	}
	
	private boolean isSubsumedByInternal(SubsumptionNormalFormSHIELD subSNF, SubsumptionNormalFormSHIELD superSNF, OWLReasoner kernelReasoner, OWLReasoner statementReasoner) {
		boolean focusConceptsSubsumedBy =  superSNF.getFocusConcepts().stream()
                .allMatch(conSup -> subSNF.getFocusConcepts().stream()
                 .anyMatch(conSub -> isSubsumedBy(conSub, conSup, statementReasoner)));
		boolean rolesSubsumedBy =  superSNF.getUngroupedProps().stream()
                .allMatch(propSup -> subSNF.getUngroupedProps().stream()
                 .anyMatch(propSub -> isSubsumedBy(propSub, propSup, kernelReasoner)));
		boolean isSubsumedBy = focusConceptsSubsumedBy && rolesSubsumedBy;
		return isSubsumedBy;
	}

	private boolean isPropertySubsumedByAsStated(OWLObjectProperty subNodePropertyExpression, OWLObjectProperty superNodePropertyExpression) {
		if (subNodePropertyExpression.equals(superNodePropertyExpression))
			return true;
		if (subNodePropertyExpression.isOWLTopObjectProperty())
			return false;
		Set<OWLObjectProperty> directParentPropertiesAsStated = getDirectParentPropertiesAsStated(subNodePropertyExpression);
		for (Iterator<OWLObjectProperty> iterator = directParentPropertiesAsStated.iterator(); iterator.hasNext();) {
			OWLObjectProperty candidateSubsumedProperty = (OWLObjectProperty) iterator.next();
				if (isPropertySubsumedByAsStated(candidateSubsumedProperty, superNodePropertyExpression))
					return true;
			}
			return false;
	}
	
	private boolean isPropertySubsumedByAsInferred(OWLObjectProperty subNodePropertyExpression, OWLObjectProperty superNodePropertyExpression) {
      String superExpression = getTemporalPropertyDefinitionAsString(superNodePropertyExpression);
      String subExpression = getTemporalPropertyDefinitionAsString(subNodePropertyExpression);
		return temporalSubsumptionTester.subsumes(superExpression, subExpression);
	}

	private boolean hasTemporalDefinition(OWLObjectProperty owlProperty) {
	    Collection<OWLAnnotation> annotations = EntitySearcher.getAnnotations(owlProperty, ontology_);
	    for (Iterator<OWLAnnotation> iterator = annotations.iterator(); iterator.hasNext();) {
	    	OWLAnnotation owlAnnotation = (OWLAnnotation) iterator.next();
	    	if (owlAnnotation.getProperty().getIRI().toString().equals(this.temporalAnnotationOwlIRI)) 
	    		return true; 
	    	}
		return false;
	}
	
	private String getTemporalPropertyDefinitionAsString(OWLObjectProperty propertyExpression) {
		String definition = "";  // 
	    Collection<OWLAnnotation> annotations = EntitySearcher.getAnnotations(propertyExpression, ontology_);
	    for (Iterator<OWLAnnotation> iterator = annotations.iterator(); iterator.hasNext();) {
	    	OWLAnnotation owlAnnotation = (OWLAnnotation) iterator.next();
	    	if (owlAnnotation.getProperty().getIRI().toString().equals(this.temporalAnnotationOwlIRI)) {
	    		String quotedValue = owlAnnotation.getValue().toString();
	    		return quotedValue.substring(1, quotedValue.length() - 1); // remove beginning/ending double quotes
	    	}
	    }
		return definition;
	}

	@SuppressWarnings("deprecation")
	private Set<OWLObjectProperty> getDirectParentPropertiesAsStated(OWLObjectProperty subNodePropertyExpression) {
		Set<OWLObjectProperty> parentProperties = new HashSet<OWLObjectProperty>(); // this will be returned
		Set<OWLObjectPropertyAxiom> subPropertyAxioms = this.ontology_.getAxioms(subNodePropertyExpression);
		for (Iterator<OWLObjectPropertyAxiom> iterator = subPropertyAxioms.iterator(); iterator.hasNext();) {
			OWLObjectPropertyAxiom subPropertyAxiom = (OWLObjectPropertyAxiom) iterator.next();
			if (subPropertyAxiom instanceof OWLSubObjectPropertyOfAxiom) {
				OWLObjectProperty superProperty = (((OWLSubObjectPropertyOfAxiom) subPropertyAxiom).getSuperProperty().asOWLObjectProperty());
				parentProperties.add(superProperty);
			}
		}
		return parentProperties;
	}


	//For testing only
/** NOT USED, I THINK
	public boolean classExpressionIsSubsumedBy(String subClassName, String superClassName, OWLOntology ontology, OWLReasoner kernelReasoner, OWLReasoner classifiedReasoner) {
		IRI ontologyIRI = IRI.create(statementConceptNamespace + "#");
		OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
		OWLClass subClass = factory.getOWLClass(IRI.create(ontologyIRI + subClassName));
		OWLClass superClass = factory.getOWLClass(IRI.create(ontologyIRI + superClassName));
		boolean kernelSubsumption = isSubsumedBy(subClass, superClass, kernelReasoner);
		boolean classifiedSubsumption = isSubsumedBy(subClass, superClass, classifiedReasoner);	
		return (kernelSubsumption || classifiedSubsumption);
	}

	
	public boolean classExpressionIsSubsumedBy(OWLClassExpression subNodeClassExpression, OWLClassExpression superNodeClassExpression, OWLReasoner kernelReasoner) {
		return false;  // Default if the pair of expression types aren't handled above 
					   // (i.e., don't match or are unrecognized)
	}
	

	@SuppressWarnings("deprecation")
	public static OWLClassExpression getNormalizedAxiomDefinition(ElkClass elkClass, OWLOntology ontology) {
		OWLClass owlClass = ElkConverter.getInstance().convert(elkClass);
        Set<OWLAxiom> referencingAxioms = ontology.getReferencingAxioms(owlClass, true);
        for (Iterator<OWLAxiom> iterator = referencingAxioms.iterator(); iterator.hasNext();) {
			OWLAxiom owlAxiom = (OWLAxiom) iterator.next();
			if (owlAxiom instanceof OWLEquivalentClassesAxiom) {
				Set<OWLClassExpression> nestedOwlClassExpressions = ((OWLEquivalentClassesAxiom) owlAxiom).getClassExpressions();
				for (Iterator<OWLClassExpression> iterator2 = nestedOwlClassExpressions.iterator(); iterator2.hasNext();) {
					OWLClassExpression owlClassExpression = (OWLClassExpression) iterator2.next();
						if (owlClassExpression.equals((OWLClassExpression) owlClass)) {
							System.out.println("    DEFINING AXIOM: " + owlAxiom);
						}
					}
			}
			// TODO:  Add else clause to also handle SubClassOfAxioms
			// TODO:  Replace "return owlClass" with the OWLClassExpression that defines the input ELK Class
			//        (will just need to call owlAxiom.getClassExpressionMinus(the Elk Class)
			// TODO:  Handle exception that will occur for any elk classes that don't have a defining
			//        OWLEquivalentClassesAxiom or OWLSubClassOfAxiom (specifically, Declaration axioms).
			//        What OWLClassExpression instance should be returned by the method in this case?
        }
		return owlClass;  //TEMP placeholder; return the 2nd OWLClassExpression in owlAxiom, unless it's an exception
	}
**/
	
	private boolean pattern(OWLClassExpression subNodeClassExpression, OWLClassExpression superNodeClassExpression, Pattern targetPattern) {
		Pattern thisPattern = Pattern.NOTHANDLEDPATTERN;
		if (subNodeClassExpression instanceof OWLClass && superNodeClassExpression instanceof OWLClass)  
			thisPattern = Pattern.BOTH_OWLCLASS;
		else if (subNodeClassExpression instanceof OWLObjectSomeValuesFrom && superNodeClassExpression instanceof OWLObjectSomeValuesFrom) 
			thisPattern = Pattern.BOTH_OWLOBJECTSOMEVALUES;
		else if (subNodeClassExpression instanceof OWLObjectProperty && superNodeClassExpression instanceof OWLObjectProperty) 
			thisPattern = Pattern.BOTH_OWLPROPERTY;
		else if (subNodeClassExpression instanceof OWLObjectIntersectionOf && superNodeClassExpression instanceof OWLObjectIntersectionOf) 
			thisPattern = Pattern.BOTH_OWLOBJECTINTERSECTION;		
		return (thisPattern == targetPattern);
	}
	
	private OWLClassExpression intersectionIfSingleton(OWLClassExpression classExpression) {
		if (!(classExpression instanceof OWLObjectIntersectionOf)) {
			 return (OWLObjectIntersectionOf) manager_.getOWLDataFactory().getOWLObjectIntersectionOf(classExpression);  
		}
		else
			return classExpression;
	}
	
	private enum Pattern {
		BOTH_OWLCLASS,
		BOTH_OWLPROPERTY,
		BOTH_OWLOBJECTINTERSECTION,
		BOTH_OWLOBJECTSOMEVALUES,
		NOTHANDLEDPATTERN;
	}
	
		// Probably not needed
		@SuppressWarnings("unused")
		private boolean bothClassExpressionsValidStatementDefinitions(OWLClassExpression subNodeElkClass, OWLClassExpression superNodeElkClass) {
			return true;  //TEMP placeholder
		}
		

		// Probably not needed
		@SuppressWarnings("unused")
		private boolean bothClassExpressionsSameType(OWLClassExpression subNodeElkClass, OWLClassExpression superNodeElkClass) {
			return true;  //TEMP placeholder
		}


}
