package ch.sventschui.maven.visualizer.model;

public class MavenArtifact {

    private String groupId;

    private String artifactId;

    private String version;

    public MavenArtifact() {
    }

    public MavenArtifact(String groupId, String artifactId, String version) {
        super();
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

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;

        MavenArtifact a = (MavenArtifact) obj;

        if (!compareAttribute(this.groupId, a.groupId)) return false;

        if (!compareAttribute(this.artifactId, a.artifactId)) return false;

        if (!compareAttribute(this.version, a.version)) return false;

        return true;
    }

    private boolean compareAttribute(String a, String b) {
        if(a == null && b == null)
            return true;
        
        if(a != null && a.equals(b))
            return true;
        
        return false;
    }
    
    public String toJSON() {
        return "{groupId: \"" + this.getGroupId() + "\", artifactId: \"" + this.getArtifactId() + "\", version: \"" + this.getVersion() + "\"}";
    }
}
