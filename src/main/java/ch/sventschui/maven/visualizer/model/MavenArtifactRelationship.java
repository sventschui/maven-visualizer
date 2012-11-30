package ch.sventschui.maven.visualizer.model;

public class MavenArtifactRelationship {
    private MavenArtifact base;

    private MavenArtifact dependency;
    
    public MavenArtifactRelationship(MavenArtifact base, MavenArtifact dependency) {
        super();
        this.base = base;
        this.dependency = dependency;
    }

    public MavenArtifact getBase() {
        return base;
    }

    public MavenArtifact getDependency() {
        return dependency;
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if(obj == null)
            return false;
        
        MavenArtifactRelationship other = (MavenArtifactRelationship) obj;

        if(compare(this.getBase(), other.getBase()) && compare(this.getDependency(), other.getDependency()))
            return true;
            
        return false;
    }
    
    private boolean compare(MavenArtifact a1, MavenArtifact a2) {
        if((a1 == null && a2 == null) || (a1 != null && a1.equals(a2))) 
            return true;
        
        return false;
    }
    
    public String toJSON(MavenArtifactStore store) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("{");
        
        sb.append("source:");
        
        sb.append(Integer.toString(store.getArtifacts().indexOf(this.base)));
        
        sb.append(",target:");
        
        sb.append(Integer.toString(store.getArtifacts().indexOf(this.dependency)));
        
        sb.append("}");
        
        return sb.toString();
    }
    
}
