package edu.iastate.agron.mesonet;

import java.sql.*;
import org.collaborium.portfolio.*;

public class mSite {

  private String id = "0";
  private String name = "null";
  private String hid = null;
  private String nid = null;
  private String mid = null;
  private String lat = null;
  private String lon = null;
  private String state = null;
  private String elev = null;
  private String portfolio = null;

  /**
   * Blank Constructor
   */
  public mSite() {}

  public mSite(String INportfolio, String idnum) {
    this.portfolio = INportfolio;
    try {
      ResultSet rs =
          dbInterface.callDB("SELECT * from iem_sites WHERE "
                             + " s_mid = '" + idnum + "' and portfolio = '" +
                             this.portfolio + "' ");
      while (rs.next()) {
        this.setName(rs.getString("s_name"));
        this.setID(rs.getString("id"));
        this.setMID(rs.getString("s_mid"));
        this.setNID(rs.getString("s_nid"));
        this.setHID(rs.getString("s_hid"));
        this.setLat(rs.getString("s_lat"));
        this.setLon(rs.getString("s_lon"));
        this.setState(rs.getString("s_st"));
        this.setElev(rs.getString("s_elev"));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      plogger.report("Problem in Setting site information");
    }

  } // End of mSite

  /**
   * Get the value of elevation
   */
  public String getElev() { return this.elev; }

  /**
   * Set the value of elevation
   */
  public void setElev(String in) {
    if (in == null)
      this.elev = "";
    else
      this.elev = in;
  }

  /**
   * Get the value of State
   */
  public String getState() { return this.state; }

  /**
   * Set the value of State
   */
  public void setState(String in) {
    if (in == null)
      this.state = "";
    else
      this.state = in;
  }

  /**
   * Get the value of longitude
   */
  public String getLon() { return this.lon; }

  /**
   * Set the value of longitude
   */
  public void setLon(String in) {
    if (in == null)
      this.lon = "";
    else
      this.lon = in;
  }

  /**
   * Get the value of latitude
   */
  public String getLat() { return this.lat; }

  /**
   * Set the value of latitude
   */
  public void setLat(String in) {
    if (in == null)
      this.lat = "";
    else
      this.lat = in;
  }

  /**
   * Get the in House ID of this station
   */
  public String getHID() { return this.hid; }

  /**
   * Set the in house ID of this station
   */
  public void setHID(String in) {
    if (in == null)
      this.hid = "";
    else
      this.hid = in;
  }

  /**
   * Get the nws ID of this station
   */
  public String getNID() { return this.nid; }

  /**
   * Set the nws ID of this station
   */
  public void setNID(String in) {
    if (in == null)
      this.nid = "";
    else
      this.nid = in;
  }

  /**
   * Get the mesonet ID of this station
   */
  public String getMID() { return this.mid; }

  /**
   * Set the mesonet ID of this station
   */
  public void setMID(String in) {
    if (in == null)
      this.mid = "";
    else
      this.mid = in;
  }

  /**
   * Get the name of this station
   */
  public String getName() { return this.name; } // End of getName

  /**
   * Set the name of this station
   */
  public void setName(String in) {
    if (in == null)
      this.name = "";
    else
      this.name = in;
  } // End of setName

  /**
   * Return the ID of this site
   */
  public String getID() { return this.id; }

  /**
   * Set the ID of this site
   */
  public void setID(String in) {
    if (in == null)
      this.id = "0";
    else
      this.id = in;
  }

  /**
   * Method that updates the database entry for this station
   */
  public void update() {
    dbInterface.updateDB("UPDATE iem_sites SET s_name = '" + name + "', "
                         + " s_hid = '" + hid + "', s_nid = '" + nid +
                         "', s_mid = '" + mid + "', "
                         + " s_lat = " + lat + ", s_lon = " + lon +
                         ", s_st = '" + state + "', "
                         + " s_elev = " + elev + " WHERE id = " + id + " ");
  }

} // End of class declaration
