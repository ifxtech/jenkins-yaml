package pl.szczad.jenkins_yaml.parser;

class SimpleParser implements BaseParser{
  String[] parseConfigs() {
    return []
  }

  def parse(org.jenkinsci.plugins.workflow.cps.CpsScript script, def object) {

  }
}
