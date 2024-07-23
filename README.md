The code in this repository implements a customized version of the ELK Reasoner (https://liveontologies.github.io/elk-reasoner/) that, unlike the original ELK Reasoner, can correctly classify DL concepts that are defined as "absent" (such as "No family history of asthma"), as well as DL concepts that are defined using certain temporal relationships (such as "infection occurring within 72 hours after surgery").

Like the Elk Reasoner, the hybrid reasoner implements the OWLReasoner interface and supports all of its methods.  In particular, the hybrid reasoner may be configured as a plug-in reasoner for the Protege ontology-editing environment (as an alternative to the base ELK Reasoner or other reasoners, such as HERMIT).

The implemented hybrid reasoner class "gov.hhs.fda.shield.ElkReasonerSHIELD" is a sub-class of the class "org.semanticweb.elk.owlapi.ElkReasoner" and re-uses most of the code from this class

The code base in this repository consists of an Eclipse "parent" project named shield-hybrid-reasoner-parent and two module sub-projects:

1.  shield-hybrid-reasoner:  Packages the hybrid reasoner as a .jar file that supports all of the interfaces of the original Elk Reasoner (including the OWLReasoner interface), but also correctly classifies concepts defined as "absent" and concepts defined using temporal expressions.
2.  shield-hybrid-reasoner-protege-plugin:  Packages the hybrid reasoner as a protege plugin .jar that may be called from the Protege application via the OSGI component framework.

IMPORTANT NOTE: The hybrid reasoner re-uses almost all of the code from the existing Elk Reasoner and merely overrides several methods of the original ElkReasoner class in order to implement the hybrid reasoning functionility.  *However*, a number of classes in the following Elk Reasoner libraries needed to be slightly modified to support the hybrid reasoning features of the ElkReasonerSHIELD class:

      elk-olwapi-0.4.3
      elk-reasoner-0.4.3

As such, the source code for these libraries has been forked into the following github project:  wsujansky/Shield-Elk-Reasoner-Project-Fork.  This project generates the following customized versions of the libraries above, which are referenced in the Maven POM file of the shield-hybrid-reasoner project:

      elk-olwapi-shield-0.4.3
      elk-reasoner-shield-0.4.3

Note that these custom libraries do not yet exist in the public Maven repository, so they must be built from the source code in the github project referenced above and installed in the local Maven repository before the code in this repository can be built.

Except for these two libraries, however, all ofthe other Elk Reasoner libraries needed by the hybrid reasoner are unchanged and may be accessed from the public Maven repository (as specified in the Maven POM file of the shield-hybrid-reasoner project).

      
    
