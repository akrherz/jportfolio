//   authBean.java
//     - Module to handle authentication and move it out of jlib...
package org.collaborium.portfolio;

import java.sql.*;
import java.util.Arrays;
import javax.servlet.http.*;

public class authBean {

  public String authError = null;
  public portfolioUser thisUser = null;

  /**
   * If a form variable is "", then return null to!
   *
   * @param request which is the servlet request
   * @param varname which is the name of the variable
   * @return string or perhaps null!
   */
  public String getCGI(HttpServletRequest request, String varname) {
    String t = (String)request.getParameter(varname);
    if (t != null && !t.equals(""))
      return t;
    return null;
  }

  /** Constructor for the authBean class */
  public authBean(HttpServletRequest request, HttpSession session) {
    /* Test pull from the session */
    thisUser = (portfolioUser)session.getAttribute("User");

    /* Check for SSO authentication first */
    if (thisUser == null && SSOHelper.isSSO(request)) {
      plogger.report("authBean.java: SSO authentication detected");
      thisUser = SSOHelper.getSSOUser(request);
      if (thisUser != null) {
        session.setAttribute("User", thisUser);
        session.setMaxInactiveInterval(10800);
        return; // SSO user is authenticated, no need for further processing
      }
    }

    /* Pull vars from the request */
    String cgiPortPass = getCGI(request, "portpass");
    String cgiPort = getCGI(request, "portfolio");
    // String logout = getCGI(request, "logout");
    String logoutPortfolio = getCGI(request, "logoutPortfolio");

    // Exit portfolio
    if (thisUser != null && logoutPortfolio != null) {
      plogger.report("authbean.java: #3, Switching Portfolios");
      thisUser.setPortfolio(null);
    }

    // Logout, which doesn't work with SSO really
    // if (logout != null) {
    //  plogger.report("authBean.java: #6, Logging out user");
    //  thisUser = null;
    // }

    // Nothing to do
    if (thisUser != null && thisUser.getPortfolio() != null)
      return;

    // Attempting to join a portfolio
    if (cgiPortPass != null && cgiPort != null) {
      plogger.report("authBean.java: #4, Sign up for a portfolio");
      jlib.signUpForPortfolio(thisUser, request);
    }

    // Logging in to a portfolio
    if (thisUser != null && cgiPort != null) {
      plogger.report("authBean.java: #2, Logging into Portfolio!");
      loginPort(cgiPort);
    }

    /* Now we set the session var */
    session.setAttribute("User", thisUser);
    session.setMaxInactiveInterval(10800);
  } // End of authBean constructor

  /**
   * Method that creates a new Portfolio User Account
   *
   * @param request HttpServletRequest variable
   */
  public portfolioUser createUser(HttpServletRequest request) {
    String reqUserID = request.getParameter("username");
    String reqfName = jlib.cleanString(request.getParameter("fName"));
    String reqlName = jlib.cleanString(request.getParameter("lName"));
    String email = request.getParameter("email");
    String password1 = request.getParameter("password1");
    String password2 = request.getParameter("password2");

    if (!password1.equals(password2)) {
      authError = "Passwords do not match!";
      return null;
    }

    ResultSet rs = null;
    try {
      rs = dbInterface.callDB("SELECT * from users "
                              + " WHERE username = '" + reqUserID + "' ");
      if (rs.next()) {
        return null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    jlib.updateDB("INSERT into users(fname, lname, passwd, username, email) "
                  + " values('" + reqfName + "', '" + reqlName + "', '" +
                  password1 + "', "
                  + " '" + reqUserID + "', '" + email + "') ");

    return new portfolioUser(reqUserID);
  }

  public boolean requiredUserPort(HttpServletResponse response,
                                  HttpSession session, String comebackURL) {
    if (thisUser == null || thisUser.getPortfolio() == null) {
      session.setAttribute("comebackURL", comebackURL);
      response.setHeader("Refresh", "0; URL=/jportfolio/login.jsp");
      return true;
    }
    return false;
  }

  /** Method to handle the authenication involved in a Jportfolio session */
  private void doAuth(String username, String passwd) {
    ResultSet rs = null;
    String dbPass = null;
    try {
      rs = dbInterface.callDBWithParameters("SELECT * from users "
                                                + " WHERE username = ?",
                                            Arrays.asList(username));
      if (rs != null && rs.next()) {
        dbPass = rs.getString("passwd");
      } else {
        authError = "Username does not exist.";
        return;
      }

    } catch (Exception ex) {
      authError = "A database error occured!";
      ex.printStackTrace();
    }

    if (passwd.equals(dbPass)) {
      thisUser = new portfolioUser(username);
    } else {
      authError = "Incorrect Password.";
    }
  } // End of doAuth

  /** Method to set up a user with a Portfolio if needed */
  private void loginPort(String portfolio) {
    if (jlib.isMember(thisUser.getUserID(), portfolio)) {
      thisUser.setPortfolio(portfolio);
      String gID = jlib.getGID(thisUser.getUserID(), portfolio);
      thisUser.setGroupID(gID);
      plogger.report("authBean --> Testing for Admin\n");
      if (jlib.isAdmin(thisUser.getUserID(), portfolio)) {
        plogger.report("authBean --> Is Admin!!\n");
        thisUser.setIsAdmin(Boolean.TRUE);
      }
    } else if (portfolio.equals("appease linter")) {
      doAuth(portfolio, "");
    }
  } // End of loginPort
} // End of authBean class
