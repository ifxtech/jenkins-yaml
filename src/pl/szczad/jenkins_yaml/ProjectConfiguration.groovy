package pl.szczad.jenkins_yaml;

import pl.szczad.jenkins_yaml.docker.DockerConfiguration;
import pl.szczad.jenkins_yaml.steps.Steps;

class ProjectConfiguration {
  private def script

  Steps steps;


  def environment;
  def services;
  def dockerfile;
  def dockerImage;
  def projectName;
  def buildNumber;

  ProjectConfiguration(def script) {
    this.@script = script
  }
}
