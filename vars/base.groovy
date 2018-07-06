@Library('jenkins-yaml')
import pl.szczad.jenkins_yaml.*;

def call(ProjectConfiguration projectConfig, def _, def nextClosure) {
    return { variables ->
        def timeoutTime = projectConfig.env.TIMEOUT ?: 600 // timeout 10 minutes
        timeout(time: timeoutTime, unit: 'SECONDS') {
            withEnv(projectConfig.environment) {
                wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'XTerm']) {
                    nextClosure(variables)
                }
            }
        }
    }
}
