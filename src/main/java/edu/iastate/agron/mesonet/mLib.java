package edu.iastate.agron.mesonet;

/** mLib , library for mesonet needs */
import java.sql.*;
import java.util.Arrays;
import org.collaborium.portfolio.*;

public class mLib {

  /**
   * stationSelect() - create HTML formated select for stations in network
   *
   * @param portfolio - value of portfolio
   * @return HTML formated string
   */
  public static String stationSelect(String portfolio) throws SQLException {
    StringBuffer sbuf = new StringBuffer();

    ResultSet rs = dbInterface.callDBWithParameters(
        "SELECT * from iem_sites WHERE "
            + " portfolio = ? ORDER by s_name ASC",
        Arrays.asList(portfolio));
    sbuf.append("<select name=\"s_mid\">\n");
    while (rs.next()) {
      sbuf.append("  <option value=\"" + rs.getString("s_mid") + "\">" +
                  rs.getString("s_name") + "\n");
    } // End of rs.next()
    sbuf.append("</select>\n");
    return sbuf.toString();
  } // End of stationSelect

  /**
   * stationSelectMulti() - create HTML formated select for stations in network
   *
   * @param portfolio - value of portfolio
   * @return HTML formated string
   */
  public static String stationSelectMulti(String portfolio)
      throws SQLException {
    StringBuffer sbuf = new StringBuffer();

    ResultSet rs =
        dbInterface.callDBWithParameters("SELECT * from iem_sites WHERE "
                                             + " portfolio = ? ",
                                         Arrays.asList(portfolio));
    sbuf.append("<select name=\"s_mid\" MULTIPLE>\n");
    while (rs.next()) {
      sbuf.append("  <option value=\"" + rs.getString("s_mid") + "\">" +
                  rs.getString("s_name") + "\n");
    } // End of rs.next()
    sbuf.append("</select>\n");
    return sbuf.toString();
  } // End of stationSelect

  /**
   * listContacts() - create HTML formated TABLE for site contacts
   *
   * @param site      value of mesonet site
   * @param portfolio value of the current portfolio
   * @return HTML formated string
   */
  public static String listContacts(String mid, String portfolio)
      throws SQLException {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append(
        "<table width=\"100%\">\n"
        + "<tr><th>Name:</th><th>Phone</th>\n<th>Email</th>\n<td></td>\n");
    ResultSet rs = dbInterface.callDBWithParameters(
        "SELECT * from iem_site_contacts WHERE "
            + " s_mid = ? and portfolio = ? ",
        Arrays.asList(mid, portfolio));
    if (rs == null) {
      sbuf.append("<tr><th colspan=\"3\">No Entries Found</th></tr>\n");
    } else {
      while (rs.next()) {
        sbuf.append(
            "<tr><td>" + rs.getString("name") + "</td>\n"
            + "<td>" + rs.getString("phone") + "</td>\n"
            + "<td>" + rs.getString("email") + "</td>\n"
            + "<td><a href=\"/jportfolio/mesonet/site/contacts.jsp?c_id=" +
            rs.getString("id") + "\">Delete</a></td></tr>\n");
      } // End of while
    } // End of else

    sbuf.append("</table>\n");
    return sbuf.toString();
  }
}
