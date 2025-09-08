package com.oreilly.servlet;

import java.io.IOException;
import java.net.Socket;
import javax.servlet.http.HttpServlet;

public class RemoteDaemonHttpServlet extends HttpServlet {
  protected int getSocketPort() { return 2428; }
  public void handleClient(Socket s) throws IOException { s.close(); }
}
