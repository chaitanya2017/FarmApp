/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import javax.servlet.http.HttpServlet;

/**
 *
 * Startup servlet reads configuration.cfg file to initialize parameters
 */
public class Startup extends HttpServlet {
    
    public static String dbUrl = "jdbc:mysql://localhost:3306/farmmonitor";
    public static String userName = "root";
    public static String password = "root";
    public static String imagesPath = "";
    
    /*
    * Reads configuration.cfg file
    */
    public void init()
    {
        try
        {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(getServletContext().getRealPath("")
				+ "/configuration.cfg")));
            
            if(!(properties.getProperty("dburl")==null || properties.getProperty("dburl").equals("")))
            {
		dbUrl = properties.getProperty("dburl");		
            }
            if(!(properties.getProperty("username")==null || properties.getProperty("username").equals("")))
            {
		userName = properties.getProperty("username");
            }
            if(!(properties.getProperty("password")==null || properties.getProperty("password").equals("")))
            {
		password = properties.getProperty("password");
            }
            if(!(properties.getProperty("imagespath")==null || properties.getProperty("imagespath").equals("")))
            {
		imagesPath = properties.getProperty("imagespath");
                System.out.println(imagesPath);
            }
        }
        catch(Exception ex)
        {
            java.util.logging.Logger.getLogger(Startup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
