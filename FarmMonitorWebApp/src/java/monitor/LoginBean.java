package monitor;

import hibernate.*;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;

/*
* This class is used for managing user login session
*/
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    //Fields
    private String lastlogindate;
    private String uname;
    private String password;
    private UIComponent loginButton;

    public LoginBean() {
        
    }

    //Property accessors
    public void setLoginButton(UIComponent loginButton) {
        this.loginButton = loginButton;
    }

    public UIComponent getLoginButton() {
        return loginButton;
    }

    public void setLastlogindate(String lastlogindate) {
        this.lastlogindate = lastlogindate;
    }

    public String getLastlogindate() {
        return lastlogindate;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUname() {
        return uname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    /*
    * this function gets called from the UI, validates user credentials and creates session
    */
    public void loginProject() {
        try {
            UsersDAO userDao = new UsersDAO();
            Users users = checkUserCredentials(this.uname, this.password, userDao);
            if (users != null) {
                createSession(users.getUsername(), users.getUserid());               

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (users.getLastlogindate() != null) {
                    lastlogindate = formatter.format(users.getLastlogindate());
                }
                userDao.updateLastLoginDate(users);
                FacesContext.getCurrentInstance().getExternalContext().redirect("../pages/index.xhtml");
            } else {
                FacesMessage message = new FacesMessage("Invalid Username or Password");
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(loginButton.getClientId(context), message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
    * validate user credentials
    */
    public Users checkUserCredentials(String userName, String password, UsersDAO userDao)
    {
        Users users = userDao.login(userName, password);
        return users;
    }
    
    /*
    * create session for the user
    */
    public HttpSession createSession(String userName, Integer userId)
    {
        HttpSession session = SessionUtil.getSession();
        session.setAttribute("username", userName);
        session.setAttribute("userid", userId);
        return session;
    }

    /*
    * logs out the user
    */
    public void logout() {
        try {
            HttpSession session = SessionUtil.getSession();
            session.invalidate();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../pages/login.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
    * checks user seesion is valid
    */
    public boolean checkSession() {
        try {
            HttpSession session = SessionUtil.getSession();
            if (session == null || session.getAttribute("username") == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../pages/login.xhtml");
                return false;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return true;
    }
}
