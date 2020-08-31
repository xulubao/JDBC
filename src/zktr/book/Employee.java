package zktr.book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

/**
 * 员工类
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.book
 * @date 2020/8/14 13:45
 */
public class Employee {
    private static String empId = null;//员工ID
    private static String empName = null;//员工名称
    private  boolean state =false;//员工登陆状态
    BookTest bookTest = new BookTest();
    public Employee(String empId) {
        Employee.empId = empId;
    }
    public Employee(){}

    public void setEmpId(String empId) {
        Employee.empId = empId;
    }
    public String getEmpId(){
        return empId;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void menuEmp() throws SQLException, ClassNotFoundException {
        boolean menuState = true;//程序运行状态
        int menu =0;//菜单
        int submenu;//子菜单
        System.out.println("**********请输入菜单进行选择**********");
        while (menuState){
            System.out.println("【1】登陆系统\t【2】查询员工信息\t【3】编辑员工信息\t【4】注册员工\t【0】返回上一层");
            menu = bookTest.isMenu(0,4);
            switch (menu){
                case 1:
                    this.loginEmployee();
                    break;
                case 2:
                    System.out.print("要查询的状态【1】在职\t【2】离职 :");
                    submenu=bookTest.isMenu(1,2);
                    this.printAllEmpState(submenu+1);
                    break;
                case 3:
                    System.out.print("要修改的信息【1】密码 【2】状态  :");
                    submenu=bookTest.isMenu(1,2);
                    if(submenu==1){
                        this.alterEmpPass(this.getEmpId());
                    }else {
                        System.out.print("请输入要修改员工状态的ID:");
                        String empIDS = scanner.next();
                        System.out.print("请输入状态【1】离职【0】不修改:");
                        int namestate = bookTest.isMenu(0,1);
                        if (namestate==0){
                            break;
                        }else {
                            this.alterEmpState(empIDS,namestate+2);
                        }
                    }
                    break;
                case 4:
                    this.addEmp();
                    break;
                default:
                    menuState=false;
                    break;
            }
        }
    }

    /**
     * 管理员是否登陆
     * @return
     */
    public boolean isEmpState(){
        //当员工ID没有、状态为夹时表示未登陆
        if (empId==null&&state==false){
            return false;
        }else {
            return true;
        }
    }
    BaseDao bd = new BaseDao();
    Scanner scanner = new Scanner(System.in);

    /**
     * 创建员工编号
     * @return
     * @throws SQLException
     */
    public String createEmpId() throws SQLException {
        String empId = "emp";
        Date date = new Date();
        int year = date.getYear()+1900;
        empId=empId+year;
        try {
            String sql = "select max(Empid) as Empid from employee where empid like ?";
            ResultSet rs = bd.query(sql,empId+"%");
            if (rs.next()){
                String x = rs.getString("Empid");
                if (x==null){
                    empId = empId+"001";
                }else {
                    x = x.substring(7);
                    int y=Integer.parseInt(x)+1;
                    if (y<10){
                        empId = empId+"00"+y;
                    }else if (y<100){
                        empId = empId+"0"+y;
                    }else {
                        empId=empId+y;
                    }
                }
            }
        } catch (SQLException throwables) {
            throw  new SQLException("创建员工编号错误！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return empId;
    }

    /**
     * 员工注册
     */
    public void addEmp() throws SQLException {
        System.out.print("请输入员工姓名:");
        String name=null;
        //判断姓名是否重复
        String sql = "select empname from employee where empname=?;";
        while (true) {
            try {
                 name= new Scanner(System.in).next();
                ResultSet rs = bd.query(sql, name);
                if (rs.next()) {
                    System.out.print("该员工姓名已存在！请重新输入:");
                    continue;
                }else {
                    break;
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throw new SQLException("员工查询重复姓名出错");
            }
        }
        System.out.print("请输入密码：");
        String pass = null;
        while (true){
            pass = scanner.next();
            if (this.checkPassLength(pass)){
                break;
            }else {
                System.out.print("输入的密码长度不在6~20位之间，请重新输入:");
                continue;
            }
        }
        sql = "insert into employee(Empid,empname,emppass)values (?,?,?);";
         try {
             int row=bd.update(sql,this.createEmpId(),name,pass);
             if (row>0){
                 System.out.println("添加员工成功!");
             }
             bd.close();
        } catch (SQLException throwables) {
            throw new SQLException("添加员工错误！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询员工是否在职
     * @param i 状态
     */
    public void printAllEmpState(int i) throws SQLException {
        String sql = null;
        if (i==3){
             sql= "select * from employee where state=?";
        }else {
            sql = "select * from employee where state!=?";
        }
        try {
            ResultSet rs = bd.query(sql,i);
            System.out.println("员工编号\t\t员工姓名\t 在职状态");
            while (rs.next()){
                String empId = rs.getString("Empid");
                String empName = rs.getString("empname");
                String state = rs.getInt("state")==3?"离职":"在职";
                System.out.println(empId+"\t"+empName+"\t"+state);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throw new SQLException("查询员工状态有误！");
        }
    }

    /**
     * 检查是否员工编号跟密码匹配
     * @param empname 员工姓名
     * @param empPass 员工密码
     * @return
     * @throws SQLException
     */
    public boolean checkPass(String empname,String empPass) throws SQLException {
        boolean ck_pass=false;//默认为错误
        String sql = "select empid from employee where empname=? and emppass=? and state!=3";
        try {
            ResultSet rs = bd.query(sql,empname,empPass);
            if (rs.next()){
                ck_pass = true;//编号跟密码正确
            }else {
                ck_pass = false;
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throw new SQLException("检查密码跟编号是否匹配错误!");
        }
        return ck_pass;
    }

    /**
     * 检查密码是否符合数据词典
     * @param empPass 要验证的密码
     * @return 是否符合数据词典
     */
    public boolean checkPassLength(String empPass){
        if(empPass.length()>=6&&empPass.length()<=20){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 修改员工状态员工状态
     * @param empId 员工编号
     * @param state 状态值
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean alterEmpState(String empId,int state) throws SQLException, ClassNotFoundException {
        if (empId.equals(this.getEmpId())&&this.getState()){
            System.out.println("不能修改当前已经登陆的状态");
            return false;
        }
        String sql = "update employee set state=? where empId=? and state!=3;";
        int i = bd.update(sql,state,empId);
        bd.close();
        if (i>0){
            System.out.println("修改成功");
            return true;
        }else {
            System.out.println("员工编号错误，或者已离职！修改失败。");
            return false;
        }
    }

    /**
     * 修改员工密码
     * @param empId 员工编号
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void alterEmpPass(String empId) throws SQLException, ClassNotFoundException {
        if (!this.isEmpState()){
            System.out.println("您还未登陆系统，不能修改密码！");
            return;
        }
        System.out.print("请输入原密码:");
        String empPass;
        int num =1;//当输入的原密码错误超过三次时修改失败并退出登陆
        while (true){
            if (num>3){
                this.setState(false);
                this.setEmpId(null);
                empName = null;
                this.alterEmpState(empId,2);
                System.out.println("输入的原密码错误超过三次，修改失败，退出本次登陆！请重新登陆");
                this.loginEmployee();
            }else {
                num++;
            }
            empPass = scanner.next();
            if (this.checkPass(empName,empPass)){
                //说明密码跟编号符合
                break;
            }else {
                System.out.print("输入的原密码错误！请重新输入:");
                continue;
            }
        }
        System.out.print("请输入新密码:");
        while (true){
            empPass = scanner.next();
            if (this.checkPass(empName,empPass)){
                break;
            }else {
                System.out.print("输入的密码长度不符合规定[6-20位]！\n请重新输入新密码：");
                continue;
            }
        }
        String sql = "update employee set emppass=? where empid=?";
        bd.update(sql,empPass,empId);
        System.out.println("修改密码成功！");
    }

    /**
     * 员工登陆
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void loginEmployee() throws SQLException, ClassNotFoundException {
        if (this.isEmpState()){
            System.out.println("已登陆！如需重新登陆请重新运行程序！");
            return;
        }
        System.out.print("请输入姓名:");
        String empname = scanner.next();
        System.out.print("请输入密码:");
        String empPass = scanner.next();
        if (this.checkPass(empname,empPass)){
            String sql = "select empid from employee where empname=? and state!=3";
            ResultSet rs = bd.query(sql,empname);
            if (rs.next()){
                empId = rs.getString("empid");
            }
            this.alterEmpState(empId,1);
            empName = empname;
            this.setEmpId(empId);
            this.state=true;
            System.out.println("登陆成功！欢迎"+empname+"来到书香满屋书吧！");
        }else {
            System.out.println("员工编号或密码错误！");
        }
    }
}
