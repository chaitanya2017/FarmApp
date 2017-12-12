package test;

import java.util.List;
import monitor.Farm;
import monitor.FarmBean;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class FarmBeanTest {
    
    FarmBean farmBean = new FarmBean();
    public FarmBeanTest() {
        
    }

    /*
    * test getFarmNames function of FarmBean
    */
    @Test
    public void testGetFarmNames() {
        List<String> listFarms = farmBean.getFarmNames();
        assertTrue(listFarms.size() > 0);
        assertEquals(listFarms.get(0), "Test Farm");
    }
    
    /*
    * test getFarmBoundaries function of FarmBean
    */
    @Test
    public void testGetFarmBoundaries() {
        farmBean.setSelectedFarmName("Test Farm");
        List<Farm> farmPoints = farmBean.getFarmBoundaries();
        
        assertTrue(farmPoints.size() > 0);
        assertTrue(farmPoints.get(0).getLatitude() != 0);
        assertTrue(farmPoints.get(0).getLongitude() != 0);
    }
    
    /*
    * test getFieldBoundaries function of FarmBean
    */
    @Test
    public void testGetFieldBoundaries() {
        farmBean.setSelectedFarmName("Test Farm");
        List<List<Farm>> farmPoints = farmBean.getFieldBoundaries();
        
        assertTrue(farmPoints.size() > 0);
        assertTrue(farmPoints.get(0).get(0).getLatitude() != 0);
        assertTrue(farmPoints.get(0).get(0).getLongitude() != 0);
    }
}
