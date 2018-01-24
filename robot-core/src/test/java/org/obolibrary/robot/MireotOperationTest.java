package org.obolibrary.robot;

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Tests MIREOT extraction.
 *
 * <p>This is a very minimal test with a dumb example
 *
 * @author cjm
 */
public class MireotOperationTest extends CoreTest {

  //the path to a known-good file for comparison
  private String expectedPath = null;

  /**
   * Test MIREOT.
   **
   * @throws IOException on IO problems
   * @throws OWLOntologyCreationException on ontology problems
   */
  @Test
  public void testMireotNoImports() throws IOException, OWLOntologyCreationException {

    expectedPath = "/mireot.owl";

    OWLOntology inputOntology = loadOntology("/filtered.owl");

    IRI outputIRI = IRI.create("http://purl.obolibrary.org/obo/uberon.owl");

    Set<IRI> upperIRIs =
        Collections.singleton(IRI.create("http://purl.obolibrary.org/obo/UBERON_0001235"));
    Set<IRI> lowerIRIs = upperIRIs;
    // Set<IRI> branchIRIs = upperIRIs;

    List<OWLOntology> outputOntologies = new ArrayList<OWLOntology>();

    outputOntologies.add(MireotOperation.getAncestors(inputOntology, upperIRIs, lowerIRIs, null));

    /*
    outputOntologies.add(
            MireotOperation.getDescendants(inputOntology,
                    branchIRIs, null));
    */

    OWLOntology outputOntology = MergeOperation.merge(outputOntologies);

    OntologyHelper.setOntologyIRI(outputOntology, outputIRI, null);

    OWLOntology expected = loadOntology(expectedPath);
    removeDeclarations(expected);
    removeDeclarations(outputOntology);
    assertIdentical(expected, outputOntology);
  }


  @Test
  public void testMireotImports() throws IOException, OWLOntologyCreationException {

    //test passes with ro-mireot-output.owl that doesn't include all the annotations for RO_0001000 as they come from the imported ontologies
    //expectedPath = "/ro-mireot-output.owl";

    //test fails with ro-mireot.owl that is the correct generated module with the MIREOT method AFAIK
    expectedPath = "/ro-mireot.owl";

    OWLOntology inputOntology = loadOntology("/ro-filtered.owl");

    IRI outputIRI = IRI.create("http://purl.obolibrary.org/obo/ro.owl");

    Set<IRI> upperIRIs =
      Collections.singleton(IRI.create("http://purl.obolibrary.org/obo/RO_0001000"));
    Set<IRI> lowerIRIs = upperIRIs;

    List<OWLOntology> outputOntologies = new ArrayList<OWLOntology>();

    outputOntologies.add(MireotOperation.getAncestors(inputOntology, upperIRIs, lowerIRIs, null));

    OWLOntology outputOntology = MergeOperation.merge(outputOntologies);

    OntologyHelper.setOntologyIRI(outputOntology, outputIRI, null);

    OWLOntology expected = loadOntology(expectedPath);
    removeDeclarations(expected);
    removeDeclarations(outputOntology);
    assertIdentical(expected, outputOntology);
  }





}
