package pl.szczad.jenkins_yaml.parser;

import pl.szczad.jenkins_yaml.ProjectConfiguration;
import pl.szczad.jenkins_yaml.docker.DockerConfiguration;
import pl.szczad.jenkins_yaml.services.*;
import pl.szczad.jenkins_yaml.steps.*;

class YAMLParser {

    private static String LATEST = 'latest';

    static ProjectConfiguration parse(org.jenkinsci.plugins.workflow.cps.CpsScript script, def yaml) {
        ProjectConfiguration projectConfiguration = new ProjectConfiguration(script);

        projectConfiguration.error          = error;

        projectConfiguration.environment    = parseEnvironment(yaml.environment);
        projectConfiguration.steps          = parseSteps(yaml.steps);
        projectConfiguration.services       = parseServices(yaml.services);
//        projectConfiguration.docker         = parseDocker(yaml.docker);
        projectConfiguration.stacks         = 

        projectConfiguration.dockerImage    = parseDockerImage(yaml.config);
        projectConfiguration.dockerfile     = parseDockerfile(yaml.config);
        projectConfiguration.projectName    = parseProjectName(yaml.config);

        projectConfiguration.dockerConfiguration = new DockerConfiguration(projectConfiguration: projectConfiguration);

        return projectConfiguration;
    }

    static def parseEnvironment(def environment) {
        if (!environment) {
            return "";
        }

        return environment.collect { k, v -> "${k}=${v}"};
    }

    static def parseSteps(def yamlSteps) {
        List<Step> steps = yamlSteps.collect { k, v ->
            Step step = new Step(name: k)

            // a step can have one or more commands to execute
            v.each {
                step.commands.add(it);
            }
            return step
        }
        return new Steps(steps: steps);
    }

    static def parseServices(def steps) {
        def services = [];

        steps.each {
            def service = it.tokenize(':')
            def version = service.size() == 2 ? service.get(1) : LATEST
            def instance = getServiceClass(service.get(0).capitalize())?.newInstance()

            services.add([service: instance, version: version])
        };

        services.add([service: new Base(), version: LATEST]);

        return services
    }

    static def getServiceClass(def name) {
        switch(name) {
            case "Postgres":
                return Postgres
                break
            case "Redis":
                return Redis
                break
            case "Mssql":
                return Mssql
                break
        }
    }

    static def parseImage(def config) {
        if (!config || !config["image"]) {
            return null;
        }

        return config["image"];
    }

    static def parseDockerfile(def config) {
        if (!config || !config["dockerfile"]) {
            return "Dockerfile";
        }

        return config["dockerfile"];
    }

    static def parseProjectName(def config) {
        if (!config || !config["project_name"]) {
            return "jenkins-yaml";
        }

        return config["project_name"];
    }

    // static def parseDocker(def yamlDocker) {
    //     if (!yamlDocker)
    // }
}
