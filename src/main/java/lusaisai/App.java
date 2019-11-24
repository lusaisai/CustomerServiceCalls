package lusaisai;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main( String[] args ) throws SQLException, InterruptedException {
        String connStr = "jdbc:mysql://192.168.110.128/customer_service?user=lusaisai&password=Mysql_633";
        Connection conn = DriverManager.getConnection(connStr);

        while (true) {
            new Thread(new SingleCall(conn)).start();
            Thread.sleep(100);
        }
    }
}
