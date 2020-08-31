package zktr.cybercaf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 电脑管理
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.cybercaf
 * @date 2020/8/17 11:30
 */
public class Computer {
    Scanner scanner = new Scanner(System.in);
    ResultSet rs = null;
    BaseDao bd = new BaseDao();
    Tool tool = new Tool();

    /**
     * 电脑管理菜单
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void cptMenu() throws SQLException, ClassNotFoundException {
        boolean stateMenu = true;
        while (stateMenu){
            System.out.println("【1】采购电脑\t【2】查询机器\t【0】返回上一层");
            int menu = tool.isMenu(0,2);
            switch (menu){
                case 1:
                    this.purchaseCpt();
                    break;
                case 2:
                    System.out.println("【1】全部\t【2】输入机器号查询");
                    this.selectCpt(tool.isMenu(1,2));
                    break;
                case 0:
                    stateMenu=false;//退出循环，返回到上一层
                default:
                    break;
            }
        }
    }


    /**
     * 获取电脑编号
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String getByCptId() throws SQLException, ClassNotFoundException {
        String sql = "select max(cptid) as cptid from computer;";
        try{
            rs = bd.query(sql,null);
        }catch (SQLException S){
            throw new SQLException("查询最大的编号错误！");
        }
        String cptID = "cpt";
        if (rs.next()){
            String x = rs.getString("cptid");
            if (x==null){
                cptID="cpt01";
            }else {
                x=x.substring(3);
                int i=Integer.parseInt(x)+1;
                if (i<10){
                    cptID=cptID+"0"+i;
                }else{
                    cptID=cptID+i;
                }
            }
        }
        return cptID;
    }

    /**
     * 采购电脑
     */
    public void purchaseCpt() throws SQLException, ClassNotFoundException {
        System.out.print("请输入采购的台数:");
        int cptNumber = tool.isMenu(0,Integer.MAX_VALUE);
        int row=0;
        for (int i=0;i<cptNumber;i++){
            String sql = "insert into computer values(?,0)";
            row=row+bd.update(sql,this.getByCptId());
        }
        if (row>0){
            System.out.println("成功采购"+row+"台电脑！");
        }else if (row==0){
            System.out.println("没有采购电脑！");
        }
    }

    /**
     * 查询所有电脑状态
     * @param state
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void selectCpt(int state) throws SQLException, ClassNotFoundException {
        String sql;//sql语句
        String cptid;//要查询的电脑编号
        rs = null;//结果集
        if (state==1){
            sql="select * from computer";
            rs = bd.query(sql,null);
        }else if(state==3){//在查看可用机器时调用
            sql = "select * from computer where state=0;";
            rs = bd.query(sql,null);
        }else {
            System.out.print("请输入机器号");
            cptid = scanner.next();
            if (!this.findByCptid(cptid,3)){//从系统中查询全部状态的电脑编号
             System.out.println("输入的机器号未找到！");
             return;
            }
            sql = "select cptid,state from computer where cptid=?;";
            rs = bd.query(sql,cptid);
        }
        System.out.println("电脑编号\t状态");
        while (rs.next()){
            cptid = rs.getString("cptid");
           String states = rs.getInt("state")==0?"未用":"已用";
           System.out.println(cptid+"\t"+states);
        }
    }

    /**
     * 根据电脑编号查询是否存在该电脑
     * @param cptid 要查询的电脑编号
     * @return 根据查询电脑编号、状态返回此台电脑是否可用
     * @throws SQLException
     */
    public boolean findByCptid(String cptid,int state) throws SQLException {

        String sql = "select cptid from computer where cptid=? and state!=?;";
        try {
            rs=bd.query(sql,cptid,state);
        } catch (SQLException throwables) {
            throw new SQLException("查询是否存在电脑编号错误！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (rs.next()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 根据电脑编号修改状态
     * @param cptid 电脑编号
     * @param state 电脑状态
     * @throws SQLException
     */
    public void alterComState(String cptid,int state) throws SQLException {
        String sql = "update computer set state=? where cptid=?;";
        try {
            bd.update(sql,state,cptid);
        } catch (SQLException throwables) {
            throw new SQLException("根据电脑编号修改电脑状态失败！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}



