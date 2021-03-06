//@Library('jenkins-yaml')
import pl.szczad.jenkins_yaml.parser.Parser;
import pl.szczad.jenkins_yaml.*;

def call(String yamlName, def override=[]) {
    def yaml = readYaml file: yamlName;
    yaml = override << yaml

    def buildNumber = Integer.parseInt(env.BUILD_ID)

    // load project's configuration
    ProjectConfiguration projectConfig = Parser.parse(yaml);

    def imageName = projectConfig.dockerConfiguration.imageName().toLowerCase();

    // build the image specified in the configuration
    def customImage = (projectConfiguration.dockerImage) ?: docker.build(imageName, "--file ${projectConfig.dockerfile} .");

    // adds the last step of the build.
    def closure = buildSteps(projectConfig, customImage);

    // each service is a closure that when called it executes its logic and then calls a closure, the next step.
    projectConfig.services.each {
        closure = "${it.service.getVar()}"(projectConfig, it.version, closure);
    }

    // we execute the top level closure so that the cascade starts.
    try {
        closure([:]);
    } finally{
        deleteDockerImages(projectConfig);
    }
}
