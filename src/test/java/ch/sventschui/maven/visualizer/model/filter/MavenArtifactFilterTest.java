package ch.sventschui.maven.visualizer.model.filter;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.sventschui.maven.visualizer.model.MavenArtifact;

public class MavenArtifactFilterTest {
    private MavenArtifactFilter filter;
    
    @Before
    public void setUp() throws Exception {
        this.filter = new MavenArtifactFilter();

        List<String> includes = new ArrayList<String>();
        List<String> excludes = new ArrayList<String>();

        includes.add("ch.sventschui.test1:Test1");
        includes.add("ch.sventschui.test2.*");
        
        excludes.add("ch.sventschui.test2:Test2");

        Field includesFiled = filter.getClass().getDeclaredField("includes");
        Field excludesFiled = filter.getClass().getDeclaredField("excludes");
        
        includesFiled.setAccessible(true);
        excludesFiled.setAccessible(true);

        includesFiled.set(filter, includes);
        excludesFiled.set(filter, excludes);
        
    }
    
    @Test
    public void testFilter1_1() {
        MavenArtifact a = new MavenArtifact("ch.sventschui.test1", "Test1", "0.0.1-SNAPSHOT");
        
        assertTrue(this.filter.matches(a));
    }
    
    @Test
    public void testFilter1_2() {
        MavenArtifact a = new MavenArtifact("ch.sventschui.test1", "Test2", "0.0.1-SNAPSHOT");
        
        assertFalse(this.filter.matches(a));
    }
    
    @Test
    public void testFilter2_1() {
        MavenArtifact a = new MavenArtifact("ch.sventschui.test2", "Test1", "0.0.1-SNAPSHOT");
        
        assertTrue(this.filter.matches(a));
    }
    
    @Test
    public void testFilter2_2() {
        MavenArtifact a = new MavenArtifact("ch.sventschui.test2", "Test2", "0.0.1-SNAPSHOT");
        
        assertFalse(this.filter.matches(a));
    }
}
