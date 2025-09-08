package com.oreilly.servlet;

import java.io.*;
import java.net.*;
import java.util.*;

public class HttpMessage {
  private URL url;

  public HttpMessage(URL url) { this.url = url; }

  public InputStream sendGetMessage() throws IOException {
    URLConnection conn = url.openConnection();
    return conn.getInputStream();
  }

  public void sendPostMessage(Properties props) throws IOException {
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    conn.setDoOutput(true);
    conn.setRequestMethod("POST");
    try (OutputStream os = conn.getOutputStream()) {
      StringBuilder sb = new StringBuilder();
      for (String name : props.stringPropertyNames()) {
        if (sb.length() > 0)
          sb.append('&');
        sb.append(URLEncoder.encode(name, "UTF-8"));
        sb.append('=');
        sb.append(URLEncoder.encode(props.getProperty(name), "UTF-8"));
      }
      os.write(sb.toString().getBytes("UTF-8"));
    }
    int rc = conn.getResponseCode();
  }
}
