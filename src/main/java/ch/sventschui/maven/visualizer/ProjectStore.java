package ch.sventschui.maven.visualizer;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.project.MavenProject;

public class ProjectStore {
    private Map<String, Object> store = new HashMap<String, Object>();

    public void addProject(MavenProject p) {
        String key = String.format("%s:%s-%s-%s", p.getGroupId(), p.getArtifact(), p.getVersion(), "");
    }
}
