package com.server;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * This class creates a servlet for handling HTTO requests to get JSON string
 */
public class FarmServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static ProcessRequest processRequest = new ProcessRequest();

    public FarmServlet() {

    }

    /**
     * Gets JSON and uploads JSON string to database
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String json = request.getParameter("json");
            //json = URLDecoder.decode(json, "UTF-8");
            System.out.println("JSON " + json);
            String res = processRequest.process(json);
            System.out.println("Response : " + res);
            response.getWriter().print(res);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("Error");
        }
    }
}
