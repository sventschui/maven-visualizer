package ch.sventschui.maven.visualizer.filefilter;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;

public class PomFileFilter implements IOFileFilter {

    public boolean accept(File pathname, String filename) {
        return accept(new File(pathname, filename));
    }

    public boolean accept(File pathname) {
        if (pathname.isFile() && "pom.xml".equals(pathname.getName())) return true;

        return false;
    }

}
