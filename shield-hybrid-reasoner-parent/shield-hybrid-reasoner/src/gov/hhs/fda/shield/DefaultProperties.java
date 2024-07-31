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
	
/** SWEC Test Ontologies **/
	public static final String STATEMENT_CONCEPT_NAMESPACE = "http://www.hhs.fda.org/shield/SWEC-Ontology#";
	public static final String STATEMENT_CONCEPT_NAME = "Statement-Concept";
	public static final String ABSENCE_PROPERTY_NAMESPACE = "http://www.hhs.fda.org/shield/SWEC-Ontology#";
	public static final String ABSENCE_PROPERTY_NAME = "Situation-Presence";
	public static final String ABSENCE_VALUE_NAMESPACE = "http://www.hhs.fda.org/shield/SWEC-Ontology#";
	public static final String ABSENCE_VALUE_NAME = "Absent";
	public static final String ROLE_GROUP_PROPERTY_NAMESPACE = "http://www.hhs.fda.org/shield/SWEC-Ontology#";
	public static final String ROLE_GROUP_PROPERTY_NAME = "Role-Group";
	public static final String TEMPORAL_ANNOTATION_OWL_IRI = "http://www.w3.org/2000/01/rdf-schema#isDefinedBy";
/**   **/
	
	
/**  SNOMED OWL ONTOLOGY 
	public static final String STATEMENT_CONCEPT_NAMESPACE = "http://snomed.info/id/243796009"; // Situation with explicit context
	public static final String STATEMENT_CONCEPT_NAME = "";
	public static final String ABSENCE_PROPERTY_NAMESPACE = "http://snomed.info/id/408729009"; // Finding context
	public static final String ABSENCE_PROPERTY_NAME = "";
	public static final String ABSENCE_VALUE_NAMESPACE = "http://snomed.info/id/410516002"; // Known absent
	public static final String ABSENCE_VALUE_NAME = "";
	public static final String ROLE_GROUP_PROPERTY_NAMESPACE = "http://snomed.info/id/609096000"; // Role group
	public static final String ROLE_GROUP_PROPERTY_NAME = "";
	public static final String TEMPORAL_ANNOTATION_OWL_IRI = "http://www.w3.org/2000/01/rdf-schema#isDefinedBy";
   **/
	
/** Misc Testing Values
//	public static final String TEMPORAL_ANNOTATION_OWL_IRI = "http://www.w3.org/2000/01/rdf-schema#comment";
// 	public static final String ABSENCE_VALUE = "NEGATIVE";

 */
	
}
