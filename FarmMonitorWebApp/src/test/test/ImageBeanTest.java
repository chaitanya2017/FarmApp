package test;

import java.util.List;
import monitor.Farm;
import monitor.Image;
import monitor.ImageBean;
import monitor.Monitorpoint;
import org.junit.Test;
import static org.junit.Assert.*;

public class ImageBeanTest {
    
    ImageBean imageBean = new ImageBean();
    public ImageBeanTest() {
    }

    /*
    * test loadFarms function of ImageBean
    */
    @Test
    public void testLoadFarms() {
        List<Farm> farms = imageBean.loadFarms();
        
        assertTrue(farms.size() > 0);
        assertEquals(farms.get(0).getFarmname(), "Test Farm");
    }
    
    /*
    * test getMonitorPoints function of ImageBean
    */
    @Test
    public void testLoadMonitorPoints() {
        imageBean.loadMonitorpoints();
        List<Monitorpoint> monitorPoints = imageBean.getMonitorPoints();
        
        assertTrue(monitorPoints.size() > 0);
        assertEquals(monitorPoints.get(0).getMonitorPointName(), "Test Monitor Point");
    }
    
    /*
    * test getMonitorPointImages function of ImageBean
    */
    @Test
    public void testGetMonitorPointImages() {
        imageBean.setSelectedMonitorPointId("2");
        imageBean.getMonitorPointImages();
        List<Image> images = imageBean.getImages();
        
        assertTrue(images.size() > 0);
        assertFalse(images.get(0).getPath().equals(""));
    }
    
}
