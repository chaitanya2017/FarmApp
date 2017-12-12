package hibernate;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/*
* This class is gets user details from database
*/
public class UsersDAO extends BaseHibernateDAO {

    public UsersDAO() {

    }

    /*
    * gets user details based on userid
    * @param userid - user id
    */
    public static Users getUser(int userid) {

        Session hib_session = HibernateSessionFactory.getSession();
        Transaction transaction = hib_session.beginTransaction();
        try {
            String hql_query = "from hibernate.Users where userid=" + userid;
            List<Users> list = hib_session.createQuery(hql_query).list();
            return list.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            transaction.commit();
            hib_session.clear();
            hib_session.close();
        }
    }

    /*
    * validates user credentials
    * @param username - user name of user
    * @param password - password of user
    */
    public Users login(String username, String password) {
        Users userlogin = null;
        Session hib_session = null;
        Transaction transaction = null;
        try {
            hib_session = HibernateSessionFactory.getSession();
            transaction = hib_session.beginTransaction();
            String query = " from hibernate.Users where username='" + username + "' and password='" + password + "'";
            List<Users> list = hib_session.createQuery(query).list();

            if (list.size() > 0) {
                userlogin = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (transaction != null) {
                transaction.commit();
            }
            if (hib_session != null) {
                hib_session.clear();
                hib_session.close();
            }
        }
        return userlogin;
    }

    /*
    * update user last login date
    * @param user - reference to user
    */
    public int updateLastLoginDate(Users user) {
        Session hib_session = null;
        Transaction transaction = null;
        int result = -1;
        try {
            hib_session = HibernateSessionFactory.getSession();
            transaction = hib_session.beginTransaction();
            String query = " update hibernate.Users set lastlogindate=now() where userid=" + user.getUserid();
            result = hib_session.createQuery(query).executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (transaction != null) {
                transaction.commit();
            }
            if (hib_session != null) {
                hib_session.clear();
                hib_session.close();
            }
        }
        return result;
    }
}
