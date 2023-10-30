import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {
    private String servername;
    private String username;
    private String password;
    private String dbname;

    public DB() {
        this.servername = "localhost";
        this.username = "root";
        this.password = "84u)4J26";
        this.dbname = "atm";
    }

    public Connection connect() {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://" + servername + "/" + dbname;
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public ResultSet getData(String sql) {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(sql);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getJSON(String sql) {
        ResultSet resultSet = getData(sql);
        StringBuilder json = new StringBuilder();
        json.append("[");
        try {
            while (resultSet.next()) {
                json.append("{");
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    json.append("\"").append(resultSet.getMetaData().getColumnName(i)).append("\":\"")
                        .append(resultSet.getString(i)).append("\",");
                }
                json.deleteCharAt(json.length() - 1); // Remove the last comma
                json.append("},");
            }
            if (json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1); // Remove the last comma
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        json.append("]");
        return json.toString();
    }

/*     public static void main(String[] args) {
        DB db = new DB();
        String query = "SELECT * FROM useraccount";
        String jsonResult = db.getJSON(query);
        System.out.println(jsonResult);
    }*/
}
