package ch.sventschui.maven.visualizer.model.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.project.MavenProject;

/**
 * Filter to determine whether a MavenArtifact should be included in the result.
 * 
 * @author Sven Tschui
 * 
 */
public class MavenArtifactFilter {
	/**
	 * List of RegEx to match artifact against to check if it should be included
	 * in the result. Default everything is included.
	 */
    private List<String> includes = Arrays.asList(new String[] {".*"});

	/**
	 * List of RegEx to match artifact against to check if it should be excluded
	 * in the result. Default nothing is excluded.
	 */
	private List<String> excludes = new ArrayList<String>();

	/**
	 * Check whether the MavenArtifact artifact matches to the plugin filter
	 * configuration
	 * 
	 * @param artifact
	 *            MavenArtifact to check
	 * @return true if the MavenArtifact should be included in the result
	 */
    public boolean matches(MavenProject artifact) {
		String name = artifact.getGroupId() + ":" + artifact.getArtifactId();

		// first check excludes since they're heavier weightned
		for (String exclude : this.excludes) {
			if (name.matches(exclude)) {
				return false;
			}
		}

		// if the artifact is not explicitly excluded check wether it should be
		// included
		for (String include : this.includes) {
			if (name.matches(include)) {
				return true;
			}
		}

		// if it is not explicitly included or excluded it will be
		return false;
	}
}
