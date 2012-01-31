package br.com.ibnetwork.guara.app.email;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.xingu.template.Context;
import br.com.ibnetwork.xingu.messaging.MessageDispatcher;

public class MessageUtils
{
    public static final String CHARSET = "ISO-8859-1";

    private static final Log logger = LogFactory.getLog(MessageUtils.class);
    
    public static Boolean sendMessage(MessageDispatcher md, Email email, Context ctx, String html, String text) 
        throws Exception
    {
        Message message = MessageUtils.createMessage(md.getSession(),email,html,text);      
        logger.info("Sending message from["+email.getFrom()+"] to["+email.getTo()+"]");
        boolean sent = md.sendMessage(message);
        logger.info("message from["+email.getFrom()+"] to["+email.getTo()+"] sent successfully ? " + sent);
        return sent ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Message createMessage(Session session,Email email, String html, String text)
    	throws Exception
    {
        MimeMessage message = new MimeMessage(session);

        //from
        String from = email.getFrom();
        if(email.getSenderName() != null)
        {
            from = "\""+email.getSenderName()+"\" <" + email.getFrom() + ">"; 
        }
        message.setFrom(new InternetAddress(from));

        //to
        String[] to = StringUtils.split(email.getTo(), ",");
        addRecipientsToMessage(to, RecipientType.TO, message);

        //repply to
        String replyTo = email.getReplyTo();
        if(replyTo != null)
        {
            Address[] addresses = createAddresses(new String[]{replyTo});
            message.setReplyTo(addresses);
        }
        
        //cc
        String[] cc = StringUtils.split(email.getCc(), ",");
        addRecipientsToMessage(cc, RecipientType.CC, message);

        //bcc
        String[] bcc = StringUtils.split(email.getBcc(), ",");
        addRecipientsToMessage(bcc, RecipientType.BCC, message);
        
        //subject
        message.setSubject(email.getSubject(), CHARSET);

        //body
        Multipart emailBody = createMultipart(html, text);
        message.setContent(emailBody);

        return message;
    }

    private static void addRecipientsToMessage(String[] recipients,RecipientType type, MimeMessage message) 
    	throws Exception
    {
        if(recipients == null)
        {
            return;
        }
        
        Address[] addresses = createAddresses(recipients);
        for (int i = 0; i < addresses.length; i++)
        {
            Address address = addresses[i];
            message.addRecipient(type, address);
        }
    }

    public static Address[] createAddresses(String[] recipients) 
		throws Exception
	{
        Address[] addresses = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++)
        {
            String recipient = recipients[i];
            addresses[i] = new InternetAddress(recipient);
        }
        return addresses;
	}

    private static Multipart createMultipart(String html, String text)
    	throws Exception
    {
        Multipart result = new MimeMultipart("alternative");

        //html
        if(StringUtils.isNotEmpty(html))
        {
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(html, "text/html; charset=\"" + CHARSET + "\"");
            result.addBodyPart(htmlPart);
        }
        
        //text
        if(StringUtils.isNotEmpty(text))
        {
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(text, "text/plain; charset=\"" + CHARSET + "\"");
            result.addBodyPart(textPart);
            
        }
        //TODO: adicionar anexos -> http://www.jguru.com/faq/view.jsp?EID=532257
        return result;
    }
}