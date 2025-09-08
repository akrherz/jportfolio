package org.collaborium.portfolio.chat;

// import edu.iastate.iitap.portfolio.*;
import com.oreilly.servlet.RemoteDaemonHttpServlet;
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChatServlet extends RemoteDaemonHttpServlet implements ChatServer {

  // source acts as the distributor of new messages
  MessageSource source = new MessageSource();

  // socketClients holds references to all the socket-connected clients
  Vector<Socket> socketClients = new Vector<>();

  // rmiClients holds references to all the RMI clients
  Vector<ChatClient> rmiClients = new Vector<>();

  // doGet() returns the next message.  It blocks until there is one.
  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    // Return the next message (blocking)
    out.println(getNextMessage());
  }

  // doPost() accepts a new message and broadcasts it to all
  // the currently listening HTTP and socket clients.
  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    // Accept the new message as the "message" parameter
    String message = req.getParameter("message");

    // Broadcast it to all listening clients
    if (message != null)
      broadcastMessage(message);

    // Set the status code to indicate there will be no response
    res.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  // getNextMessage() returns the next new message.
  // It blocks until there is one.
  public String getNextMessage() {
    // Create a message sink to wait for a new message from the
    // message source.
    return new MessageSink().getNextMessage(source);
  }

  // broadcastMessage() informs all currently listening clients that there
  // is a new message.  Causes all calls to getNextMessage() to unblock.
  public void broadcastMessage(String message) {
    // Send the message to all the HTTP-connected clients by giving the
    // message to the message source
    source.sendMessage(message);

    // Directly send the message to all the socket-connected clients
    Enumeration<Socket> myenum = socketClients.elements();
    while (myenum.hasMoreElements()) {
      Socket client = null;
      try {
        client = myenum.nextElement();
        PrintStream out = new PrintStream(client.getOutputStream());
        out.println(message);
      } catch (IOException e) {
        // Problem with a client, close and remote it
        try {
          if (client != null)
            client.close();
        } catch (IOException ignored) {
        }
        socketClients.removeElement(client);
      }
    }

    // Directly send the message to all RMI clients
    Enumeration<ChatClient> myenum2 = rmiClients.elements();
    while (myenum2.hasMoreElements()) {
      ChatClient chatClient = null;
      try {
        chatClient = myenum2.nextElement();
        chatClient.setNextMessage(message);
      } catch (RemoteException e) {
        // Problem communicating with a client, remove it
        deleteClient(chatClient);
      }
    }
  }

  protected int getSocketPort() {
    // We listen on port 2428 (look at a phone to see why)
    return 2428;
  }

  public void handleClient(Socket client) {
    // We have a new socket client.  Add it to our list.
    socketClients.addElement(client);
  }

  public void addClient(ChatClient client) {
    // We have a new RMI client.  Add it to our list.
    rmiClients.addElement(client);
  }

  public void deleteClient(ChatClient client) {
    // Remote the specified client from our list.
    rmiClients.removeElement(client);
  }
}

// MessageSource acts as the source for new messages.
// Clients interested in receiving new messages can
// register a BlockingQueue listener and will receive the next
// message via that queue.
class MessageSource {
  private final CopyOnWriteArrayList<BlockingQueue<String>> listeners =
      new CopyOnWriteArrayList<>();

  public void addListener(BlockingQueue<String> q) { listeners.add(q); }

  public void removeListener(BlockingQueue<String> q) { listeners.remove(q); }

  public void sendMessage(String message) {
    for (BlockingQueue<String> q : listeners) {
      // best-effort delivery; offer won't block
      q.offer(message);
    }
  }
}

// MessageSink acts as the receiver of new messages.
// It listens to the source by registering a temporary BlockingQueue
// and blocking on take() until a message arrives.
class MessageSink {
  // Gets the next message sent out from the message source
  public String getNextMessage(MessageSource source) {
    BlockingQueue<String> q = new LinkedBlockingQueue<>(1);
    source.addListener(q);
    try {
      String msg = q.take();
      return msg != null ? msg : "";
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      return "";
    } finally {
      source.removeListener(q);
    }
  }
}
