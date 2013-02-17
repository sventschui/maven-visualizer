package ch.sventschui.maven.visualizer.writer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sventschui.maven.visualizer.model.MavenArtifactStore;

public class VisualizationWriter {
    private static Logger logger = LoggerFactory.getLogger(VisualizationWriter.class);

    private final static String TEMPLATE_NAME = "visualization.tpl";

    public static void writeVisualization(MavenArtifactStore store, File outFile) throws IOException {
        BufferedReader tpl = null;
        BufferedWriter out = null;

        try {
            InputStream tplStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(TEMPLATE_NAME);
            
            if(tplStream == null) {
                throw new FileNotFoundException("Template " + TEMPLATE_NAME + " not found");
            }
            
            tpl = new BufferedReader(new InputStreamReader(tplStream));

            out = new BufferedWriter(new FileWriter(outFile));

            String line = null;

            String json = store.toJSON();

            while (null != (line = tpl.readLine())) {
                line = line.replace("#jsondata#", json);

                out.write(line + "\n");
            }
        } finally {
            System.out.println("finally");
            if (tpl != null) {
                try {
                    tpl.close();
                } catch (IOException e) {
                    logger.debug("Could not close tpl file", e);
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.debug("Could not close output file", e);
                }
            }
        }
    }
}
