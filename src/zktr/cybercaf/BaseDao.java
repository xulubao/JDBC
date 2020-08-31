package zktr.cybercaf;

import java.sql.*;

public class BaseDao {
    private static Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * 获取Connection
     *
     * @return 连接
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void getConnection() throws ClassNotFoundException,SQLException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zktrbook?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC","root","123456");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("驱动加载有误");
        } catch (SQLException throwables) {
            throw new SQLException("数据库连接有误");
        }
    }

    /**
     * SQL修改方法，完成数据的insert、delete、update操作
     * @param sql     sql要执行的语句
     * @param objects 因为不知道有多少个参数，所以用'...'表示多个参数，不知道传入的是什么类型，可以是int、String所以用Object类型
     * @return 影响的行数
     */
    public int update(String sql, Object... objects) throws SQLException, ClassNotFoundException {
        int row = 0;
        try {
            this.createStatement(sql, objects);
            row  = ps.executeUpdate();
        }catch (ClassNotFoundException ce){
            throw new ClassNotFoundException("连接有无！");
        }catch(SQLException ex){
            throw new SQLException("修改的sql语句有误！");
        }
        this.close();
        return row;
    }

    /**
     * 查询sql语句
     *
     * @param sql     要执行的sql语句
     * @param objects 参数
     * @return 结果集
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ResultSet query(String sql, Object... objects) throws SQLException, ClassNotFoundException {
        try {
            this.createStatement(sql, objects);
            rs = ps.executeQuery();
            return rs;
        }catch(SQLException se){
            throw new SQLException("查询有误！");
        }
    }

    /**
     * 创建执行对象，并绑定参数
     */
    public void createStatement(String sql, Object...objects) throws SQLException, ClassNotFoundException {
        try{
            //调用连接方法
            this.getConnection();
            ps = con.prepareStatement(sql);
            //绑定参数
            if (objects != null) {
                for (int i = 0; i < objects.length; i++) {
                    ps.setObject(i + 1, objects[i]);
                }
            }
        }catch(SQLException se){
            throw new SQLException("参数绑定有误");
        }
    }

    /**
     * 释放资源
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        try{
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }}catch (SQLException ex){
            throw new SQLException("释放资源有误！");
        }
    }
}
