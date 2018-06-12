package com.wolox;

import com.wolox.docker.DockerConfiguration;
import com.wolox.steps.Steps;

class ProjectConfiguration {
    DockerConfiguration dockerConfiguration;
    Steps steps;

    def env;
    def error; 

    def environment;
    def services;
    def dockerfile;
    def dockerImage;
    def projectName;
    def buildNumber;
}
