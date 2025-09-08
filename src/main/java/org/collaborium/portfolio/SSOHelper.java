package org.collaborium.portfolio;

import javax.servlet.http.HttpServletRequest;

/** SSO integration helper for Apache OIDC authentication */
public class SSOHelper {

  /** Check if the request contains SSO authentication headers */
  public static boolean isSSO(HttpServletRequest request) {
    return request.getHeader("X-Remote-User-Email") != null;
  }

  /**
   * Get the authenticated user ID from SSO headers Extract username from email
   * by splitting on '@'
   */
  public static String getSSOUserID(HttpServletRequest request) {
    String email = request.getHeader("X-Remote-User-Email");
    if (email != null && email.contains("@")) {
      return email.split("@")[0];
    }
    return email;
  }

  /** Get the authenticated user email from SSO headers */
  public static String getSSOUserEmail(HttpServletRequest request) {
    return request.getHeader("X-Remote-User-Email");
  }

  /** Get the authenticated user full name from SSO headers */
  public static String getSSOUserName(HttpServletRequest request) {
    return request.getHeader("X-Remote-User-Name");
  }

  /** Create or retrieve a portfolioUser based on SSO authentication */
  public static portfolioUser getSSOUser(HttpServletRequest request) {
    String userID = getSSOUserID(request);
    String email = getSSOUserEmail(request);
    String fullName = getSSOUserName(request);

    if (userID == null) {
      return null;
    }

    // Try to find existing user
    portfolioUser user = null;
    try {
      user = new portfolioUser(userID);
      if (!user.isPortfolioUser()) {
        // User doesn't exist, create them
        createSSOUser(userID, email, fullName);
        user = new portfolioUser(userID);
      }
    } catch (Exception e) {
      // User doesn't exist, create them
      createSSOUser(userID, email, fullName);
      user = new portfolioUser(userID);
    }

    return user;
  }

  /** Create a new user account from SSO information */
  private static void createSSOUser(String userID, String email,
                                    String fullName) {
    String firstName = "";
    String lastName = "";

    // Parse name format: "last name, first name and department"
    if (fullName != null && fullName.contains(",")) {
      String[] parts = fullName.split(",", 2);
      lastName = parts[0].trim();

      // Extract first name (everything before potential department info)
      String firstPart = parts[1].trim();
      // Split by space and take the first word as first name
      if (firstPart.contains(" ")) {
        firstName = firstPart.split(" ")[0];
      } else {
        firstName = firstPart;
      }
    } else if (fullName != null) {
      // Fallback: treat as "first last" format
      String[] parts = fullName.split(" ", 2);
      firstName = parts[0];
      if (parts.length > 1) {
        lastName = parts[1];
      }
    }

    try {
      jlib.updateDB("INSERT into users(fname, lname, passwd, username, email) "
                    + " values('" + jlib.cleanString(firstName) + "', '" +
                    jlib.cleanString(lastName) + "', 'SSO_AUTH', '" + userID +
                    "', '" + (email != null ? email : "") + "')");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
