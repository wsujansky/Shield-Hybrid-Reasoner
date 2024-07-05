package gov.hhs.fda.shield;


import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

public class OntologyAxiomParserAndPrinterVisitor extends OWLObjectVisitorAdapter{

	public OntologyAxiomParserAndPrinterVisitor() {
		// TODO Auto-generated constructor stub
	}
	
	int incrementLevel = 0;
	int indentSize = 2;
	
	private void incrementLevel() {
		this.incrementLevel = this.incrementLevel + indentSize;
	}
	
	private void decrementLevel() {
		this.incrementLevel = this.incrementLevel - indentSize;
	}

	
	
	private void printlnWithIndent(String toPrint, int level) {
		for (int i = 0; i < level; i++) {
			System.out.print(" ");
		}
		System.out.println(toPrint);
	}
	
    @Override
    public void visit(OWLEquivalentClassesAxiom ce) {
    	incrementLevel();  // indent for nested printing
    	printlnWithIndent("AXIOM: " + ce, incrementLevel);
        Set<OWLClassExpression> expressions = ce.getClassExpressions();
        for (Iterator iterator = expressions.iterator(); iterator.hasNext();) {
			OWLClassExpression expr = (OWLClassExpression) iterator.next();
			expr.accept(this);
		}
        decrementLevel();  // un-indent nesting
    }
    
    @Override
    public void visit(OWLSubClassOfAxiom ce) {
    	incrementLevel();  // indent for nested printing
    	printlnWithIndent("AXIOM: " + ce, incrementLevel);
    	OWLClassExpression property = ce.getSubClass();
        property.accept(this);
        OWLClassExpression filler = ce.getSuperClass();
		filler.accept(this);
        decrementLevel();  // un-indent nesting
    }

    
    @Override
    public void visit(OWLClass ce) {
    	incrementLevel();  // indent for nested printing
    	printlnWithIndent("ATOMIC CLASS: " + ce, incrementLevel);
        decrementLevel();  // un-indent nesting
    }
    
    @Override
    public void visit(OWLObjectIntersectionOf ce) {
    	incrementLevel();  // indent for nested printing
//    	printlnWithIndent("CLASS INTERSECTION: " + ce, incrementLevel);
//    	System.out.println("OWL CLASS TYPE: " + ce.getClassExpressionType());
//    	System.out.println(ce.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF);
        Set<OWLClassExpression> expressions = ce.getOperands();
        for (Iterator iterator = expressions.iterator(); iterator.hasNext();) {
			OWLClassExpression expr = (OWLClassExpression) iterator.next();
			expr.accept(this);
		}
        decrementLevel();  // un-indent nesting
    }

    public void visit(OWLObjectSomeValuesFrom ce) {
    	incrementLevel();  // indent for nested printing
    	printlnWithIndent("CLASS SOME VALUES FROM: " + ce, incrementLevel);
//    	System.out.println("OWL CLASS TYPE: " + ce.getClassExpressionType());
//    	System.out.println(ce.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM);
        OWLObjectProperty property = ce.getProperty().getNamedProperty();
        property.accept(this);
        OWLClassExpression filler = ce.getFiller();
		filler.accept(this);
        decrementLevel();  // un-indent nesting
    }

    
    @Override
    public void visit(OWLObjectProperty property) {
    	incrementLevel();  // indent for nested printing
//    	System.out.println("OWL CLASS TYPE: " + property.getEntityType());
//    	System.out.println(property.getEntityType() == EntityType.OBJECT_PROPERTY);
    	printlnWithIndent("CLASS PROPERTY: " + property.getIRI(), incrementLevel);
        decrementLevel();  // un-indent nesting
    }
    
}
