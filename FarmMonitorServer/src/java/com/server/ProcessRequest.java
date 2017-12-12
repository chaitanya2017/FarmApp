/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import javax.imageio.ImageIO;
//import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 * 
 * This class processes JSON file
 */
public class ProcessRequest {

    public String process(String json) {

        String error = "failed to upload ";
        boolean err = false;
        Database db = null;
        try {
            db = new Database();
            db.openConnection();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
            JSONArray array;

            if (jsonObject.containsKey("farm")) {
                array = (JSONArray) jsonObject.get("farm");
                if (!insertFarms(array, db)) {
                    err = true;
                    error += " farm";
                }
            }
            if (jsonObject.containsKey("field")) {
                array = (JSONArray) jsonObject.get("field");
                if (!insertFields(array, db)) {
                    err = true;
                    error += " field";
                }
            }
            if (jsonObject.containsKey("farmmonitor")) {
                array = (JSONArray) jsonObject.get("farmmonitor");
                if (!insertFarmMonitor(array, db)) {
                    err = true;
                    error += " farmmonitor";
                }
            }
            if (jsonObject.containsKey("monitorpoint")) {
                array = (JSONArray) jsonObject.get("monitorpoint");
                if (!insertMonitorPoint(array, db)) {
                    err = true;
                    error += " monitorpoint";
                }
            }
            if (jsonObject.containsKey("perimeter")) {
                array = (JSONArray) jsonObject.get("perimeter");
                if (!insertPerimeter(array, db)) {
                    err = true;
                    error += " perimeter";
                }
            }
            if (jsonObject.containsKey("perimeterpoint")) {
                array = (JSONArray) jsonObject.get("perimeterpoint");
                if (!insertPerimeterPoint(array, db)) {
                    err = true;
                    error += " perimeterpoint";
                }
            }
            if (jsonObject.containsKey("point")) {
                array = (JSONArray) jsonObject.get("point");
                if (!insertPoint(array, db)) {
                    err = true;
                    error += " point";
                }
            }
            if (jsonObject.containsKey("events")) {
                array = (JSONArray) jsonObject.get("events");
                if (!insertEvents(array, db)) {
                    err = true;
                    error += " events";
                }
            }

            if (err) {
                return error;
            } else {
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error + " data";
        } finally {
            if (db != null) {
                db.closeConnection();
            }
        }
    }

    /**
     * parses farms data from JSON and inserts the data to database
     */
    private boolean insertFarms(JSONArray farms, Database db) {
        JSONObject innerObj = null;
        try {
            Iterator iter = farms.iterator();
            while (iter.hasNext()) {
                innerObj = (JSONObject) iter.next();
                db.addFarm(Integer.parseInt(innerObj.get("farmid").toString()),
                        (String) innerObj.get("farmname"),
                        (String) innerObj.get("addressline1"),
                        (String) innerObj.get("addressline2"),
                        (String) innerObj.get("landmark"),
                        Integer.parseInt(innerObj.get("perimeterid").toString()),
                        Integer.parseInt(innerObj.get("visitorscount").toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

     /**
     * parses fields data from JSON and inserts the data to database
     */
    private boolean insertFields(JSONArray fields, Database db) {
        JSONObject innerObj = null;
        try {
            Iterator iter = fields.iterator();
            while (iter.hasNext()) {
                innerObj = (JSONObject) iter.next();
                db.addField(Integer.parseInt(innerObj.get("fieldid").toString()),
                        (String) innerObj.get("fieldname"),
                        Integer.parseInt(innerObj.get("farmid").toString()),
                        Integer.parseInt(innerObj.get("perimeterid").toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

     /**
     * parses farmmonitors data from JSON and inserts the data to database
     */
    private boolean insertFarmMonitor(JSONArray farmMonitor, Database db) {
        JSONObject innerObj = null;
        try {
            Iterator iter = farmMonitor.iterator();
            while (iter.hasNext()) {
                innerObj = (JSONObject) iter.next();
                db.addFarmMonitor(Integer.parseInt(innerObj.get("farmid").toString()),
                        Integer.parseInt(innerObj.get("monitorpointid").toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

     /**
     * parses monitorpoints data from JSON and inserts the data to database
     */
    private boolean insertMonitorPoint(JSONArray monitorPoints, Database db) {
        JSONObject innerObj = null;
        try {
            Iterator iter = monitorPoints.iterator();
            while (iter.hasNext()) {
                innerObj = (JSONObject) iter.next();
                
                String byteimage = (String) innerObj.get("referenceimage").toString();
                String refImagePath = (String) innerObj.get("referenceimagepath");
                String path = saveImage(byteimage, refImagePath);
                db.addMonitorPoint(Integer.parseInt(innerObj.get("monitorpointid").toString()),
                        (String) innerObj.get("monitorpointname"),
                        Integer.parseInt(innerObj.get("pointid").toString()),
                        refImagePath,
                        (String) innerObj.get("imagespath"),
                        (String) innerObj.get("monitorpointlocation"),
                        path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

     /**
     * parses perimeters data from JSON and inserts the data to database
     */
    private boolean insertPerimeter(JSONArray perimeters, Database db) {

        JSONObject innerObj = null;
        try {
            Iterator iter = perimeters.iterator();
            while (iter.hasNext()) {
                innerObj = (JSONObject) iter.next();
                db.addPerimeter(Integer.parseInt(innerObj.get("perimeterid").toString()),
                        (String) innerObj.get("perimetername"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

     /**
     * parses perimeterpoints data from JSON and inserts the data to database
     */
    private boolean insertPerimeterPoint(JSONArray farms, Database db) {

        JSONObject innerObj = null;
        try {
            Iterator iter = farms.iterator();
            while (iter.hasNext()) {
                innerObj = (JSONObject) iter.next();
                db.addPerimeterPoint(Integer.parseInt(innerObj.get("perimeterid").toString()),
                        Integer.parseInt(innerObj.get("pointid").toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

     /**
     * parses points data from JSON and inserts the data to database
     */
    private boolean insertPoint(JSONArray farms, Database db) {

        JSONObject innerObj = null;
        try {
            Iterator iter = farms.iterator();
            while (iter.hasNext()) {
                innerObj = (JSONObject) iter.next();
                db.addPoint(Integer.parseInt(innerObj.get("pointid").toString()),
                        Float.parseFloat(innerObj.get("latitude").toString()),
                        Float.parseFloat(innerObj.get("longitude").toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

     /**
     * parses events data from JSON and inserts the data to database
     */
    private boolean insertEvents(JSONArray farms, Database db) {

        JSONObject innerObj = null;
        try {
            Iterator iter = farms.iterator();
            while (iter.hasNext()) {
                innerObj = (JSONObject) iter.next();

                SimpleDateFormat formatterdate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formattertime = new SimpleDateFormat("HH:mm");

                String byteimage = (String) innerObj.get("eventimage").toString();
                String clientImagePath = (String) innerObj.get("imagepath");
                String serverImagePath = saveImage(byteimage, clientImagePath);

                db.addEvent(Integer.parseInt(innerObj.get("eventid").toString()),
                        formatterdate.parse(innerObj.get("eventoccureddate").toString()),
                        formattertime.parse(innerObj.get("eventoccuredtime").toString()),
                        clientImagePath,
                        serverImagePath,
                        Integer.parseInt(innerObj.get("monitorpointid").toString()));

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Saves binary image to local disk
    private String saveImage(String byteimage, String path) {
        String filePath = "";
        try {
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            filePath = Startup.imagesPath + "/" + fileName;// fileName;
            //Base64 base64= new Base64();
            byte[] bytes = Base64.decode(byteimage);
            InputStream in = new ByteArrayInputStream(bytes);
            BufferedImage bImageFromConvert = ImageIO.read(in);
            // BufferedImage bImageFromConvert =
            // JPEGCodec.createJPEGDecoder(in).decodeAsBufferedImage();
            File file = new File(filePath);
            if (!file.exists()) {
                ImageIO.write(bImageFromConvert, "jpg", file);
                return fileName;
            } else {
                final String fileNameWithoutExt = fileName.substring(0,
                        fileName.lastIndexOf('.'));
                File dir = new File(Startup.imagesPath);
                File[] files = dir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        name = name.substring(0, name.lastIndexOf('.'));
                        if (name.indexOf('(') > -1) {
                            name = name.substring(0, name.indexOf('('));
                        }
                        return name.equals(fileNameWithoutExt);
                    }
                });

                String newFileName = "";
                if (files.length == 1) {
                    newFileName = fileNameWithoutExt + "(1).jpg";
                } else {
                    int lastNo = -1;
                    for (int i = 0; i < files.length; i++) {
                        String lastFile = files[i].getName();
                        if (lastFile.indexOf('(') > -1) {
                            int num = Integer.parseInt(lastFile.substring(
                                    lastFile.indexOf('(') + 1,
                                    lastFile.indexOf(')')));
                            if (num > lastNo) {
                                lastNo = num;
                            }
                        }
                    }
                    lastNo++;
                    newFileName = fileNameWithoutExt + "(" + lastNo + ").jpg";
                }
                filePath = Startup.imagesPath + "/" +  newFileName;
                ImageIO.write(bImageFromConvert, "jpg", new File(filePath));
                return newFileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
