package jdbc_init;

import java.sql.*;

/**
 * jdbc 初试
 *
 * @author xulubao
 * @date 2020/08/11
 */
public class jdbc初试 {
    private Connection con = null;//创建连接
    private PreparedStatement ps = null;//预执行
    private ResultSet rs = null;//结果集

    /**
     * 获取连接
     * @return
     * @throws ClassNotFoundException
     */
    public void getConnection() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zktr", "root", "123456");
        }catch(ClassNotFoundException cno){
            throw new ClassNotFoundException("驱动加载有误！");
        }catch (SQLException se){
            throw new SQLException("连接地址或密码有误！");
        }
    }

    /**
     * 修改数据
     * @param sql sql语句
     * @param objects 参数
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int update(String sql,Object... objects) throws SQLException, ClassNotFoundException {
        try {
            this.createStatement(sql, objects);
            int i = ps.executeUpdate();
            return i;
        }catch (SQLException SE){
            throw new SQLException("修改sql语句有误");
        }
    }

    /**
     * 查询数据
     * @param sql 查询的语句
     * @param objects 参数
     * @return
     * @throws SQLException sql语句错误
     * @throws ClassNotFoundException 加载驱动有误
     */
    public ResultSet query(String sql,Object...objects) throws SQLException, ClassNotFoundException {
        try {
            this.createStatement(sql, objects);
            rs = ps.executeQuery();
            return rs;
        }catch (SQLException se){
            throw new SQLException("查询修改语句有误！");
        }
    }

    public void createStatement(String sql,Object...objects) throws SQLException, ClassNotFoundException {
        try{
        this.getConnection();
        ps = con.prepareStatement(sql);
        if (objects!=null){
            for (int i=0;i<objects.length;i++){
                ps.setObject(i+1,objects[i]);
            }
        }} catch(SQLException se){
            throw new SQLException("参数添加有误！");
        }
    }

    /**
     * 关闭连接
     * @param con
     * @param ps
     * @param rs
     * @throws SQLException
     */
    public void close(Connection con, PreparedStatement ps, ResultSet rs) throws SQLException {
        try{
        if (con!=null){
            con.close();
        }
        if (ps!=null){
            ps.close();
        }
        if (rs!=null){
            rs.close();
        }}catch(SQLException se){
            throw new SQLException("释放资源有误！");
        }
    }
}
