package ru.d10xa.allure;

import org.gradle.api.Action;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;

public class MavenRepositories {

    public final static String D10XA_MAVEN = "https://dl.bintray.com/d10xa/maven";

    public static void addRepository(RepositoryHandler repositories, final String repo) {
        repositories.maven(new Action<MavenArtifactRepository>() {
            @Override
            public void execute(MavenArtifactRepository mavenArtifactRepository) {
                mavenArtifactRepository.setUrl(repo);
            }
        });
    }

}
