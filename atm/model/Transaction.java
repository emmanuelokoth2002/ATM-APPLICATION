import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class Transaction extends DB {
    public Transaction() {
        super();
    }

    public double getBalance(int accountId) {
        try (Connection connection = connect();
             CallableStatement callableStatement = connection.prepareCall("{call sp_balance_inquiry(?, ?)}")) {
    
            callableStatement.setInt(1, accountId);
            callableStatement.registerOutParameter(2, Types.DOUBLE);
            callableStatement.execute();
    
            return callableStatement.getDouble(2);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0; // Return a default value or handle the error appropriately
        }
    }  
    
    

    public void deposit(int accountId, double amount) {
        try (Connection connection = connect();
             CallableStatement callableStatement = connection.prepareCall("{call sp_deposit(?, ?)}")) {
            callableStatement.setInt(1, accountId);
            callableStatement.setDouble(2, amount);
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean withdraw(int accountId, double amount) {
        try (Connection connection = connect();
             CallableStatement callableStatement = connection.prepareCall("{call sp_withdraw(?, ?, ?)}")) {
    
            callableStatement.setInt(1, accountId);
            callableStatement.setDouble(2, amount);
            callableStatement.registerOutParameter(3, Types.VARCHAR); // Assuming result_message is a VARCHAR output parameter
    
            callableStatement.execute();
    
            // Check the result message after executing the stored procedure
            String resultMessage = callableStatement.getString(3);
    
            // You can check the result message and decide how to proceed
            if (resultMessage.equals("success")) {
                return true;
            } else {
                // Handle the error or display the result message as needed
                System.out.println("Withdrawal failed: " + resultMessage);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false to indicate a failure
        }
    }
    
    

    public boolean transfer(int senderAccountId, int receiverAccountId, double amount) {
        try (Connection connection = connect();
             CallableStatement callableStatement = connection.prepareCall("{call sp_transfer(?, ?, ?, ?)}")) {
            callableStatement.setInt(1, senderAccountId);
            callableStatement.setInt(2, receiverAccountId);
            callableStatement.setDouble(3, amount);
            callableStatement.registerOutParameter(4, Types.INTEGER); // Register the output parameter as INTEGER
    
            callableStatement.execute();
    
            // Check the result of the transfer and return true if it was successful (1), false if it failed (0)
            int transferResult = callableStatement.getInt(4);
            return transferResult == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false; // If there was an exception or another issue, return false
    }
    
    

    public boolean billPayment(int accountId, double amount, String payee) {
        try (Connection connection = connect();
             CallableStatement callableStatement = connection.prepareCall("{call sp_bill_payment(?, ?, ?, ?)}")) {
            callableStatement.setInt(1, accountId);
            callableStatement.setDouble(2, amount);
            callableStatement.setString(3, payee);
            callableStatement.registerOutParameter(4, Types.BOOLEAN); // Register the output parameter
    
            callableStatement.execute();
    
            // Check the result of the bill payment and return true if it was successful
            return callableStatement.getBoolean(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false; // If there was an exception or another issue, return false
    }
    

    public List<String> getTransactionHistory(int accountId) {
        List<String> transactionHistory = new ArrayList<>();
        try (Connection connection = connect();
             CallableStatement callableStatement = connection.prepareCall("{call sp_transactionhistory(?)}")) {
            callableStatement.setInt(1, accountId);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                String transactionDetails = resultSet.getString("transaction_details");
                transactionHistory.add(transactionDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionHistory;
    }
}
