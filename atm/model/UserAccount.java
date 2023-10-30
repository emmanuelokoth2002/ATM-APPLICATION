import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccount extends DB {
    public UserAccount() {
        super();
    }

    public int createAccount(int pin, double balance) {
        int accountnumber = -1;

        try (Connection connection = connect();
             CallableStatement callableStatement = connection.prepareCall("{call sp_saveaccount(?, ?, ?)}")) {
            callableStatement.setInt(1, pin);
            callableStatement.setDouble(2, balance);
            callableStatement.registerOutParameter(3, java.sql.Types.INTEGER);
            callableStatement.execute();
            accountnumber = callableStatement.getInt(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountnumber;
    }

    public UserAccountData retrieveAccount(int enteredPin) {
        UserAccountData accountData = null;

        try (Connection connection = connect();
             CallableStatement callableStatement = connection.prepareCall("{call sp_getaccount(?)}")) {
            callableStatement.setInt(1, enteredPin);
            ResultSet resultSet = callableStatement.executeQuery();
            if (resultSet.next()) {
                int pin = resultSet.getInt("pin");
                double balance = resultSet.getDouble("balance");
                accountData = new UserAccountData(enteredPin, pin, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountData;
    }

    public void deleteAccount(int accountnmber) {
        try (Connection connection = connect();
             CallableStatement callableStatement = connection.prepareCall("{call sp_deleteaccount(?)}")) {
            callableStatement.setInt(1, accountnmber);
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public class UserAccountData {
        private int accountNumber;
        private int pin;
        private double balance;

        public UserAccountData(int accountNumber, int pin, double balance) {
            this.accountNumber = accountNumber;
            this.pin = pin;
            this.balance = balance;
        }

        public int getAccountNumber() {
            return accountNumber;
        }

        public int getPin() {
            return pin;
        }

        public double getBalance() {
            return balance;
        }
    }
}
