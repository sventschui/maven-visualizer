package ch.sventschui.maven.visualizer;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sventschui.maven.visualizer.model.MavenArtifact;
import ch.sventschui.maven.visualizer.model.MavenArtifactStore;

/**
 * Goal which creates the visualization
 * 
 * @goal visualize
 * 
 * @phase process-sources
 */
public class VisualizerMojo extends AbstractMojo {
	private static Logger logger = LoggerFactory
			.getLogger(VisualizerMojo.class);

	/**
	 * Project build directory (e.g. /target folder).
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File projectBuildDir;

	/**
	 * visualizer folder within the project build directory
	 */
	private File outputDir = null;

	/**
	 * List of pom files to process
	 */
	private List<File> poms = new Vector<File>();

	/**
	 * Place to store maven artifacts and relationships
	 */
	private MavenArtifactStore store = new MavenArtifactStore();

	/**
	 * directories to search for pom files
	 * 
	 * @parameter
	 * @required
	 */
	private String[] directories;

	/**
	 * @parameter
	 * @required
	 */
	private String[] includes;

	public void execute() throws MojoExecutionException {

		this.createOutputDir();

		for (String directory : this.directories) {
			this.poms.addAll(FileUtils.listFiles(new File(directory),
					new PomFileFilter(), new TargetFileFilter()));
		}

		for (File pom : this.poms) {

			Model model = null;

			FileReader reader = null;

			MavenXpp3Reader mavenreader = new MavenXpp3Reader();

			try {
				reader = new FileReader(pom);
				model = mavenreader.read(reader);
				model.setPomFile(pom);
			} catch (IOException e) {
				logger.error("Failed to read pom " + pom.getAbsolutePath(), e);
			} catch (XmlPullParserException e) {
				logger.error("Failed to parse pom " + pom.getAbsolutePath(), e);
			}

			MavenProject project = new MavenProject(model);

			if (this.matchesFilter(project.getGroupId())) {

				MavenArtifact base = new MavenArtifact(project.getGroupId(),
						project.getArtifactId(), project.getVersion());

				this.store.addArtifact(base);

				for (Dependency artifact : project.getDependencies()) {
					if (this.matchesFilter(artifact.getGroupId())) {
						MavenArtifact dependency = new MavenArtifact(
								artifact.getGroupId(),
								artifact.getArtifactId(), artifact.getVersion());

						this.store.addArtifact(dependency);
						this.store.addRelationship(base, dependency);
					}
				}
			}
		}

		try {
			FileWriter fstream = new FileWriter(new File(this.outputDir,
					"test.json"));
			BufferedWriter out = new BufferedWriter(fstream);

			out.write(this.store.toJSON());

			out.close();
		} catch (IOException e) {
			logger.error("Failed to write result", e);
		}

	}

	private void createOutputDir() {
		this.outputDir = new File(this.projectBuildDir, "visualizer");

		// clean
		if (this.outputDir.exists()) {
			this.outputDir.delete();
		}

		// create
		this.outputDir.mkdirs();
	}

	public boolean matchesFilter(String groupId) {
		for (String include : this.includes) {
			if (groupId.startsWith(include)) {
				return true;
			}
		}

		return false;
	}
}
