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

import java.io.File;
import java.io.FileReader;
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

import ch.sventschui.maven.visualizer.filefilter.PomFileFilter;
import ch.sventschui.maven.visualizer.filefilter.TargetFileFilter;
import ch.sventschui.maven.visualizer.model.MavenArtifact;
import ch.sventschui.maven.visualizer.model.MavenArtifactStore;
import ch.sventschui.maven.visualizer.model.filter.MavenArtifactFilter;
import ch.sventschui.maven.visualizer.writer.VisualizationWriter;

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
	private MavenArtifactFilter filter = new MavenArtifactFilter();

	private File outputDir = null;

	private List<File> poms = new Vector<File>();

	private MavenArtifactStore store = new MavenArtifactStore();

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

			MavenArtifact base = new MavenArtifact(project.getGroupId(),
					project.getArtifactId(), project.getVersion());

			if (this.filter.matches(base)) {

				this.store.addArtifact(base);

				for (Dependency artifact : project.getDependencies()) {
					MavenArtifact dependency = new MavenArtifact(
							artifact.getGroupId(), artifact.getArtifactId(),
							artifact.getVersion());

					if (this.filter.matches(dependency)) {
						this.store.addArtifact(dependency);
						this.store.addRelationship(base, dependency);
					}
				}
			}
		}

		try {
			VisualizationWriter.writeVisualization(this.store, new File(
					this.outputDir, "visualization.html"));
		} catch (IOException e) {
			logger.error("Failed to write visualization", e);
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
}
