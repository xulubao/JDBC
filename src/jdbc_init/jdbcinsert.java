package jdbc_init;

import java.sql.*;

public class jdbcinsert {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/zktr","root","123456");
        String sql = "insert into student values(?,?,?,?,?);";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,"3");
        ps.setString(2,"xu");
        ps.setString(3,"女");
        ps.setDate(4,java.sql.Date.valueOf("1999-01-2"));
        ps.setString(5,"02702");
        int row = ps.executeUpdate();
        if (row>0){
            System.out.println("1111");
        }
        ps.close();
        connection.close();

    }
}
