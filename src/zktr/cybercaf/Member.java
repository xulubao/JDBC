package zktr.cybercaf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 会员表
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.cybercaf
 * @date 2020/8/17 13:28
 */
public class Member {
    Scanner scanner = new Scanner(System.in);
    ResultSet rs = null;
    BaseDao bd = new BaseDao();
    Tool tool = new Tool();

    /**
     * 会员管理菜单
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void meMenu() throws SQLException, ClassNotFoundException {
        boolean stateMenu = true;
        while (stateMenu){
            System.out.println("【1】注册会员\t【2】查询会员\t【3】编辑会员\t【4】会员注销\t【0】返回上一层");
            int menu = tool.isMenu(0,4);
            switch (menu){
                case 1:
                    this.addMem();
                    break;
                case 2:
                    //这里通过1，2，3，为什么是相反的，在显示会员的sql语句中通过查询相反的状态，代码更少
                    System.out.println("请输入查询方式【1】查看注销的会员   【2】查看可用的会员   【3】查看全部会员");
                    this.findAllMem(tool.isMenu(1,3));
                    break;
                case 3:
                    this.alterMemInfo();
                    break;
                case 4:
                    this.outMem();
                    break;
                case 0:
                    stateMenu=false;//退出循环，返回到上一层
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 获取会员编号编号
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String getByMemId() throws SQLException, ClassNotFoundException {
        String sql = "select max(memid) as memid from member;";
        try{
            rs = bd.query(sql,null);
        }catch (SQLException S){
            throw new SQLException("查询最大的编号错误！");
        }
        String memID = "id";
        if (rs.next()){
            String x = rs.getString("memid");
            if (x==null){
                memID="id00001";
            }else {
                x=x.substring(3);
                int i=Integer.parseInt(x)+1;
                if (i<10){
                   memID = "id0000"+i;
                }else if (i<100){
                    memID = "id000"+i;
                }else if (i<1000){
                    memID = "id00"+i;
                }else if (i<10000){
                    memID = "id0"+i;
                }else {
                    memID = "id"+i;
                }
            }
        }
        return memID;
    }

    /**
     * 注册会员
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void addMem() throws SQLException, ClassNotFoundException {
        System.out.print("请输入会员姓名:");
        String memName = scanner.next();
        if (this.checkMemName(memName,1)){//说明会员表中已经存在注销的用户，直接将该用户状态改为可用，会员等级恢复默认
            String sql = "update member set state=1,grade=1 where memname=?;";
            int row = bd.update(sql,memName);
            if (row>0){
                System.out.println("表中已存在该用户，状态已改为可用！");
                return;
            }
        }else if (this.checkMemName(memName,2)){
            System.out.println("会员表中已存在该用户，可直接上网！");
            return;
        }
        String sql = "insert into member values(?,?,1,1);";
        int row = bd.update(sql,this.getByMemId(),memName);
        if (row>0){
            System.out.println("注册会员成功！");
        }else {
            System.out.println("注册会员失败！");
        }
    }

    /**
     * 根据状态查询会员
     * @param state 会员状态
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void findAllMem(int state) throws SQLException, ClassNotFoundException {
        String sql = "select memid,memname,state,grade from member where state!=?;";
        try{
            rs=bd.query(sql,state);
        }catch (SQLException sq){
            System.out.println(sq.getMessage());
            System.out.println("查询会员状态错误!");
            return;
        }
        System.out.println("会员编号\t会员姓名\t状态\t会员等级");
        while (rs.next()){
            String memid = rs.getString("memid");
            String memname = rs.getString("memname");
            String states = rs.getInt("state")==1?"可用":"注销";
            String grade = rs.getInt("grade")==1?"普通会员":rs.getInt("grade")==2?"高级会员":"VIP会员";
            System.out.println(memid+"\t"+memname+"\t\t"+states+"\t"+grade);
            if (rs.getRow()==0){
                System.out.println("暂无数据！");
                break;
            }
        }

    }

    /**
     * 修改会员信息
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void alterMemInfo() throws SQLException, ClassNotFoundException {
        System.out.println("【1】修改姓名 【2】修改状态 【3】修改等级 【0】返回上一层");
        int subMenu = tool.isMenu(0,3);
        String sql = null;//接下来要执行sql语句
        switch (subMenu){
            case 1:
                System.out.print("请输入原姓名:");
                String memname = scanner.next();
                if (!this.checkMemName(memname,2)){//查询姓名在会员表中存在且状态可用的
                    System.out.println("输入的姓名已存在或者已注销，修改失败！");
                    return;
                }
                System.out.print("请输入新姓名:");
                String memname1 = scanner.next();
                while (this.checkMemName(memname1,3)){
                    System.out.print("修改的姓名会员表中已经存在！请重新输入【0】不修改：");
                    memname1 = scanner.next();
                    if ("0".equals(memname1)){
                        this.alterMemInfo();
                    }
                }
                //上面都执行完说明要修改的姓名可用
                sql = "update member set memname=? where memname=?;";
                int row = bd.update(sql,memname1,memname);
                if (row>0){
                    System.out.println("修改会员姓名成功！");
                }else {
                    System.out.println("修改会员失败！");
                }
                break;
            case 2:
                System.out.print("请输入要修改状态的会员姓名:");
                String mamname = scanner.next();
                if (!this.checkMemName(mamname,3)){
                 System.out.println("输入的会员姓名错误！");
                 this.alterMemInfo();//重新调用修改信息方法
                }
                System.out.println("请输入状态【1】可用 【2】注销");
                int state = tool.isMenu(1,2);
                sql = "update member set state=? where memname=?;";
                int row2 = bd.update(sql,state,mamname);
                if (row2>0){
                    System.out.println("修改会员状态成功！");
                }else {
                    System.out.println("修改会员状态失败！");
                }
                break;
            case 3:
                System.out.print("请输入要修改等级的会员姓名:");
                String mamname3 = scanner.next();
                if (!this.checkMemName(mamname3,2)){
                    System.out.println("输入的会员姓名错误或已注销！");
                    this.alterMemInfo();//重新调用修改信息方法
                }
                System.out.println("请输入状态【1】普通会员 【2】高级会员　【3】VIP会员");
                int grade = tool.isMenu(1,3);
                sql = "update member set grade=? where memname=?;";
                int row3 = bd.update(sql,grade,mamname3);
                if (row3>0){
                    System.out.println("修改会员状态成功！");
                }else {
                    System.out.println("修改会员状态失败！");
                }
                break;
            default:
                break;
            case 0:
                break;
        }
    }

    /**
     * 注销会员
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void outMem() throws SQLException, ClassNotFoundException {
        System.out.print("请输入要注销的会员姓名:");
        String memname = scanner.next();
        if (this.checkMemName(memname,1)){
            System.out.println("该会员已经注销！");
            return;
        }
        String sql = "update member set state=2 where memname=?;";
        int row = bd.update(sql,memname);
        if (row>0){
            System.out.println("已成功注销该会员！");
        }else {
            System.out.println("用户表未找到该用户！");
        }
    }
    /**
     * 根据状态查询是否有该名字的会员
     * @param memName
     * @param state
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean checkMemName(String memName,int state) throws SQLException, ClassNotFoundException {
        String sql = "select memname from member where memname=? and state!=?;";
        try {
            rs = bd.query(sql,memName,state);
        } catch (SQLException throwables) {
            throw new SQLException("查询会员表重名错误！");
        }
        if (rs.next()){
            return true;
        }
        return false;
    }

    /**
     * 根据会员姓名获取会员编号
     * @param memname 会员姓名
     * @return
     *//*
    public String getByMemId(String memname) throws SQLException, ClassNotFoundException {
        //判断1：判断上机的用户状态是否可用 [直接查询用户名状态是否可用]
        if(!this.checkMemName(memname,2)){
            System.out.println("未在会员表中找到该用户或已注销！");
            UpandDown upandDown = new UpandDown();
            upandDown.UDMenu();//说明未找到该姓名
        }
        String sql = "select memid from member where memname=?;";
        rs = bd.query(sql,memname);
        if (rs.next()){
            return rs.getString("memid");
        }else {
            System.out.println("未找到会员姓名！");
            return null;
        }
    }
*/
    /**
     * 根据编号或者会员姓名获取会员等级
     * @return
     */
   /* public int getOutMoney(String member) throws SQLException, ClassNotFoundException {
        String sql = "select grade from member where memid=? or memname=?;";
        try {
            rs = bd.query(sql, member, member);
        }catch (SQLException S){
            throw new SQLException("获取会员等级错误！");
        }
        int grade = 1;//会员等级
        if (rs.next()){
            grade = rs.getInt("grade");
        }
        return grade;
    }*/
}
