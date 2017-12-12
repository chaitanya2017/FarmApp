/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import monitor.EmailBean;
import org.junit.Test;
import static org.junit.Assert.*;


public class EMailBeanTest {
    
    public EMailBeanTest() {
    }
    
    /*
    * test sendEmail function of EmailBean
    */
    @Test
    public void testSendEmail() {
        EmailBean email = new EmailBean();
        boolean status = email.sendEmail("chvarmak@gmail.com", "Test Subject", "Test Email");
        assertTrue(status);
    }
}
