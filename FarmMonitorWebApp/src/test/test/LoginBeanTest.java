package test;

import hibernate.Users;
import hibernate.UsersDAO;
import monitor.LoginBean;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginBeanTest {
    
    public LoginBeanTest() {
    }

    LoginBean loginBean = new LoginBean();
    
    /*
    * test checkUserCredentials function of LoginBean
    */
    @Test
    public void testCheckUserCredentials() {
        UsersDAO loginobj = new UsersDAO();
        Users users = loginBean.checkUserCredentials("admin", "admin", loginobj);
        assertTrue(users != null);
    }
}
