package zktr.cybercaf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

/**
 * 上下机管理
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.cybercaf
 * @date 2020/8/17 16:07
 */
public class UpandDown {
    Scanner scanner = new Scanner(System.in);
    Tool tool = new Tool();
    Member mem = new Member();
    Employee emp = new Employee();
    Computer computer = new Computer();
    BaseDao bd = new BaseDao();
    ResultSet rs = null;

    public void UDMenu() throws SQLException, ClassNotFoundException {
        if (!emp.isEmpState()){//点用员工类判断是否登陆
            System.out.println("管理员未登陆！");
            return;
        }
        boolean stateMenu = true;
        while (stateMenu){
            System.out.println("【1】上机\t【2】下机\t【3】查看空机\t【4】查看正在上网费用\t【0】返回上一层");
            int menu = tool.isMenu(0,4);
            switch (menu){
                case 1:
                    this.onComputer(emp.getEmpId());
                    break;
                case 2:
                    this.offComputer(emp.getEmpId());
                    break;
                case 3:
                    computer.selectCpt(3);
                    break;
                case 4:
                    this.findUpMoney();
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
     * 上机
     * 会员id是否可用—会员表
     * 会员id是否正在上机----查上下机表：id的下机时间是否为空
     * 电脑是否正在使用----电脑表的状态是否可用
     * 上机
     * 上下机添加数据
     * 电脑状态改为已用
     */
    public void onComputer(String empId) throws SQLException, ClassNotFoundException {
        System.out.print("请输入用户名：");
        String memname = scanner.next();
        if (!mem.checkMemName(memname,2)){//判断是否是会员表里的会员
            System.out.println("请先注册会员！");
            return;
        }
        if (this.findBymemid(memname)){//判断会员是否正在上机
            System.out.println("会员正在上机，一个会员只能上机一台！");
            return;
        }
        System.out.print("请输入电脑编号：");
        String cptId = scanner.next();
        if (!computer.findByCptid(cptId,3)){
            System.out.println("输入的电脑编号错误！");
            return;
        }
        if (computer.findByCptid(cptId,0)){//查看电脑状态是否可用(运用的是相反的，即该状态是否可用)
            System.out.println("该电脑正有会员正在上机，暂不能上机！");
            return;
        }
        int row=0;
        try{
            String memid=null;
            String sql = "select memid from member where memname=?";
            rs = bd.query(sql,memname);
            if (rs.next()){
                memid=rs.getString("memid");
            }
            sql = "insert into upanddown (udid,memid,cptid,`update`,empid1) values (?,?,?,NOW(),?);";
            row = bd.update(sql,this.getUdId(),memid,cptId,empId);
        }catch (SQLException S){
            System.out.println(S.getMessage());
            throw new SQLException("插入上机数据库sql语句错误！");
        }
        if (row>0){
            computer.alterComState(cptId,1);
            System.out.println("上机成功成功！");
        }else {
            System.out.println("上机失败！未添加入上机表！");
        }
    }

    /**
     * 下机
     * 算出消费金额，把机器改为可用
     */
    public void offComputer(String empId) throws SQLException, ClassNotFoundException {
        System.out.print("请输入会员姓名:");
        String memname = scanner.next();
        String memid =null;//会员编号
        float grade =0.0f;//会员等级消费金额/h
        String cptId = null;//电脑编号
        float money=0.0f;//消费金额
        String sql;//SQL语句
        float upDownDate = 0.0f;//上机时间
        if (mem.checkMemName(memname,1)){
            System.out.println("该用户没有注册会员，无法上机下机！");//直接判断是否是会员表里状态可用的会员
            return;
        }
        //通过会员姓名去上下机表里判断是否正在上机
        if (!this.findBymemid(memname)){
            System.out.println("会员没有上机，无需下机！");
            return;
        }
        sql = "select member.memid as memid,member.grade as grade,cptid,TIMESTAMPDIFF(MINUTE,`update`,now())/60 as hour\n" +
                "from member inner join upanddown on upanddown.memid=member.memid where member.memname=? and upanddown.downdate is null;";
        rs = bd.query(sql,memname);
        if (rs.next()){
            grade = rs.getInt("grade");//根据会员姓名获取会员等级
            grade = grade==1?2:grade==2?1.5f:1;//判断等级获取每小时消费金额
            cptId = rs.getString("cptid");//通过上下机表查询到机器号，用于修改电脑状态
            //根据数据库函数，获取到上机时间到现在的小时数
            upDownDate = rs.getFloat("hour");
            memid=rs.getString("memid");
        }
        money = grade*upDownDate;//根据上机时间乘会员消费等级算出金额
        //判断完成之后说明输入的会员正在上机，进行下机操作
        sql = "update upanddown set downdate=NOW(),money=?,empid2=? where memid=? and downdate is null;";
        int row = bd.update(sql,money,empId,memid);
        if (row>0){
            upDownDate = (float)(Math.round(upDownDate*100))/100;//小时精确到两位小数
            money = (float)(Math.round(money*100))/100;//将金额转换为两位小数
            System.out.println("您一共上了"+upDownDate+"小时，消费"+money+"元，请去前台消费！");
            computer.alterComState(cptId,0);
            System.out.println("下机成功！");
        }else {
            System.out.println("下机失败！");
        }
    }

    /**
     * 查询正在上机的费用
     */
    public void findUpMoney() throws SQLException, ClassNotFoundException {
        float grade =0.0f;//会员等级消费金额/h
        String grades = null;//会员等级
        float money=0.0f;//消费金额
        String sql;//SQL语句
        float upDownDate = 0.0f;//上机时间
        System.out.println("会员姓名\t电脑编号\t时间/小时\t\t\t上机时间\t\t\t\t\t\t会员等级\t消费金额");
        //查询正在上机的会员姓名，电脑编号，会员等级,上机时间，小时数
        sql = "select memname,upanddown.cptid as cptid,grade,`update`,TIMESTAMPDIFF(MINUTE,`update`,now())/60 as hour from member inner join upanddown on member.memid=upanddown.memid inner join computer on computer.cptid=upanddown.cptid where downdate is null;";
        rs = bd.query(sql);
        while (rs.next()){
            String memname = rs.getString("memname");//会员姓名
            String cptId = rs.getString("cptid");//电脑编号
            Timestamp upDate = rs.getTimestamp("update");//上机时间
            grade = rs.getInt("grade");
            grades = grade==1?"普通会员":rs.getInt("grade")==2?"高级会员":"VIP会员";//转换为可视会员等级
            grade = grade==1?2:grade==2?1.5f:1;//判断等级获取每小时消费金额
            float hour = rs.getFloat("hour");//上机的小时数
            hour = (float)(Math.round(hour*100))/100;//小时精确到两位小数
            money = hour * grade;
            money = (float)(Math.round(money*100))/100;//将金额转换为两位小数
            System.out.println(memname+"\t\t"+cptId+"\t"+hour+"\t\t\t"+upDate.toString()+"\t\t"+grades+"\t"+money+" \t正在上机");
        }
        if (grade==0.0f){
            System.out.println("当前没有人在上机！");
        }

    }



    /**
     * 自动获取上下机编号
     * @return 上下机编号
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String getUdId() throws SQLException, ClassNotFoundException {
        java.util.Date date = new java.util.Date();
        int year = date.getYear()+1900;
        int mouth = date.getMonth()+1;
        int day = date.getDay();
        String udId="ud"+year+(mouth<10?"0"+mouth:mouth)+day;
        String sql = "select max(udid) as udid from upanddown where udid like (?);";
        rs = bd.query(sql,udId+"%");
        if (rs.next()){
            String x=rs.getString("udid");
            if (x==null){
                //当如果获取的月份小于10时，显示的是'9'不是’09‘要在前面添加一个'0'
                udId=udId+"001";
            }else {
                x=x.substring(10);
                int i=Integer.parseInt(x)+1;
                if (i<10){
                    udId+="00"+i;
                }else if (i<100){
                    udId+="0"+i;
                }else {
                    udId+=i;
                }
            }
        }
        return udId;
    }

    /**
     * 根据 会员名称查看是否正在上机
     * @param memname
     * @return
     */
    public boolean findBymemid(String memname) throws SQLException, ClassNotFoundException {
        //再根据会员姓名查看上下机表中是否存在未下机的该编号
        String sql = "select udid from upanddown inner join member on upanddown.memid=member.memid where memname=? and downdate is null;";
        try {
            rs = bd.query(sql,memname);
        }catch (SQLException se){
            throw new SQLException("查看会员姓名是否正在上机sql语句错误！");
        }
        boolean checkUp = false;//是否上机
        if (rs.next()){
            checkUp=true;//有数据代表正在上机
        }
        return checkUp;
    }
}
