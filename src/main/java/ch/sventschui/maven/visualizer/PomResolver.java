package ch.sventschui.maven.visualizer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sventschui.maven.visualizer.neu.model.VisualizerArtifact;

public class PomResolver {
    private static final Logger logger = LoggerFactory.getLogger(PomResolver.class);

    private List<VisualizerArtifact> parents = new ArrayList<VisualizerArtifact>();

    private List<VisualizerArtifact> all = new ArrayList<VisualizerArtifact>();

    /*
     * private Map<URI, MavenProject> parentStore = new HashMap<URI, MavenProject>();
     * 
     * private Map<URI, MavenProject> moduleStore = new HashMap<URI, MavenProject>();
     */

    public MavenProject loadProjectFromFile(File pom) throws PomResolverException {

        Model model = null;

        FileReader reader = null;

        MavenXpp3Reader mavenreader = new MavenXpp3Reader();

        try {
            reader = new FileReader(pom);
            model = mavenreader.read(reader);
            model.setPomFile(pom);
        } catch (IOException e) {
            logger.error("Failed to read pom " + pom.getAbsolutePath(), e);

            throw new PomResolverException(e);
        } catch (XmlPullParserException e) {
            logger.error("Failed to parse pom " + pom.getAbsolutePath(), e);

            throw new PomResolverException(e);
        }

        MavenProject p = new MavenProject(model);

        System.out.println("Getting parent for: " + p);

        // if (p.getProjectBuildingRequest() == null) p.setProjectBuildingRequest(new DefaultProjectBuildingRequest());

        if (p.getModel().getParent() != null) {
            System.out.println("parent: " + p.getModel().getParent());
        }

        return p;
    }
}
