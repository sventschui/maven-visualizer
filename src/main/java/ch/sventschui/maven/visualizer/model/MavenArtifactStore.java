package ch.sventschui.maven.visualizer.model;

import java.util.List;
import java.util.Vector;

public class MavenArtifactStore {
    private List<MavenArtifact> artifacts = new Vector<MavenArtifact>();
    private List<MavenArtifactRelationship> relationships = new Vector<MavenArtifactRelationship>();
    
    public void addArtifact(MavenArtifact a) {
        if(!artifacts.contains(a)) {
            artifacts.add(a);
        }
    }
    
    public void addRelationship(MavenArtifact a, MavenArtifact b) {
        MavenArtifactRelationship r = new MavenArtifactRelationship(a, b);
        
        if(!this.relationships.contains(r)) {
            this.relationships.add(r);
        }
    }
    
    public List<MavenArtifact> getArtifacts() {
        return this.artifacts;
    }
    
    public List<MavenArtifactRelationship> getRelationships() {
        return this.relationships;
    }
    
    public String toJSON() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("nodes: [");
        
        for(MavenArtifact a : this.artifacts) {
            sb.append("" + a.toJSON()+ ",");
        }

        sb.append("],");

        sb.append("links: [");
        
        for(MavenArtifactRelationship r : this.relationships) {
            sb.append("" + r.toJSON(this)+ ",");
        }

        sb.append("]");
        sb.append("}");
        
        return sb.toString().replaceAll("\"", "\\\"");
    }
}
