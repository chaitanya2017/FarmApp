package monitor;

import java.io.Serializable;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
* This class is used for sending email
*/
@ManagedBean(name = "emailBean")
@ViewScoped
public class EmailBean implements Serializable {

    //Fields
    private String toEmail;
    private String subject;
    private String message;
    private UIComponent emailButton;

    // Constructor
    public EmailBean() {

    }

    //
    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    //Property accessors
    public String getToEmail() {
        return this.toEmail;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setEmailButton(UIComponent emailButton) {
        this.emailButton = emailButton;
    }

    public UIComponent getEmailButton() {
        return emailButton;
    }

    /*
    * Calls sendEmail method and displays sent message status in the UI
    */
    public void send() {
        FacesMessage facesMessage = null;
        if(sendEmail(this.toEmail, this.subject, this.message) == true)
        {
           facesMessage  = new FacesMessage("Successfully Sent Email");
        }
        else
        {
            facesMessage = new FacesMessage("Failed to Send Email");
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(emailButton.getClientId(context), facesMessage);
    }
    
    /*
    * Sends an email using gmail
    * @Param emailTo - to email address
    * @Param emailSubject - email subject
    * @Param emailMessage - email message
    */
    public boolean sendEmail(String emailTo, String emailSubject, String emailMessage)
    {
        final String username = "farmmonitormanager@gmail.com";
        final String password = "Manager123";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(emailSubject);
            message.setText(emailMessage);

            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            return false;
        }
    }
}
