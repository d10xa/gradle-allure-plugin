package ru.d10xa.allure;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

public class AllurePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        Task allureReport =  project.getTasks().create("allureReport");
        allureReport.getActions().add(new Action<Task>() {
            @Override
            public void execute(Task task) {
                System.out.println("allure!");
            }
        });

    }

}
