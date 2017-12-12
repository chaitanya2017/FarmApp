/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Database {
    
    
    private Connection connection = null;   
        
    
    public Database()
    {
        
    }
    
    /**
     * Opens a MySQL database connection
     */
    public void openConnection()
    {
        try
        {
            String dbDriverURL = "com.mysql.jdbc.Driver";
            Class.forName(dbDriverURL);
            connection = DriverManager.getConnection(Startup.dbUrl, Startup.userName, Startup.password);
        }
        catch(Exception ex)
        {
            java.util.logging.Logger.getLogger(Database.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
      
    /**
     * Closes database connection
     */
    public void closeConnection()
    {
        try
        {
            if(connection != null)
                connection.close();
        }
        catch(Exception ex)
        {
            java.util.logging.Logger.getLogger(Database.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /**
     * adds a farm to farm table.
     */
    public boolean addFarm(int farmid, String farmname, String addressline1, String addressline2, String landmark, int perimeterid, int visitorscount) {
        boolean added = false;
        try {
            PreparedStatement st  = connection.prepareStatement("insert into farm(farmid, farmname, addressline1, addressline2, landmark, perimeterid, visitorscount) values(?, ?, ?, ?, ?, ?, ?)");
            st.setInt(1, farmid);
            st.setString(2, farmname);
            st.setString(3, addressline1);
            st.setString(4, addressline2);
            st.setString(5, landmark);
            st.setInt(6, perimeterid);
            st.setInt(7, visitorscount);

            int result = st.executeUpdate();
            if (result == 1) {
                added = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return added;
    }
    
    /**
     * adds field details to field table.
     */
    public boolean addField(int fieldid, String fieldname, int farmid, int perimeterid) {
        boolean added = false;
        try {
            PreparedStatement st  = connection.prepareStatement("insert into field(fieldid, fieldname, farmid, perimeterid) values(?, ?, ?, ?)");
            st.setInt(1, fieldid);
            st.setString(2, fieldname);
            st.setInt(3, farmid);
            st.setInt(4, perimeterid);

            int result = st.executeUpdate();
            if (result == 1) {
                added = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return added;
    }
    
    /**
     * adds farmmonitor details to farmmonitor table.
     */
    public boolean addFarmMonitor(int farmid, int monitorpointid) {
        boolean added = false;
        try {
            PreparedStatement st  = connection.prepareStatement("insert into farmmonitor(farmid, monitorpointid) values(?, ?)");
            st.setInt(1, farmid);
            st.setInt(2, monitorpointid);
            
            int result = st.executeUpdate();
            if (result == 1) {
                added = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return added;
    }
    
    /**
     * adds monitor point details to monitorpoint table.
     */
    public boolean addMonitorPoint(int monitorpointid, String monitorpointname, int pointid, 
            String referenceimagepath, String imagespath, String monitorpointlocation, String sreferenceimagepath) {
        boolean added = false;
        try {
            PreparedStatement st  = connection.prepareStatement
                ("insert into monitorpoint(monitorpointid, monitorpointname, pointid, referenceimagepath, imagespath, monitorpointlocation, sreferenceimagepath)"
                        + " values(?, ?, ?, ?, ?, ?, ?)");
            st.setInt(1, monitorpointid);
            st.setString(2, monitorpointname);
            st.setInt(3, pointid);
            st.setString(4, referenceimagepath);
            st.setString(5, imagespath);
            st.setString(6, monitorpointlocation);
            st.setString(7, sreferenceimagepath);

            int result = st.executeUpdate();
            if (result == 1) {
                added = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return added;
    }
    
    /**
     * adds perimeter details to perimeter table.
     */
    public boolean addPerimeter(int perimeterid, String perimetername) {
        boolean added = false;
        try {
            PreparedStatement st  = connection.prepareStatement
                ("insert into perimeter(perimeterid, perimetername) values(?, ?)");
            st.setInt(1, perimeterid);
            st.setString(2, perimetername);
            
            int result = st.executeUpdate();
            if (result == 1) {
                added = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return added;
    }
    
    /**
     * adds perimeter point details to perimeterpoint table.
     */
    public boolean addPerimeterPoint(int perimeterid, int pointid) {
        boolean added = false;
        try {
            PreparedStatement st  = connection.prepareStatement
                ("insert into perimeterpoint(perimeterid, pointid) values(?, ?)");
            st.setInt(1, perimeterid);
            st.setInt(2, pointid);
            
            int result = st.executeUpdate();
            if (result == 1) {
                added = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return added;
    }
    
    /**
     * adds  point details to point table.
     */
    public boolean addPoint(int pointid, float latitude, float longitude) {
        boolean added = false;
        try {
            PreparedStatement st  = connection.prepareStatement
                ("insert into point(pointid, latitude, longitude) values(?, ?, ?)");
            st.setInt(1, pointid);
            st.setFloat(2, latitude);
            st.setFloat(3, longitude);
            
            int result = st.executeUpdate();
            if (result == 1) {
                added = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return added;
    }
    
    /**
     * adds events data to events table.
     */
    public boolean addEvent(int eventid, Date eventoccureddate, Date eventoccuredtime, String imagepath, String simagepath, int monitorpointid) {
        boolean added = false;
        try {
            PreparedStatement st  = connection.prepareStatement
                ("insert into events(eventid, eventoccureddate, eventoccuredtime, imagepath, simagepath, monitorpointid) values(?, ?, ?, ?, ?, ?)");
            st.setInt(1, eventid);
            st.setDate(2, new java.sql.Date(eventoccureddate.getTime()));
            st.setDate(3, new java.sql.Date(eventoccuredtime.getTime()));
            st.setString(4, imagepath);
            st.setString(5, simagepath);
            st.setInt(6, monitorpointid);
            
            int result = st.executeUpdate();
            if (result == 1) {
                added = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return added;
    }
}
