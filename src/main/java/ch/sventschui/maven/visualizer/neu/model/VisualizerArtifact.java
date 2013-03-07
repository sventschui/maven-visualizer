package ch.sventschui.maven.visualizer.neu.model;

import java.util.ArrayList;
import java.util.List;

public class VisualizerArtifact {
    private String groupId;

    private String artifactId;

    private String version;

    private List<VisualizerArtifact> depedencies = new ArrayList<VisualizerArtifact>();

    private List<VisualizerArtifact> modules = new ArrayList<VisualizerArtifact>();

    public VisualizerArtifact(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<VisualizerArtifact> getDepedencies() {
        return depedencies;
    }

    public void setDepedencies(List<VisualizerArtifact> depedencies) {
        this.depedencies = depedencies;
    }

    public List<VisualizerArtifact> getModules() {
        return modules;
    }

    public void setModules(List<VisualizerArtifact> modules) {
        this.modules = modules;
    }

}
