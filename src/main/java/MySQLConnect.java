import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLConnect {
    public static Connection connectToDatabase(String propertiesFilePath) {
        Properties properties = DatabaseConfig.loadProperties(propertiesFilePath);

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection successful!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            return null;
        }
    }
}
