package com.krloxz.archidocs.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import com.krloxz.archidocs.Archidoctor;
import com.krloxz.archidocs.MutableArchidoctorConfig;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Encapsulates a documentation section generated by Archidoctor.
 *
 * @author Carlos Gomez
 */
public class GeneratedSection {

  private final Archidoctor archidoctor;
  private final SoftwarePiece softwarePiece;
  private final OutputDirectory outputDirectory;
  private String sectionContent;

  public GeneratedSection(final SoftwarePiece softwarePiece, final OutputDirectory outputDirectory) {
    this.archidoctor = new Archidoctor();
    this.sectionContent = "";
    this.softwarePiece = softwarePiece;
    this.outputDirectory = outputDirectory;
  }

  @When("the {string} section is generated")
  public void generate(final String sectionType) throws IOException {
    MutableArchidoctorConfig config = MutableArchidoctorConfig.create();
    config = this.softwarePiece.enrichConfig(config);
    config = this.outputDirectory.enrichConfig(config);
    this.archidoctor.document(config.toImmutable());
    this.sectionContent = this.outputDirectory.readContent(sectionType + ".adoc")
        .replace("\n", "");
  }

  @Then("the section is titled {string}")
  public void isTitled(final String title) {
    assertThat(this.sectionContent).matches(String.format("== %s.*", title));
  }

  @Then("the section has the subtitle {string}")
  public void hasSubtitle(final String subtitle) {
    assertThat(this.sectionContent).matches(String.format(".*=== %s.*", subtitle));
  }

  @Then("the section has a {string} Components view embedded")
  public void hasComponentsViewEmbedded(final String view) {
    final String defaultViewName = "Components";
    final String viewName = view.isEmpty() ? defaultViewName : view.replace(" ", "");
    assertThat(this.sectionContent)
        .matches(String.format(".+\\Q[plantuml]....@startuml(id=%s)\\E.*\\Q@enduml....\\E.*", viewName));
  }

}
