package pl.szczad.jenkins_yaml.parser;

class StackParser implements BaseParser{
  String[] parseConfigs() {
    return ["stack"]
  }

  def parse(org.jenkinsci.plugins.workflow.cps.CpsScript script, def object) {
     
  }
}
