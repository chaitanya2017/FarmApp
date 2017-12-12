package hibernate;

import java.util.Date;

/**
 * Users entity.
 */
public class Users implements java.io.Serializable {

    //Fields
    private Integer userid;
    private String username;
    private String password;
    private Date lastlogindate = null;

    // Constructors
    public Users() {
    }

    public Users(Integer userid, String username) {
        this.userid = userid;
        this.username = username;
        this.password = password;
    }

    public Users(Integer userid, String username, String password,
            Date lastlogindate) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.lastlogindate = lastlogindate;
    }

    //Property accessors
    public Integer getUserid() {
        return this.userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastlogindate() {
        return this.lastlogindate;
    }

    public void setLastlogindate(Date lastlogindate) {
        this.lastlogindate = lastlogindate;
    }

}
