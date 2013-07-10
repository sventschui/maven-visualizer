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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import ch.sventschui.maven.visualizer.model.filter.MavenArtifactFilter;
import ch.sventschui.maven.visualizer.neu.model.VisualizerArtifact;

/**
 * Goal which creates the visualization
 * 
 * @goal visualize
 * 
 * @phase process-sources
 */
public class VisualizerMojo extends AbstractMojo {
    private static Logger logger = LoggerFactory.getLogger(VisualizerMojo.class);

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

    // private MavenArtifactStore store = new MavenArtifactStore();

    // private Map<String, MavenProject> projects = new HashMap<String, MavenProject>();

    private Map<MavenProject, File> projects = new HashMap<MavenProject, File>();

    private Map<String, VisualizerArtifact> artifacts = new HashMap<String, VisualizerArtifact>();

    private Map<String, VisualizerArtifact> parents = new HashMap<String, VisualizerArtifact>();

    public void execute() throws MojoExecutionException {

        this.createOutputDir();

        for (String directory : this.directories) {
            this.poms.addAll(FileUtils.listFiles(new File(directory), new PomFileFilter(), new TargetFileFilter()));
        }

        for (File pom : this.poms) {

            MavenProject project = projectForPom(pom);
            System.out.println(pom);

            if (this.filter.matches(project)) {
                projects.put(project, pom);
            }

            /*
             * MavenArtifact base = new MavenArtifact(project.getGroupId(), project.getArtifactId(),
             * project.getVersion());
             * 
             * if (this.filter.matches(base)) {
             * 
             * this.store.addArtifact(base);
             * 
             * for (Dependency artifact : project.getDependencies()) { MavenArtifact dependency = new MavenArtifact(
             * artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
             * 
             * if (this.filter.matches(dependency)) { this.store.addArtifact(dependency);
             * this.store.addRelationship(base, dependency); } } }
             */
        }

        // Convert to VisualizerArtifacts
        for (MavenProject p : projects.keySet()) {
            String key = p.getGroupId() + ":" + p.getArtifactId();
            VisualizerArtifact artifact = new VisualizerArtifact(p.getGroupId(), p.getArtifactId(), p.getVersion());

            artifacts.put(key, artifact);
            parents.put(key, artifact);
        }

        // Remove modules from parents
        for (MavenProject p : projects.keySet()) {

            for (String mName : p.getModules()) {

                File pom = new File(projects.get(p).getParent(), mName + "/pom.xml");

                if (!pom.isFile()) {
                    System.out.println("Pom does not exist:" + pom.getAbsolutePath());
                    continue;
                }

                MavenProject m = projectForPom(pom);

                String key = m.getGroupId() + ":" + m.getArtifactId();

                parents.remove(key);

                VisualizerArtifact module = artifacts.get(key);

                if (module == null) {
                    System.out.println("Can not get module for:" + mName);
                }

                artifacts.get(p.getGroupId() + ":" + p.getArtifactId()).getModules().add(module);

            }

        }

        // Calc dependencies
        for (MavenProject p : projects.keySet()) {

            VisualizerArtifact a = artifacts.get(p.getGroupId() + ":" + p.getArtifactId());

            for (Dependency d : p.getDependencies()) {

                String key = d.getGroupId() + ":" + d.getArtifactId();

                VisualizerArtifact dep = artifacts.get(key);

                if (dep != null) {
                    a.getDepedencies().add(dep);
                }

            }

        }

        System.out.println("digraph a {");

        System.out.println("rankdir=LR;");
        System.out.println("ranksep=2;");
        System.out.println("labeljust=l");

        String[] c = {"red", "green", "blue", "cyan", "magenta", "pink", "orange"};

        int i = 0;
        int j = 0;
        for (VisualizerArtifact a : parents.values()) {

            if (i >= c.length) {
                i = 0;
            }

            String color = c[i];

            System.out.println("subgraph cluster_" + j + " { ");

            System.out.println("label=\"" + a.getArtifactId() + "\"");

            System.out.println("color=\"" + color + "\"");

            for (VisualizerArtifact a2 : a.getModules()) {
                System.out.println("\"" + a2.getArtifactId() + "\" [color=" + color + "];");
            }

            System.out.println("}");

            i++;
            j++;
        }

        printArtifacts(parents.values(), "");

        System.out.println("}");

        /*
         * try { VisualizationWriter.writeVisualization(this.store, new File( this.outputDir, "visualization.html")); }
         * catch (IOException e) { logger.error("Failed to write visualization", e); }
         */
    }

    private MavenProject projectForPom(File pom) {

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

        return new MavenProject(model);
    }

    private void printArtifacts(Collection<VisualizerArtifact> artifacts, String indent) {

        for (VisualizerArtifact a : artifacts) {

            if (a == null) {
                System.out.println(indent + "a was null!");

                continue;
            }

            // System.out.println(indent + a.getGroupId() + ":" + a.getArtifactId() + ":" + a.getVersion());

            for (VisualizerArtifact d : a.getDepedencies()) {

                System.out.println("\"" + a.getArtifactId() + "\" -> \"" + d.getArtifactId() + "\";");

                // System.out.println(indent + "- depends on: " + d.getGroupId() + ":" + d.getArtifactId() + ":"
                // + d.getVersion());

            }

            if (a.getModules().size() > 0) {
                // System.out.println(indent + "- modules:");

                printArtifacts(a.getModules(), indent + "\t");
            }

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
