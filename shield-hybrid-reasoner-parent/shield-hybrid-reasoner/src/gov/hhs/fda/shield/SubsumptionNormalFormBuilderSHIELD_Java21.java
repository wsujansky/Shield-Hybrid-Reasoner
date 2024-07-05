package gov.hhs.fda.shield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SubsumptionNormalFormBuilderSHIELD_Java21 {
/*****
	private static final Logger LOG = LoggerFactory.getLogger(SubsumptionNormalFormBuilderSHIELD_Java21.class);

	private OWLOntology statementOntology;
	private OWLOntology kernelOntology;
	private OWLReasoner statementOwlReasoner;
	private OWLReasoner kernelOwlReasoner;

	private CustomSubsumptionTesterSHIELD subsumptionTester;

	private List<OWLClass> concepts = new ArrayList<>();
	private HashMap<OWLObjectProperty, OWLObjectProperty> chained = new HashMap<>();
	private HashSet<OWLObjectProperty> transitive = new HashSet<>();
	private HashMap<OWLObjectProperty, Set<OWLObjectProperty>> superProps = new HashMap<>();
	private HashMap<OWLClass, List<SubsumptionNormalFormSHIELD>> unmergedSubsumptionNormalFormsSHIELD = new HashMap<>();
	private HashMap<OWLClass, SubsumptionNormalFormSHIELD> subsumptionNormalFormsSHIELD = new HashMap<>();

	private int scoe_cnt = 0;
	private int scoe_chained_cnt = 0;

	public List<OWLClass> getConcepts() {
		return concepts;
	}

	public List<SubsumptionNormalFormSHIELD> getUnmergedSubsumptionNormalFormsSHIELD(OWLClass con) {
		return unmergedSubsumptionNormalFormsSHIELD.get(con);
	}

	public HashMap<OWLClass, SubsumptionNormalFormSHIELD> getSubsumptionNormalFormsSHIELD() {
		return subsumptionNormalFormsSHIELD;
	}
	
	public SubsumptionNormalFormSHIELD getSNF (OWLClass con) {
		return subsumptionNormalFormsSHIELD.get(con);
	}

	public SubsumptionNormalFormBuilderSHIELD_Java21(OWLOntology kernelOntology,
												 OWLOntology statementOntology,
												 OWLReasoner kernelOwlReasoner,
												 OWLReasoner statementOwlReasoner) {
		super();
		this.kernelOntology = kernelOntology;
		this.statementOntology = statementOntology;
		this.kernelOwlReasoner = kernelOwlReasoner;
		this.statementOwlReasoner = statementOwlReasoner;
		this.subsumptionTester  = new CustomSubsumptionTesterSHIELD(kernelOntology);
	}
	
// This method is for debugging only
	public void testSubsumptionsPairwise() {
		        for (Map.Entry<OWLClass, SubsumptionNormalFormSHIELD> entry : subsumptionNormalFormsSHIELD.entrySet()) {
		            OWLClass key = entry.getKey();
		            SubsumptionNormalFormSHIELD value = entry.getValue();
// DEBUG		            System.out.println("SNF Key=" + key + ", \nSNF Value=\n" + value);
		        }
//		CustomSubsumptionTesterSHIELD subsumptionTester = new CustomSubsumptionTesterSHIELD();
		for (OWLClass superConcept : statementOntology.getClassesInSignature()) {
			for (OWLClass subConcept : statementOntology.getClassesInSignature()) {
				if (StatementClassifierSHIELD.logicallyDefinedInOntology(superConcept, statementOntology) &&
					StatementClassifierSHIELD.logicallyDefinedInOntology(subConcept, statementOntology)) {

//  "if" statement select a particular concept or pair of concepts to check subsumption with respect to.	
//  If the statement is omitted, this will test subsumption with respect to all pairs of SNFs
					if (superConcept.getIRI().toString()
  .contains("#ABSENT_History-of-Allergic-Asthma-in-Patient-in-Past") &&
	subConcept.getIRI().toString()
  .contains("#ABSENT_Respiratory-Disease-in-Patient-in-Past")	)  	{

						SubsumptionNormalFormSHIELD superSNF = subsumptionNormalFormsSHIELD.get(superConcept);
						SubsumptionNormalFormSHIELD subSNF = subsumptionNormalFormsSHIELD.get(subConcept);

// DEBUG						System.out.println("Super Class: " + superConcept);
// DEBUG						System.out.println(superSNF.toString());
// DEBUG				System.out.println("Sub Class: " + subConcept);
// DEBUG						System.out.println(subSNF.toString());
						boolean isSnfSubsumedBy = subsumptionTester.isSubsumedBy(subSNF, superSNF, kernelOwlReasoner, statementOwlReasoner);
// DEBUG						System.out.println("     Is Subsumed: " + isSnfSubsumedBy);	
// DEBUG						System.out.println();
	}
				}
			}
		}
	}


	public void init() {
		initConcepts();
		initRoles();
		for (OWLAxiom ax : statementOntology.getAxioms()) {
//			if( ax.getAxiomType().getName().equals("Declaration")) {
//				System.out.println("Axiom type name is Declaration!");
//			}
			switch (ax.getAxiomType().getName()) {
			case "Declaration" : {
			}
			case "SubClassOf" : {
			}
			case "EquivalentClasses" : {
			}
			case "SubObjectPropertyOf" : {
			}
			case "SubPropertyChainOf" : {
			}
			case "TransitiveObjectProperty" : {
			}
			case "ReflexiveObjectProperty" : {
			}
//  TODO: Not recognizing "Declaration" for some reason and throwing this exception...
			default : { } //throw new UnsupportedOperationException("Unexpected Axiom Type: " + ax + " **" + ax.getAxiomType().getName() + "**");
						  // TODO:  Figure out why this case statement is not recognizing "Declaration" Axioms...
			}
		}
	}

	

	private void initConcepts() {
// DEBUG		System.out.println("STATEMENT REASONER TAXONOMY - IN SNF BUILDER");
// DEBUG		ReasonerExplorer.printCurrentReasonerTaxonomy((StructuralReasoner) statementOwlReasoner, false);
		for (OWLClass concept : statementOntology.getClassesInSignature()) {
			String id = concept.getIRI().toString();
			NodeSet<OWLClass> supers = statementOwlReasoner.getSuperClasses(concept, false);
			for (Iterator iterator = supers.iterator(); iterator.hasNext();) {
				OWLClassNode owlClassNode = (OWLClassNode) iterator.next();
			}
			concepts.add(concept);
		}
// LOG		LOG.info("Concepts: " + concepts.size());
	}

	private void initRoles() {
		for (OWLSubPropertyChainOfAxiom ax : kernelOntology.getAxioms(AxiomType.SUB_PROPERTY_CHAIN_OF)) {
// LOG			LOG.info("Chain: " + ax);
			if (ax.getPropertyChain().size() != 2) {
				throw new UnsupportedOperationException("Unexpected: " + ax);
			}
			OWLObjectProperty prop1 = ax.getPropertyChain().get(0).asOWLObjectProperty();
			OWLObjectProperty prop2 = ax.getPropertyChain().get(1).asOWLObjectProperty();
			OWLObjectProperty sup = ax.getSuperProperty().asOWLObjectProperty();
			if (!prop1.equals(sup)) {
				throw new UnsupportedOperationException("Unexpected: " + ax);
			}
			if (chained.get(prop1) != null) {
				throw new UnsupportedOperationException(
						"Two chains for: " + prop1 + " " + ax + " " + chained.get(prop1));
			}
			chained.put(prop1, prop2);
		}
		for (Entry<OWLObjectProperty, OWLObjectProperty> es : chained.entrySet()) {
// LOG			LOG.info("Chained: " + es.getKey() + " " + es.getValue());
		}
		for (OWLTransitiveObjectPropertyAxiom ax : kernelOntology
				.getAxioms(AxiomType.TRANSITIVE_OBJECT_PROPERTY)) {
			OWLObjectProperty prop = ax.getProperty().asOWLObjectProperty();
			transitive.add(prop);
		}
		for (OWLObjectProperty prop : transitive) {
// LOG			LOG.info("Transitive: " + prop);
		}
		for (Iterator iterator = concepts.iterator(); iterator.hasNext();) {
			OWLClass owlClass = (OWLClass) iterator.next();
		}
		Set<OWLObjectProperty> allObjectProperties = kernelOntology.getObjectPropertiesInSignature();
		for (Iterator iterator = allObjectProperties.iterator(); iterator.hasNext();) {
			OWLObjectProperty prop = (OWLObjectProperty) iterator.next();
//		for (OWLObjectProperty prop : kernelOntology.getObjectPropertiesInSignature()) {
			superProps.put(prop, new HashSet<>());
			superProps.get(prop).add(prop);
			// TODO Why does this this cause incompleteness warnings
			// TODO:  Elk does not support getSuperObjectProperties method!  So, commented out.
/******  superProps of every property will be empty set; later figure out how to handle them, given above
			Set<OWLObjectPropertyExpression> propsSuperProps = kernelReasoner.getSuperObjectProperties(prop, false).getFlattened();
			for (Iterator iterator2 = propsSuperProps.iterator(); iterator2.hasNext();) {
				OWLObjectPropertyExpression superProp = (OWLObjectPropertyExpression) iterator2
						.next();
				if (!superProp.isOWLTopObjectProperty()) {
					superProps.get(prop).add(superProp.asOWLObjectProperty());
				}
			}
*******/
	
/**************
		}
		for (Entry<OWLObjectProperty, Set<OWLObjectProperty>> es : superProps.entrySet()) {
			if (es.getValue().size() > 1) {
// LOG				LOG.info("Super props: " + es.getKey());
				for (OWLObjectProperty prop : es.getValue()) {
					if (!prop.equals(es.getKey())) {
// LOG						LOG.info("\t" + prop);
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void generate() {   // was public void generate(Roles roles)
// ??		Reasoner.processingSubsumptionNormalFormSHIELD = true;
		int cnt = 0;
		for (OWLClass concept : concepts) {
			if (++cnt % 50000 == 0) {
// LOG				LOG.info("Generate: " + cnt);
			}
			for (OWLClassAxiom axiom : getLogicalAxioms(concept, statementOntology)) {
				unmergedSubsumptionNormalFormsSHIELD.put(concept, new ArrayList<>());
// LOG				LOG.info(concept + " " + axiom);
				SubsumptionNormalFormSHIELD expr = createExpression(concept, axiom);
				unmergedSubsumptionNormalFormsSHIELD.get(concept).add(expr);
// DEBUG				System.out.println("Initial SNF for " + concept + " is :");
// DEBUG				System.out.println(" " + expr.toString());
				simplify(expr.getUngroupedProps());
			}
			if (!getLogicalAxioms(concept, statementOntology).isEmpty()) {
				subsumptionNormalFormsSHIELD.put(concept, mergeNNFs(unmergedSubsumptionNormalFormsSHIELD.get(concept)));
// DEBUG				System.out.println("Simplified/Merged SNF for " + concept + " is :");
// DEBUG				System.out.println(" " + subsumptionNormalFormSHIELD.get(concept));
			}
		}
// LOG		LOG.info("Generate: " + cnt);
// ??	Reasoner.processingSubsumptionNormalFormSHIELD = false;
// LOG		LOG.info("SCOE: " + scoe_cnt);
	}

	private SubsumptionNormalFormSHIELD mergeNNFs(List<SubsumptionNormalFormSHIELD> nnfs) {
		SubsumptionNormalFormSHIELD merged_nnf = new SubsumptionNormalFormSHIELD();
		// TODO subClassOf
		merged_nnf.setUngroupedProps(new HashSet<>());
		merged_nnf.setFocusConcepts(new HashSet<>());
		for (SubsumptionNormalFormSHIELD nnf : nnfs) {
			merged_nnf.getUngroupedProps().addAll(nnf.getUngroupedProps());
			merged_nnf.getFocusConcepts().addAll(nnf.getFocusConcepts());
		}
		simplify(merged_nnf.getUngroupedProps());
		simplifyFocusConcepts(merged_nnf.getFocusConcepts());
		if (isAbsentConcept(merged_nnf) )
			merged_nnf.setIsAbsent(true);
		else
			merged_nnf.setIsAbsent(false);			
		return merged_nnf;
	}
	
	private OWLClassExpression getDefinition(OWLClass concept, OWLClassAxiom axiom) {
// DEBUG	  System.out.println("The Axiom: " + axiom);
// DEBUG	  System.out.println("The Concept: " + concept);
		switch (axiom) {
			case OWLEquivalentClassesAxiom x -> {
				Set<OWLClassExpression> class_exprs = x.getClassExpressions();
				if (class_exprs.size() != 2)
					throw new UnsupportedOperationException("Unexpected: " + class_exprs.size() + " " + class_exprs);
				Iterator<OWLClassExpression> iterator = class_exprs.iterator();
				OWLClassExpression clazz = iterator.next();
				OWLClassExpression def = iterator.next();
				return def;
			}
			case OWLSubClassOfAxiom x -> {
				OWLClassExpression def = x.getSuperClass();
				return def;
			}
			default -> throw new UnsupportedOperationException("Unexpected: " + axiom.getAxiomType());
		}
	}

	private SubsumptionNormalFormSHIELD createExpression(OWLClass concept, OWLClassAxiom axiom) {
		SubsumptionNormalFormSHIELD expr = new SubsumptionNormalFormSHIELD();
		{
			switch (axiom) {
				case OWLEquivalentClassesAxiom x -> expr.setSubClassOf(false);
				case OWLSubClassOfAxiom x -> expr.setSubClassOf(true);
				default -> throw new UnsupportedOperationException("Unexpected: " + axiom.getAxiomType());
			}
		}
		Set<OWLClass> focusConcepts = processDefinitionFocusConcepts(concept, axiom);
		expr.setFocusConcepts(focusConcepts);
		Set<OWLObjectSomeValuesFrom> roles = processDefinitionProperties(concept, axiom);;
		expr.setUngroupedProps(roles);
		return expr;
	}

	private Set<OWLObjectSomeValuesFrom> processDefinitionProperties(OWLClass concept, OWLClassAxiom axiom) {
		Set<OWLObjectSomeValuesFrom> properties = new HashSet<>();
		OWLClassExpression classDefinition = getDefinition(concept, axiom);
		switch (axiom) {  // Just a safety check
			case OWLSubClassOfAxiom x -> { }
			case OWLEquivalentClassesAxiom x -> { }
			default -> throw new UnsupportedOperationException("Unexpected Axiom Type: " + axiom.getAxiomType());
		}
		switch (classDefinition) {
			case OWLClass y -> {
				for (OWLClassAxiom superAxiom : statementOntology.getAxioms(y)) {
					properties.addAll(processDefinitionProperties(getDefinedClass(superAxiom), superAxiom));
				}
			}
			case OWLObjectSomeValuesFrom y -> { 
				properties.add(y);	
			}  
			case OWLObjectIntersectionOf y -> {
				properties.addAll(processIntersectionProperties(y.getOperands(), axiom));
			}
			default -> throw new UnsupportedOperationException(
					"Unexpected: " + classDefinition + " " + classDefinition.getClassExpressionType());
		}
		return properties;
	}

	private Set<OWLObjectSomeValuesFrom> processIntersectionProperties(Set<OWLClassExpression> class_exprs, OWLClassAxiom axiom) {
		Set<OWLObjectSomeValuesFrom> properties = new HashSet<>();
		int counter = 0;
		for (Iterator iterator = class_exprs.iterator(); iterator.hasNext();) {
			OWLClassExpression class_expr = (OWLClassExpression) iterator.next();
			switch (class_expr) {
				case OWLClass z -> {
					for (OWLClassAxiom superAxiom : statementOntology.getAxioms(z)) {
						properties.addAll(processDefinitionProperties(getDefinedClass(superAxiom), superAxiom));
					}
				}
				case OWLObjectSomeValuesFrom z -> {
					properties.add(z);
					}
				default -> throw new UnsupportedOperationException(
					"Unexpected: " + class_expr + " " + class_expr.getClassExpressionType());
				//  TODO:  This method will not handle intersections within intersections; needed?
				//         Would need to recurse to do that
			}
		}
		return properties;
	}
	
	private Set<OWLClass> processDefinitionFocusConcepts(OWLClass concept, OWLClassAxiom axiom) {
		Set<OWLClass> focusConcepts = new HashSet<>();
		OWLClassExpression classDefinition = getDefinition(concept, axiom);
// DEBUG		if (concept.getIRI().toString().contains("#ABSENT_History-of-Allergic-Asthma-in-Patient-Recently_COPY"))
// DEBUG			System.out.print("");
		{
			switch (axiom) {
				case OWLSubClassOfAxiom x -> {
					focusConcepts.add(concept);  // And don't walk further up the stated hierarchy
				}
				case OWLEquivalentClassesAxiom x -> {
					switch (classDefinition) {
						case OWLClass y -> {
							for (OWLClassAxiom superAxiom : statementOntology.getAxioms(y)) {
								focusConcepts.addAll(processDefinitionFocusConcepts(getDefinedClass(superAxiom), superAxiom));
								return focusConcepts;
							}
						}
						case OWLObjectSomeValuesFrom y -> { 
							// ignore this type of expression; ; return null Set					
						}  
						case OWLObjectIntersectionOf y -> {
							focusConcepts = processIntersectionFocusConcepts(y.getOperands(), axiom);
							return focusConcepts;
						}
						default -> throw new UnsupportedOperationException(
								"Unexpected: " + classDefinition + " " + classDefinition.getClassExpressionType());
					}
				}
				default -> throw new UnsupportedOperationException("Unexpected: " + axiom.getAxiomType());
			}
		}
		return focusConcepts;
	}
	
	private Set<OWLClass> processIntersectionFocusConcepts(Set<OWLClassExpression> class_exprs, OWLClassAxiom axiom) {
		Set<OWLClass> focusConcepts = new HashSet<>();
		for (OWLClassExpression class_expr : class_exprs) {
			switch (axiom) {
				case OWLSubClassOfAxiom x -> {
					switch (class_expr) {
						case OWLClass z -> {
							focusConcepts.add(z); // Don't walk further up the stated hierarchy
							return focusConcepts;
						}
						case OWLObjectSomeValuesFrom z -> {
							// Ignore; add no focus concepts
						}
						default -> throw new UnsupportedOperationException(
								"Unexpected: " + class_expr + " " + class_expr.getClassExpressionType());
							//  TODO:  This method will not handle intersections within intersections; would need to recurse
					}
				}
				case OWLEquivalentClassesAxiom x -> {
					switch (class_expr) {
						case OWLClass z -> {
							for (OWLClassAxiom superAxiom : statementOntology.getAxioms(z)) {
								focusConcepts.addAll(processDefinitionFocusConcepts(getDefinedClass(superAxiom), superAxiom));
								return focusConcepts;
							}
						}
						case OWLObjectSomeValuesFrom z -> {
							// Ignore; add no focus concepts
						}
						default -> throw new UnsupportedOperationException(
								"Unexpected: " + class_expr + " " + class_expr.getClassExpressionType());
							//  TODO:  This method will not handle intersections within intersections; would need to recurse
					}
				}
				default -> throw new UnsupportedOperationException("Unexpected: " + axiom.getAxiomType());
			}
		}
		return focusConcepts;
	}

	private OWLClass getDefinedClass (OWLClassAxiom axiom) {
		switch (axiom) {
		case OWLEquivalentClassesAxiom x -> {
			Set<OWLClassExpression> class_exprs = x.getClassExpressions();
			if (class_exprs.size() != 2)
				throw new UnsupportedOperationException("Unexpected: " + class_exprs.size() + " " + class_exprs);
			OWLClassExpression definedClass = class_exprs.iterator().next();
			return (OWLClass) definedClass;
		}
		case OWLSubClassOfAxiom x -> {
			OWLClassExpression definedClass = x.getSubClass();
			return (OWLClass) definedClass;
		}
		default -> throw new UnsupportedOperationException("Unexpected: " + axiom.getAxiomType());
		}
	}
		
// Only needed to handle subsumption w.r.t. chained properties for purposes of creating the SNF
// TODO:  CustomSubsumptionTesterSHIELD does not support subsumption w.r.t. chained properties.  Should we add?
	private boolean isSubClassOfEntailed(OWLObjectSomeValuesFrom prop1, OWLObjectSomeValuesFrom prop2) {
		boolean isSubClassOf = false;
		if (superProps.get(prop1.getProperty().asOWLObjectProperty())
				.contains(prop2.getProperty().asOWLObjectProperty())) {
			scoe_cnt++;
			if (chained.get(prop1.getProperty()) == null && chained.get(prop2.getProperty()) == null
					&& !transitive.contains(prop1.getProperty()) && !transitive.contains(prop2.getProperty())) {
					OWLClass con1 = prop1.getFiller().asOWLClass();
					OWLClass con2 = prop2.getFiller().asOWLClass();
					isSubClassOf = con1.equals(con2) || subsumptionTester.isSubsumedBy(con1, con2, kernelOwlReasoner);
			} else {
				scoe_chained_cnt++;
			}
		}
		return isSubClassOf;
	}

// This subclass is only needed to handle chained properties.  Currently not handled by classification process
// TODO:  Determine if we need to add support for subsumption w.r.t. chained properties to CustomSubsumptionTesterSHIELD
	private static class SVF {
		private OWLObjectProperty prop;
		private OWLClassExpression filler;

		public SVF(OWLObjectProperty prop, OWLClassExpression filler) {
			super();
			this.prop = prop;
			this.filler = filler;
		}

		@Override
		public int hashCode() {
			return Objects.hash(filler, prop);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SVF other = (SVF) obj;
			return Objects.equals(filler, other.filler) && Objects.equals(prop, other.prop);
		}

		@Override
		public String toString() {
			return "SVF(" + prop + " " + filler + ")";
		}

	}

	private HashSet<SVF> expandChain(OWLObjectProperty prop, OWLClass filler) {
		HashSet<SVF> svfs = new HashSet<>();
		svfs.add(new SVF(prop, filler));
		while (true) {
			ArrayList<SVF> svfs_l = new ArrayList<>(svfs);
			for (SVF svf : svfs_l) {
				List<SVF> chain_exps = expandChain1(svf);
				svfs.addAll(chain_exps);
				List<SVF> prop_exps = expandSuperProps(svf);
				svfs.addAll(prop_exps);
			}
			if (svfs.size() == svfs_l.size())
				break;
		}
		return svfs;
	}

	private List<SVF> expandChain1(SVF svf) {
		ArrayList<OWLObjectProperty> chained_props = new ArrayList<>();
		if (chained.get(svf.prop) != null) {
			chained_props.add(chained.get(svf.prop));
		}
		if (transitive.contains(svf.prop))
			chained_props.add(svf.prop);
		List<OWLClass> chained_cons = subsumptionNormalFormsSHIELD.get(svf.filler).getUngroupedProps().stream()
				.filter(x -> chained_props.contains(x.getProperty().asOWLObjectProperty()))
				.map(x -> x.getFiller().asOWLClass()).distinct().toList();
		return chained_cons.stream().map(x -> new SVF(svf.prop, x)).toList();
	}

	private List<SVF> expandSuperProps(SVF svf) {
		return superProps.get(svf.prop).stream().filter(x -> !x.equals(svf.prop)).map(x -> new SVF(x, svf.filler))
				.toList();
	}

	private boolean isSubsumedBy(SVF svf1, SVF svf2, OWLReasoner owlReasoner) {
		return subsumptionTester.isSubsumedBy(svf1.prop, svf2.prop, owlReasoner) && isSubsumedBy(svf1.filler, svf2.filler, owlReasoner);
	}

	private boolean isSubsumedBy(OWLClassExpression expr1, OWLClassExpression expr2, OWLReasoner owlReasoner) {
		return subsumptionTester.isSubsumedBy(expr1, expr2, owlReasoner);
	}

	private void simplifyFocusConcepts(Set<OWLClass> cons) {
		if (cons.isEmpty())
			return;
		ArrayList<OWLClass> to_remove = new ArrayList<>();
		for (OWLClass con1 : cons) {
			for (OWLClass con2 : cons) {
				if (con1 == con2)
					continue;
				if (subsumptionTester.isSubsumedBy(con1, con2, statementOwlReasoner)) {
					to_remove.add(con2);
				}
			}
		}
		cons.removeAll(to_remove);
	}
	
	private void simplify(Set<OWLObjectSomeValuesFrom> props) {
		if (props.isEmpty())
			return;
		ArrayList<OWLObjectSomeValuesFrom> to_remove = new ArrayList<>();
		for (OWLObjectSomeValuesFrom prop1 : props) {
			for (OWLObjectSomeValuesFrom prop2 : props) {
// DEBUG	System.out.println("Prop1: " + prop1);
// DEBUG	System.out.println("Prop2: " + prop2);
				if (prop1 == prop2)
					continue;
				if (subsumptionTester.isSubsumedBy(prop1, prop2, kernelOwlReasoner))
					to_remove.add(prop2);
			}
		}
		props.removeAll(to_remove);
	}

	private boolean isAbsentConcept(SubsumptionNormalFormSHIELD merged_nnf) {
		OWLObjectSomeValuesFrom absentProperty = createAbsentProperty();
		if (merged_nnf.getUngroupedProps().contains(absentProperty))
			return true;
		else return false;
	}
	
	private OWLObjectSomeValuesFrom createAbsentProperty() {
		OWLDataFactory factory = statementOntology.getOWLOntologyManager().getOWLDataFactory();
		String iriBase = "http://www.hhs.fda.org/shield/SWEC-Ontology";
		IRI absentValueIri = IRI.create(iriBase + "#Absent");
		OWLClass absentValueClass = factory.getOWLClass(absentValueIri);
		IRI situationPresencePropertyIri = IRI.create(iriBase + "#Situation-Presence");
		OWLProperty situationPresenceProperty = factory.getOWLObjectProperty(situationPresencePropertyIri);
		OWLObjectSomeValuesFrom situationAbsentRole = factory.getOWLObjectSomeValuesFrom((OWLObjectPropertyExpression) situationPresenceProperty, (OWLClassExpression) absentValueClass);
		return situationAbsentRole;
	}
	
	public Set<OWLClassAxiom> getLogicalAxioms(OWLClass concept, OWLOntology statementOntology) {
		Set<OWLClassAxiom> logicalAxioms = new HashSet();
		for (OWLClassAxiom axiom : statementOntology.getAxioms(concept)) {
			if (axiom.isLogicalAxiom()) {
				logicalAxioms.add(axiom);
			}
		}
		return logicalAxioms;
	}
*********/
} 
