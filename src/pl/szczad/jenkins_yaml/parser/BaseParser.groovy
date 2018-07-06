package pl.szczad.jenkins_yaml.parser

interface BaseParser {
  String[] parseConfigs()
  def parse(org.jenkinsci.plugins.workflow.cps.CpsScript script, def object)
}
