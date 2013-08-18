package org.collaborium.portfolio.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {
  public void setNextMessage(String message) throws RemoteException;
}
