package ch.sventschui.maven.visualizer;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;

public class TargetFileFilter implements IOFileFilter {

    public boolean accept(File file) {
        if("target".equalsIgnoreCase(file.getName())) return false;
        
        return true;
    }

    public boolean accept(File dir, String name) {
        return accept(new File(dir, name));
    }

}
