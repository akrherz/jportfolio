package com.oreilly.servlet;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class MailMessage {
  private ByteArrayOutputStream baos = new ByteArrayOutputStream();
  private PrintStream ps = new PrintStream(baos, true);
  private String from;
  private List<String> to = new ArrayList<>();
  private List<String> cc = new ArrayList<>();
  private List<String> bcc = new ArrayList<>();
  private String subject;
  private String mailhost;

  public MailMessage(String mailhost) { this.mailhost = mailhost; }

  public void from(String f) { this.from = f; }
  public void to(String t) { this.to.add(t); }
  public void cc(String c) { this.cc.add(c); }
  public void bcc(String b) { this.bcc.add(b); }
  public void setSubject(String s) { this.subject = s; }

  public PrintStream getPrintStream() { return ps; }

  public void sendAndClose() throws Exception {
    ps.flush();
    Properties props = System.getProperties();
    if (mailhost != null)
      props.put("mail.smtp.host", mailhost);
    Session session = Session.getInstance(props, null);
    MimeMessage message = new MimeMessage(session);
    if (from != null)
      message.setFrom(new InternetAddress(from));
    for (String t : to)
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(t));
    for (String c : cc)
      message.addRecipient(Message.RecipientType.CC, new InternetAddress(c));
    for (String b : bcc)
      message.addRecipient(Message.RecipientType.BCC, new InternetAddress(b));
    if (subject != null)
      message.setSubject(subject);
    message.setText(baos.toString("UTF-8"));
    Transport.send(message);
  }
}
