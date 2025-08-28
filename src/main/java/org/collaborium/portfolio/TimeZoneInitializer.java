
package org.collaborium.portfolio;

import java.util.TimeZone;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TimeZoneInitializer implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    // Set the default timezone for the web application early on.
    TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago"));
    System.out.println("TimeZoneInitializer: default timezone set to " +
                       TimeZone.getDefault().getID());
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // no-op
  }
}
