package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getConnectionString(String propertyFileName) {
        Properties properties = new Properties();
        try (InputStream input = DBPropertyUtil.class.getClassLoader().getResourceAsStream(propertyFileName)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + propertyFileName);
                return null;
            }
            properties.load(input);
            // Retrieve connection properties from the property file
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            return url + ";user=" + user + ";password=" + password;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}


