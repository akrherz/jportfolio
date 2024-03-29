package org.collaborium.portfolio.chat;

import com.oreilly.servlet.HttpMessage;
import java.applet.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class HttpChatApplet extends Applet implements Runnable {

  TextArea text;
  Label label;
  TextField input;
  Thread thread;
  String user;
  Calendar myCal;
  Integer myHour;
  Integer myMin;

  public void init() {
    // Check if this applet was loaded directly from the filesystem.
    // If so, explain to the user that this applet needs to be loaded
    // from a server in order to communicate with that server's servlets.
    URL codebase = getCodeBase();
    if (!"http".equals(codebase.getProtocol())) {
      System.out.println();
      System.out.println("*** Whoops! ***");
      System.out.println("This applet must be loaded from a web server.");
      System.out.println("Please try again, this time fetching the HTML");
      System.out.println("file containing this servlet as");
      System.out.println("\"http://server:port/file.html\".");
      System.out.println();
      System.exit(1); // Works only from appletviewer
                      // Browsers throw an exception and muddle on
    }

    // Get this user's name from an applet parameter set by the servlet
    // We could just ask the user, but this demonstrates a
    // form of servlet->applet communication.
    user = getParameter("user");
    if (user == null)
      user = "anonymous";

    // Set up the user interface...
    // On top, a large TextArea showing what everyone's saying.
    // Underneath, a labeled TextField to accept this user's input.
    text = new TextArea();
    text.setEditable(false);
    label = new Label("Say something: ");
    input = new TextField();
    input.setEditable(true);

    setLayout(new BorderLayout());
    Panel panel = new Panel();
    panel.setLayout(new BorderLayout());

    add("Center", text);
    add("South", panel);

    panel.add("West", label);
    panel.add("Center", input);
  }

  public void start() {
    thread = new Thread(this);
    thread.start();
  }

  String getNextMessage() {
    String nextMessage = null;
    while (nextMessage == null) {
      try {
        URL url = new URL(getCodeBase(), "/jportfolio/servlet/ChatServlet");
        HttpMessage msg = new HttpMessage(url);
        InputStream in = msg.sendGetMessage();
        BufferedReader data = new BufferedReader(new InputStreamReader(in));
        nextMessage = data.readLine();
      } catch (SocketException e) {
        // Can't connect to host, report it and wait before trying again
        System.out.println("Can't connect to host: " + e.getMessage());
        try {
          Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
      } catch (FileNotFoundException e) {
        // Servlet doesn't exist, report it and wait before trying again
        System.out.println("Resource not found: " + e.getMessage());
        try {
          Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
      } catch (Exception e) {
        // Some other problem, report it and wait before trying again
        System.out.println("General exception: " + e.getClass().getName() +
                           ": " + e.getMessage());
        try {
          Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
      }
    }
    return nextMessage + "\n";
  }

  public void run() {
    Thread thisThread = Thread.currentThread();
    while (thread == thisThread) {
      text.append(getNextMessage());
    }
  }
  /* http://java.sun.com/j2se/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html
   */
  public void stop() { thread = null; }

  void broadcastMessage(String message) {
    myCal = Calendar.getInstance();
    myHour = new Integer(myCal.get(Calendar.HOUR));
    myMin = new Integer(myCal.get(Calendar.MINUTE));
    String timeStr = myHour.toString() + ":" + myMin.toString();

    message =
        user + "@" + timeStr + ": " + message; // Pre-pend the speaker's name
    try {
      URL url = new URL(getCodeBase(), "/jportfolio/servlet/ChatServlet");
      HttpMessage msg = new HttpMessage(url);
      Properties props = new Properties();
      props.put("message", message);
      msg.sendPostMessage(props);
    } catch (SocketException e) {
      // Can't connect to host, report it and abandon the broadcast
      System.out.println("Can't connect to host: " + e.getMessage());
    } catch (FileNotFoundException e) {
      // Servlet doesn't exist, report it and abandon the broadcast
      System.out.println("Resource not found: " + e.getMessage());
    } catch (Exception e) {
      // Some other problem, report it and abandon the broadcast
      System.out.println("General exception: " + e.getClass().getName() + ": " +
                         e.getMessage());
    }
  }

  public void processEvent(AWTEvent event) {
    switch (event.getID()) {
    case Event.ACTION_EVENT:
      if (event.getSource() == input) {
        broadcastMessage(input.getText());
        input.setText("");
      }
    }
  }
}
