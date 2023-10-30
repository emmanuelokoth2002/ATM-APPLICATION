import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class Withdraw extends DB {
    public Withdraw() {
        super();
    }

    public String performWithdrawal(int accountId, double amount) {
        String result = "error";

        try (Connection connection = connect();
             CallableStatement callableStatement = connection.prepareCall("{call sp_withdraw(?, ?, ?)}")) {
            callableStatement.setInt(1, accountId);
            callableStatement.setDouble(2, amount);
            callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
            callableStatement.execute();
            result = callableStatement.getString(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
