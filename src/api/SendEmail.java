package api;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendEmail
{

    static String userName;
    static String password;
    static Properties properties = new Properties();
    public static void SetProp(String uN, String pw)
    {
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        userName=uN;
        properties.put("mail.user", uN);
        password=pw;
        properties.put("mail.password", pw);
    }

    public static void sendPdf(
            String toAddress,
            String subject,
            String content,
            String Pdfpath)
            throws AddressException, MessagingException, UnsupportedEncodingException
    {
            Message msg = new MimeMessage(Session.getInstance(properties, new Authenticator()
            {
                public PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(userName, password);
                }
            }));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            msg.setSubject(subject);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart PdfBP = new MimeBodyPart();
            PdfBP.setDataHandler(new DataHandler(new FileDataSource(Pdfpath + ".pdf")));
            PdfBP.setFileName(MimeUtility.encodeText( new String( (Pdfpath + ".pdf").getBytes( "utf-8" ) ), "Cp1251", "B" ) );
            MimeBodyPart TextBP = new MimeBodyPart();
            TextBP.setText(content);
            multipart.addBodyPart(PdfBP);
            multipart.addBodyPart(TextBP);
            msg.setContent(multipart);
            Transport.send(msg);
    }
    public static void sendText(
            String toAddress,
            String subject,
            String content)
            throws AddressException, MessagingException
    {
            Message message = new MimeMessage(Session.getInstance(properties, new Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(userName, password);
            }
        }));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            //Заголовок письма
            message.setSubject(subject);
            //Содержимое
            message.setText(content);
            //Отправляем сообщение
            Transport.send(message);
    }
}