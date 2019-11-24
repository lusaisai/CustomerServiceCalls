package lusaisai;

import java.sql.*;
import java.util.Random;
import java.util.UUID;

public class SingleCall implements Runnable {
    private Connection conn;
    private String callUUID;
    private int agentID;

    public SingleCall(Connection conn) {
        this.conn = conn;
        this.callUUID = UUID.randomUUID().toString();
        this.agentID = (int) (new Random().nextGaussian() * 100 + 500);
        System.out.println("Call ready, UUID: " + this.callUUID);
    }

    public void call() throws SQLException, InterruptedException {
        String insertStr = "insert into calls (call_uuid, start_time, agent_id)" +
                "values (?, ?, ?)";
        PreparedStatement ps = this.conn.prepareStatement(insertStr);
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        ps.setString(1, this.callUUID);
        ps.setTimestamp(2, startTime);
        ps.setInt(3, this.agentID);
        ps.executeUpdate();
        ps.close();

        Thread.sleep(new Random().nextInt(300*1000));

        String updStr = "update calls set end_time = ? where call_uuid = ?";
        ps = this.conn.prepareStatement(updStr);
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        ps.setTimestamp(1, endTime);
        ps.setString(2, this.callUUID);
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void run() {
        try {
            this.call();
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
