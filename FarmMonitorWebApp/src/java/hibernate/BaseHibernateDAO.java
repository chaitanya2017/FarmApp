package hibernate;

import org.hibernate.Session;


/*
* This class returns a hibenrate session
*/
public class BaseHibernateDAO implements IBaseHibernateDAO {

    /*
    * returns a session from HibernateSessionFactory
    */
    public Session getSession() {
	return HibernateSessionFactory.getSession();
    }
}