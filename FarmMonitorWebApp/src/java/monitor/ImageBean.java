package monitor;

import hibernate.BaseHibernateDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.*;

/*
* This class is used for getting requests from UI and updating UI with monitor point images
*/
@ManagedBean(name = "imageBean")
@ViewScoped
public class ImageBean extends BaseHibernateDAO implements Serializable {

    //Fields
    private List<Image> images = null;
    private List<Farm> listFarms;
    private List<Monitorpoint> listMonitorPoints;
    private String selectedMonitorPointId;
    private String selectedFarmName;
    private boolean visible = false;

    public ImageBean() {
        
    }

    //Property accessors
    public List<Farm> getListFarms() {
        return listFarms;
    }

    public List<Monitorpoint> getMonitorPoints() {
        return listMonitorPoints;
    }

    public String getSelectedFarmName() {
        return selectedFarmName;
    }

    public void setSelectedFarmName(String selectedFarmName) {
        this.selectedFarmName = selectedFarmName;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getVisible() {
        return visible;
    }

    @PostConstruct
    public void init() {
        listFarms = loadFarms();
        loadMonitorpoints();
    }

    public String getSelectedMonitorPointId() {
        return selectedMonitorPointId;
    }

    public void setSelectedMonitorPointId(String selectedMonitorPointId) {
        this.selectedMonitorPointId = selectedMonitorPointId;
    }

    /*
    * gets all farm data from database
    */
    public List<Farm> loadFarms() {
        List<Farm> list1 = new ArrayList<>();
        Transaction trns = null;
        Farm point = null;
        Session session = getSession();
        try {
            trns = session.beginTransaction();
            String sql = "SELECT farmid, farmname FROM farm";
            SQLQuery query = session.createSQLQuery(sql).addScalar("farmid", new IntegerType()).addScalar("farmname", new StringType());
            List<Object[]> rows = query.list();
            for (Object[] row : rows) {
                point = new Farm();
                point.setFarmid(Integer.parseInt(row[0].toString()));
                point.setFarmname(row[1].toString());
                list1.add(point);
            }
            selectedFarmName = selectedMonitorPointId = null;
            listMonitorPoints = null;
            trns.commit();
        } catch (HibernateException | NumberFormatException e) {
            e.printStackTrace();
            trns.rollback();

        } finally {
            session.flush();
            session.close();
        }
        return list1;
    }

    /*
    * gets all monitor points from database
    */
    public List<Monitorpoint> loadMonitorpoints() {
        Transaction trn = null;
        Session session = getSession();
        listMonitorPoints = new ArrayList<>();
        try {
            trn = session.beginTransaction();

            String sql = "select monitorpoint.monitorpointid, monitorpoint.monitorpointname from monitorpoint";

            SQLQuery query = session.createSQLQuery(sql).addScalar("monitorpointname", new StringType()).addScalar("monitorpointid", new IntegerType());
            List<Object[]> rows = query.list();
            for (Object[] row : rows) {
                Monitorpoint mp = new Monitorpoint();
                mp.setMonitorPointName(row[0].toString());
                mp.setMonitorPointid(Integer.parseInt(row[1].toString()));
                listMonitorPoints.add(mp);
            }
            trn.commit();

            selectedMonitorPointId = null;

        } catch (Exception e) {
            e.printStackTrace();
            trn.rollback();
        } finally {
            session.close();
        }
        return listMonitorPoints;
    }

    /*
    * gets all monitor points images from database
    */
    public void getMonitorPointImages() {
        Transaction trns = null;
        Session session = getSession();

        try {
            trns = session.beginTransaction();
            images = new ArrayList<>();

            String sql = "SELECT eventoccureddate, eventoccuredtime, simagepath FROM EVENTS \n"
                    + "INNER JOIN monitorpoint ON monitorpoint.monitorpointid = events.monitorpointid \n"
                    + "WHERE monitorpoint.monitorpointid = " + selectedMonitorPointId;

            setVisible(true);
            SQLQuery query = session.createSQLQuery(sql)
                    .addScalar("eventoccureddate", new DateType())
                    .addScalar("eventoccuredtime", new TimeType())
                    .addScalar("simagepath", new StringType());
            List<Object[]> events = query.list();

            for (Object[] event : events) {
                String date = event[0].toString();
                String time = event[1].toString();
                String imagepath = event[2].toString();
                Image image = new Image(imagepath, date + " " + time);
                images.add(image);
            }
            trns.commit();

        } catch (Exception e) {
            e.printStackTrace();
            trns.rollback();
        } finally {
            session.close();
        }
    }

    /*
    * returns a list of all images
    */
    public List<Image> getImages() {
        return images;
    }

    /*
    * returns a list of all images
    */
    public Integer getSize() {
        if(images != null)
            return images.size();
        else
            return 0;
    }
}
