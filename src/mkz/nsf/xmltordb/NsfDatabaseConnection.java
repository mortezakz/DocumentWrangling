package mkz.nsf.xmltordb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NsfDatabaseConnection {

    public static Connection c;

    private static String url = "jdbc:postgresql://localhost:5433";
    private static String db = "MkzDissertation";
    private static String username = "postgres";
    private static String password = "123456";
    public static final String AWARD_TABLE_NAME = "nsf";
    public static final String TABLE_NAME = "pubmed";


    public static void connect() throws SQLException {

        c = DriverManager.getConnection(url + "/" + db, username, password);
        c.setAutoCommit(true);

    }

    public static void close() throws SQLException {

        c.close();

    }
}
