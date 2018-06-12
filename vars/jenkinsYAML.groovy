//@Library('jenkins-yaml')
import com.wolox.parser.ConfigParser;
import com.wolox.*;

def call(String yamlName, def override=[]) {
    def yaml = override << readYaml file: yamlName;

    def buildNumber = Integer.parseInt(env.BUILD_ID)

    // load project's configuration
    ProjectConfiguration projectConfig = ConfigParser.parse(yaml, env, error);

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
