package org.collaborium.portfolio.forecast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.collaborium.portfolio.dbInterface;
import org.collaborium.portfolio.portfolioUser;
import org.junit.Test;

public class ForecastDeadlineTest {
  @Test
  public void cutoffAndForecastDateUseChicagoTime() {
    assertEquals(
        LocalDate.of(2026, 11, 17),
        forecast.forecastDateBeforeDeadline(Instant.parse("2026-11-17T00:30:00Z"), 19));
    assertEquals(
        LocalDate.of(2026, 11, 17),
        forecast.forecastDateBeforeDeadline(Instant.parse("2026-11-17T00:59:59Z"), 19));
    assertNull(forecast.forecastDateBeforeDeadline(Instant.parse("2026-11-17T01:00:00Z"), 19));

    assertEquals(
        LocalDate.of(2026, 7, 17),
        forecast.forecastDateBeforeDeadline(Instant.parse("2026-07-16T23:30:00Z"), 19));
    assertNull(forecast.forecastDateBeforeDeadline(Instant.parse("2026-07-17T00:00:00Z"), 19));
    assertEquals(
        LocalDate.of(2026, 11, 18),
        forecast.forecastDateBeforeDeadline(Instant.parse("2026-11-17T06:00:00Z"), 19));
  }

  @Test
  public void activeDayLookupUsesChicagoTomorrowNotDatabaseTomorrow() throws Exception {
    Field dbField = dbInterface.class.getDeclaredField("db");
    dbField.setAccessible(true);
    Object previousConnection = dbField.get(null);
    try {
      dbField.set(null, databaseWithForecastDay("2026-11-17"));
      portfolioUser user =
          new portfolioUser() {
            @Override
            public String getPortfolio() {
              return "test";
            }
          };

      assertEquals(
          "2026-11-17", forecast.activeForecastDate(user, Instant.parse("2026-11-17T00:30:00Z")));
      assertNull(forecast.activeForecastDate(user, Instant.parse("2026-11-17T01:00:00Z")));
      assertNull(forecast.activeForecastDate(user, Instant.parse("2026-11-17T06:00:00Z")));
    } finally {
      dbField.set(null, previousConnection);
    }
  }

  private static Connection databaseWithForecastDay(String day) {
    return (Connection)
        Proxy.newProxyInstance(
            Connection.class.getClassLoader(),
            new Class<?>[] {Connection.class},
            (proxy, method, args) -> {
              if (!method.getName().equals("prepareStatement")) {
                throw new UnsupportedOperationException(method.getName());
              }
              String sql = (String) args[0];
              Map<Integer, Object> parameters = new HashMap<>();
              return Proxy.newProxyInstance(
                  PreparedStatement.class.getClassLoader(),
                  new Class<?>[] {PreparedStatement.class},
                  (statement, operation, values) -> {
                    if (operation.getName().equals("setObject")) {
                      parameters.put((Integer) values[0], values[1]);
                      return null;
                    }
                    if (operation.getName().equals("executeQuery")) {
                      boolean portfolioQuery = sql.contains("from portfolios");
                      boolean exists =
                          portfolioQuery
                              || (sql.contains("day = ?::date") && day.equals(parameters.get(2)));
                      return resultSet(exists);
                    }
                    throw new UnsupportedOperationException(operation.getName());
                  });
            });
  }

  private static ResultSet resultSet(boolean exists) {
    boolean[] first = {true};
    return (ResultSet)
        Proxy.newProxyInstance(
            ResultSet.class.getClassLoader(),
            new Class<?>[] {ResultSet.class},
            (proxy, method, args) -> {
              if (method.getName().equals("next")) {
                boolean result = first[0] && exists;
                first[0] = false;
                return result;
              }
              if (method.getName().equals("getInt")) return 19;
              throw new UnsupportedOperationException(method.getName());
            });
  }
}
