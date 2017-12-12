package monitor;

import hibernate.BaseHibernateDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/*
* This class is used for getting requests from UI and updating UI with farms and their boundaries
*/
@ManagedBean(name = "farmBean")
@ViewScoped
public class FarmBean extends BaseHibernateDAO implements Serializable {

    //Fields
    private List<Farm> farmPoints;
    private List<List<Farm>> fieldsList;
    private Float mapLatitude;
    private Float mapLongitude;
    private String selectedFarmName;
    private String displayFarmName;
    private Double area;
    private Double perimeter;

    public FarmBean() {

    }

    //Property accessors
    public String getSelectedFarmName() {
        return selectedFarmName;
    }

    public void setSelectedFarmName(String selectedFarmName) {
        this.selectedFarmName = selectedFarmName;
    }

    public Double getPerimeter() {
        return  perimeter;
    }
    
    public Double getArea() {
        return area;
    }

    public void setMapLatitude(Float mapLatitude) {
        this.mapLatitude = mapLatitude;
    }

    public Float getMapLatitude() {
        return this.mapLatitude;
    }

    public void setMapLongitude(Float mapLongitude) {
        this.mapLongitude = mapLongitude;
    }

    public Float getMapLongitude() {
        return this.mapLongitude;
    }

    public String getDisplayFarmName() {
        return displayFarmName;
    }

    public void setDisplayFarmName(String displayFarmName) {
        this.displayFarmName = displayFarmName;
    }
    
    public String getFarmName() {
        return this.selectedFarmName;
    }
    
    public void setFarmName(String selectedFarmName) {
        this.selectedFarmName = selectedFarmName;
    }
    
    public List<Farm> getFarmPoints() {
        return farmPoints;
    }

    /*
    * gets all farm names from the database to display on combo box on UI
    */
    public List<String> getFarmNames() {

        List<String> list = new ArrayList<>();
        Session session = getSession();
        try {
            String sql;
            sql = "SELECT farm.farmname FROM farm";

            SQLQuery query = session.createSQLQuery(sql).addScalar("farmname", new StringType());
            List<Object> rows = query.list();
            for (Object row : rows) {
                list.add(row.toString());
            }

        } catch (HibernateException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return list;
    }

    /*
    * gets a list of all farm boundaries from database
    */
    public List<Farm> getFarmBoundaries() {
        farmPoints = new ArrayList<>();
        farmPoints.clear();
        Session session = getSession();
        try {
            ArrayList<Double> lat = new ArrayList<>();
            ArrayList<Double> lng = new ArrayList<>();
            String sql = "SELECT point.latitude, point.longitude, farm.farmname FROM POINT \n"
                    + "JOIN perimeterpoint ON perimeterpoint.pointid = point.pointid  \n"
                    + "JOIN farm ON farm.perimeterid = perimeterpoint.perimeterid \n"
                    + "WHERE farm.farmname = '" + selectedFarmName + "'";

            SQLQuery query = session.createSQLQuery(sql).addScalar("latitude", new FloatType()).addScalar("longitude", new FloatType()).addScalar("farmname", new StringType());
            List<Object[]> rows = query.list();
            for (Object[] row : rows) {
                Farm point = new Farm();
                point.setLatitude(Float.parseFloat(row[0].toString()));
                point.setLongitude(Float.parseFloat(row[1].toString()));
                point.setFarmname(row[2].toString());
                farmPoints.add(point);
                lat.add(Double.parseDouble(row[0].toString()));
                lng.add(Double.parseDouble(row[1].toString()));
            }
            area(lat, lng); //this method is fetching area of polygon
            perimeter(lat, lng);

            if (farmPoints.isEmpty()) {
                displayFarmName = "Nothing to show here, Please select any layout";
            } else {
                displayFarmName = farmPoints.get(0).getFarmname();
                mapLatitude = farmPoints.get(0).getLatitude();
                mapLongitude = farmPoints.get(0).getLongitude();
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return farmPoints;
    }
    

    /*
    * gets a list of all field boundaries from database
    */
    public List<List<Farm>> getFieldBoundaries() {
        fieldsList = new ArrayList<>();
        fieldsList.clear();
        Session session = getSession();
        try {
            String sql = "SELECT field.fieldid FROM FIELD JOIN farm ON field.farmid = farm.farmid WHERE farm.farmname = '" + selectedFarmName + "'";
            SQLQuery query = session.createSQLQuery(sql).addScalar("fieldid", new IntegerType());
            List<Object> rowFields = query.list();
            for (Object rowField : rowFields) {
                Integer fieldid = Integer.parseInt(rowField.toString());

                List<Farm> eachfarm = new ArrayList<>();
                sql = "SELECT latitude, longitude FROM POINT \n"
                        + "JOIN perimeterpoint ON point.pointid = perimeterpoint.pointid \n"
                        + "JOIN FIELD ON perimeterpoint.perimeterid = field.perimeterid \n"
                        + "WHERE FIELD.fieldid = " + fieldid;

                query = session.createSQLQuery(sql).addScalar("latitude", new FloatType()).addScalar("longitude", new FloatType());
                List<Object[]> rows = query.list();
                for (Object[] row : rows) {
                    Farm point = new Farm();
                    point.setLatitude(Float.parseFloat(row[0].toString()));
                    point.setLongitude(Float.parseFloat(row[1].toString()));
                    eachfarm.add(point);
                }
                fieldsList.add(eachfarm);
            }
        } catch (HibernateException | NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return fieldsList;
    }

    /*
    * returns a list of all field points
    */
    public List<List<Farm>> getFieldPoints() {
        return fieldsList;
    }

    /*
    * This method is called from UI, gets farm and field boundaries
    */
    public void getBoundaries() {
        getFarmBoundaries(); //Displaying Layout on Google Map
        getFieldBoundaries();   //Displaying Farm on Google Map
    }

    

   /*
    * calculates area of a farm/field
    * @Param lats - array of latitude points
    * @Param lons - array of longitude points
    */
    public double area(ArrayList<Double> lats, ArrayList<Double> lons) {
        double sum = 0;
        double prevcolat = 0;
        double prevaz = 0;
        double colat0 = 0;
        double az0 = 0;
        for (int i = 0; i < lats.size(); i++) {
            double colat = 2 * Math.atan2(Math.sqrt(Math.pow(Math.sin(lats.get(i) * Math.PI / 180 / 2), 2) + Math.cos(lats.get(i) * Math.PI / 180) * Math.pow(Math.sin(lons.get(i) * Math.PI / 180 / 2), 2)), Math.sqrt(1 - Math.pow(Math.sin(lats.get(i) * Math.PI / 180 / 2), 2) - Math.cos(lats.get(i) * Math.PI / 180) * Math.pow(Math.sin(lons.get(i) * Math.PI / 180 / 2), 2)));
            double az = 0;
            if (lats.get(i) >= 90) {
                az = 0;
            } else if (lats.get(i) <= -90) {
                az = Math.PI;
            } else {
                az = Math.atan2(Math.cos(lats.get(i) * Math.PI / 180) * Math.sin(lons.get(i) * Math.PI / 180), Math.sin(lats.get(i) * Math.PI / 180)) % (2 * Math.PI);
            }
            if (i == 0) {
                colat0 = colat;
                az0 = az;
            }
            if (i > 0 && i < lats.size()) {
                sum = sum + (1 - Math.cos(prevcolat + (colat - prevcolat) / 2)) * Math.PI * ((Math.abs(az - prevaz) / Math.PI) - 2 * Math.ceil(((Math.abs(az - prevaz) / Math.PI) - 1) / 2)) * Math.signum(az - prevaz);
            }
            prevcolat = colat;
            prevaz = az;
        }
        sum = sum + (1 - Math.cos(prevcolat + (colat0 - prevcolat) / 2)) * (az0 - prevaz);
        double area = 5.10072E14 * Math.min(Math.abs(sum) / 4 / Math.PI, 1 - Math.abs(sum) / 4 / Math.PI);
        this.area = (area) / (4046.85642);//converting meter sq to acres
        System.out.println("area in sq met is: " + area + " area in acres: " + this.area);
        return this.area;
    }

    /*
    * calculates perimeter length of a farm/field
    * @Param lats - array of latitude points
    * @Param lons - array of longitude points
    */
    public double perimeter(ArrayList<Double> lats, ArrayList<Double> lons) {
        System.out.println("size of list lat and long for perameter" + lats.size() + " and " + lons.size());
        double dist = 0d;
        double totaldist = 0d;
        if (selectedFarmName != null) {
            try {
                for (int i = 0, j = 0; i < lats.size() - 1; i++, j++) {
                    double theta = lons.get(j) - lons.get(j + 1);
                    dist = Math.sin(deg2rad(lats.get(i))) * Math.sin(deg2rad(lats.get(i + 1))) + Math.cos(deg2rad(lats.get(i))) * Math.cos(deg2rad(lats.get(i + 1))) * Math.cos(deg2rad(theta));
                    dist = Math.acos(dist);
                    dist = rad2deg(dist);
                    dist = dist * 60 * 1.1515;//getting distance in Miles
                    dist = dist * 1.609344;// getting distance in Kilometers
                    totaldist = totaldist + dist;
                }
                double theta = lons.get(lons.size() - 1) - lons.get(0);
                dist = Math.sin(deg2rad(lats.get(lats.size() - 1))) * Math.sin(deg2rad(lats.get(0))) + Math.cos(deg2rad(lats.get(lats.size() - 1))) * Math.cos(deg2rad(lats.get(0))) * Math.cos(deg2rad(theta));
                dist = Math.acos(dist);
                dist = rad2deg(dist);
                dist = dist * 60 * 1.1515;//getting distance in Miles
                dist = dist * 1.609344;// getting distance in Kilometers            
                perimeter = totaldist + dist;
                System.out.println("parameter is : " + perimeter);

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            perimeter = 0.0;
        }
        return totaldist;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
