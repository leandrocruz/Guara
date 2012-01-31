package br.com.ibnetwork.guara.app.email;

public class Email
{
	private String senderName;
	
	private String from;
	
	private String to;
	
	private String replyTo;
	
	private String cc;
	
	private String bcc;
	
	private String subject;
	
	private String body;
	
    public String getBcc(){return bcc;}
    public String getBody(){return body;}
    public String getCc(){return cc;}
    public String getFrom(){return from;}
    public String getSubject(){return subject;}
    public String getTo(){return to;}
    public void setBcc(String string){bcc = string;}
    public void setBody(String string){body = string;}
    public void setCc(String string){cc = string;}
    public void setFrom(String string){from = string;}
    public void setSubject(String string){subject = string;}
    public void setTo(String string){to = string;}
    public String getSenderName(){return senderName;}
    public void setSenderName(String string){senderName = string;}
    public String getReplyTo(){return replyTo;}
    public void setReplyTo(String replyTo){this.replyTo = replyTo;}
}
