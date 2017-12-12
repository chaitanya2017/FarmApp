package monitor;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
* This class manages HTTPSession 
*/
public class SessionUtil {

    /*
    * creates a session
    */
    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
    }

    /*
    * gets servlet request
    */
    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
    }

    /*
    * gets user name of the current logged in user
    */
    public static String getUserName() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);       
        
        return session.getAttribute("username").toString();
    }

    /*
    * gets user login id of the current logged in user
    */
    public static String getUserLoginId() {
        HttpSession session = getSession();
        if (session != null) {
            return (String) session.getAttribute("userloginid");
        } else {
            return null;
        }
    }
}
