package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    private Properties properties;

    private FileInputStream inputStream;

    public PropertyReader() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try {
            inputStream = new FileInputStream("src/main/resources/config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.print("Unable to load config.properties");
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
