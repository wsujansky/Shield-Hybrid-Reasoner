package gov.hhs.fda.shield;


/*  Values of key parameters within the OWL ontology that will be classified.  These parameters may be changed depending
 *  on the modeling of absent concepts and temporal properties in the ontology.
 *  The active values are reflected in the following test ontologies
 *  		SWEC-Ontology-Example-9.owx
 *  		SWEC-Ontology-Example-PROPERTY-TESTING-NO-CLASSIFIED-YES-ANNOTATION.owx
 *  The commented-out parameters were used for testing the following alternatively modeled ontologies:
 *  		SWEC-Ontology-Example-9-CHANGED-ONTOLOGY-PROPERTIES.owx
 *  		SWEC-Ontology-Example-PROPERTY-TESTING-NO-CLASSIFIED-YES-ANNOTATION-CHANGED-ONTOLOGY-PROPERTIES.owx
 */			
public class DefaultProperties {
	public static final String STATEMENT_CONCEPT_NAMESPACE = "http://www.hhs.fda.org/shield/SWEC-Ontology";
//	public static final String STATEMENT_CONCEPT_NAMESPACE = "http://www.hhs.fda.org/shield/SWEC-Ontology-NEW";
	public static final String STATEMENT_CONCEPT_NAME = "Statement-Concept";
//	public static final String STATEMENT_CONCEPT_NAME = "Statement-Concept-NEW";
	public static final String TEMPORAL_ANNOTATION_OWL_IRI = "http://www.w3.org/2000/01/rdf-schema#isDefinedBy";
//	public static final String TEMPORAL_ANNOTATION_OWL_IRI = "http://www.w3.org/2000/01/rdf-schema#comment";
	public static final String ABSENCE_NAMESPACE = "http://www.hhs.fda.org/shield/SWEC-Ontology";
//	public static final String ABSENCE_NAMESPACE = "http://www.hhs.fda.org/shield/SWEC-Ontology-NEW";
	public static final String ABSENCE_PROPERTY = "Situation-Presence";
//	public static final String ABSENCE_PROPERTY = "Situation-Presence-NEW";
	public static final String ABSENCE_VALUE = "Absent";
//	public static final String ABSENCE_VALUE = "NEGATIVE";
}
