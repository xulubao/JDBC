package jdbc_init;
import java.sql.*;
import java.util.Scanner;

public class Employee {
    private static Connection connection = null;//桥
    private static PreparedStatement ps = null;//车
    public static void main(String[] args) {

        //1.加载驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/zktr?characterEncoding=utf8&useSSL=true","root","123456");
            Employee employee = new Employee();
            while (true){
                System.out.println("1、员工注册\t2、修改电话号码\t3、删除员工\t4、员工登陆\t5显示所有员工");
                String menu = new Scanner(System.in).next();
                switch (menu){
                    case "1":
                        employee.addEmployee();
                        break;
                    case "2":
                        employee.alterphone();
                        break;
                    case "3":
                        employee.deleteemp();
                        break;
                    case "4":
                        employee.login();
                        break;
                    case "5":
                        employee.printemp();
                        break;
                    case "6":
                        System.exit(0);
                        break;

                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("mysql驱动加载失败！(可能版本错误)");
            e.printStackTrace();
        }catch (SQLException throwables) {
            System.out.println("连接地址错误，账号、密码、表是否存在");
            throwables.printStackTrace();
        }finally {
            try {
                ps.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 添加员工
     */
    public void addEmployee() throws SQLException {
        System.out.print("请输入员工姓名:");
        String name = new Scanner(System.in).next();
        System.out.print("请输入员工Card:");
        String card = new Scanner(System.in).next();
        String sql = "select empname,empcard from employee;";
        ps = connection.prepareStatement(sql);
        ResultSet resultSet = null;//仓
        resultSet = ps.executeQuery();
        while (resultSet.next()){
            if (resultSet.getString("empname").equals(name)||resultSet.getString("empcard").equals(card)){
                System.out.println("员工姓名或身份证号重复,添加失败!");
                return;
            }
        }
        System.out.print("请输入员工电话号码:");
        String phone = new Scanner(System.in).next();
        String isonjob = null;
        System.out.print("员工是是否在职（0在职，1离职，不填默认在职）:");
        isonjob= new Scanner(System.in).next();
        sql = "insert into employee(empname,empcard,empphone,isonjob) values(?,?,?,?);";
        ps = connection.prepareStatement(sql);
        ps.setString(1,name);
        ps.setString(2,card);
        ps.setString(3,phone);
        ps.setString(4,isonjob);
        int i=ps.executeUpdate();
        if (i>0){
            System.out.println("添加成功！");
        }else {
            System.out.println("添加失败！数据不符合规定");
        }
    }
    public void alterphone() throws SQLException {
        System.out.print("请输入员工用户名：");
        String name = new Scanner(System.in).next();
        String sql = "select empname from employee where empname=?;";
        ps = connection.prepareStatement(sql);
        ps.setString(1,name);
        ResultSet resultSet = null;//仓
        resultSet = ps.executeQuery();
        if (resultSet.getRow()<0){
            System.out.println("没有当前用户名！");
            return;
        }
        System.out.print("请输入修改的手机号：");
        String phone = new Scanner(System.in).next();
        if (phone.length()!=11){
            System.out.println("手机号长度不为11！");
        }
        sql = "update employee set empphone=? where empname=?";
        ps = connection.prepareStatement(sql);
        ps.setString(1,phone);
        ps.setString(2,name);
        int i=ps.executeUpdate();
        if (i>0){
            System.out.println("修改成功！");
        }else {
            System.out.println("修改失败！");
        }
    }

    /**
     * 根据用户名删除用户
     * @throws SQLException 运行异常
     */
    public void deleteemp() throws SQLException {
        System.out.print("请输入要删除的用户名：");
        String name = new Scanner(System.in).next();
        String sql = "delete from employee where empname=?;";
        ps = connection.prepareStatement(sql);
        ps.setString(1,name);
        int i=ps.executeUpdate();
        if (i>0){
            System.out.println("修改成功！");
        }else {
            System.out.println("修改失败！");
        }
    }

    /**
     * 登陆
     */
    public void login() throws SQLException {
        System.out.print("请输入员工姓名:");
        String name = new Scanner(System.in).next();
        System.out.print("请输入员工Card:");
        String card = new Scanner(System.in).next();
        String sql = "select empname,empcard from employee;";
        ps = connection.prepareStatement(sql);
        ResultSet resultSet = null;//仓
        resultSet = ps.executeQuery();
        while (resultSet.next()){
            if (resultSet.getString("empname").equals(name)&&resultSet.getString("empcard").equals(card)){
                System.out.println("登陆成功！欢迎"+name);
                return;
            }
        }
        System.out.println("登陆失败，账号或密码错误!");
    }
    public void printemp() throws SQLException {
        String sql = "select * from employee;";
        ps = connection.prepareStatement(sql);
        ResultSet set = ps.executeQuery();
        System.out.println("编号\t姓名\t身份证号\t\t\t电话号码\t是否在职");
        while (set.next()){
            int id = set.getInt("empid");
            String name = set.getString("empname");
            String card = set.getString("empcard");
            String phone = set.getString("empphone");
            String isonjob = set.getString("isonjob").equals("0")?"在职":"离职";
            System.out.println(id+"\t"+name+"\t"+card+"\t"+phone+"\t"+isonjob);
        }
    }
}
