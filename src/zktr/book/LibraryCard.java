package zktr.book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.book
 * @date 2020/8/14 10:58
 */
public class LibraryCard {
    BaseDao baseDao = new BaseDao();
    Employee emp = new Employee();
    BookTest bookTest = new BookTest();
    Scanner scanner = new Scanner(System.in);

    public void cardMenu() throws Exception {
        boolean menuState = true;//程序运行状态
        int menu =0;//菜单
        System.out.println("**********请输入菜单进行选择**********");
        while (menuState){
            System.out.println("【1】办理借书证\t【2】查询借书证\t【3】编辑借书证\t【4】注销借书证\t【0】返回上一层");
            menu = bookTest.isMenu(0,4);
            switch (menu){
                //注册借书证
                case 1:
                    this.addLibraryCare();
                    break;
                //查询借书证
                case 2:
                    this.selectState();
                    break;
                //编辑借书证
                case 3:
                    System.out.print("请输入读者姓名:");
                    String cardname = scanner.next();
                    this.alterLibraryCard(cardname);
                    break;
                //注销借书证
                case 4:
                    System.out.print("请输入要注销的读者姓名:");
                    String name = scanner.next();
                    this.outState(name);
                    break;
                default:
                    menuState=false;
                    break;
            }
        }
        baseDao.close();
    }


    /**
     * 生成借书证ID
     * @return
     */
    public String getCardId() throws SQLException, ClassNotFoundException {
        String cardId = "card";
        String sql = "select max(cardid) as cardid from libraryCard;";
        ResultSet rs = baseDao.query(sql,null);
        try{
        if (rs.next()){
            String cardid2 = rs.getString("cardid");
            if (cardid2==null){
                cardId = cardId+"00001";
            }else{
                cardid2 = cardid2.substring(4); //将序号转为int再加1
                int y = Integer.parseInt(cardid2) + 1;
                if (y < 10) {
                    cardId = cardId+ "0000" + y;
                } else if (y < 100) {
                    cardId = cardId+"000" + y;
                } else if (y < 1000) {
                    cardId = cardId+"00" + y;
                } else if (y < 10000) {
                    cardId =cardId+ "0" + y;
                } else {
                    cardId = cardId + y;
                }
            }
        }}catch (SQLException SE){
            throw  new SQLException("产生借书证编号错误！");
        }
        return cardId;
    }

    /**
     * 办理借书证
     */
    public void addLibraryCare() throws SQLException, ClassNotFoundException {
        String CardName;//读者姓名
        System.out.print("请输入读者姓名:");
        CardName = scanner.next();
        if (this.checkCardName(CardName)){
            System.out.println("借书证系统中已存在该读者姓名，办理失败！");
            return;
        }
        System.out.print("请输入可借数量:");
        int totalNumber = 3;
        totalNumber = bookTest.isMenu(0,5);
        String sql = "insert into libraryCard(Cardid,name,state,number,totalNumber) values(?,?,?,?,?)";
        int row = baseDao.update(sql,this.getCardId(),CardName,1,0,totalNumber);
        if (row>0){
            System.out.println("借书证注册成功！");
        }else {
            System.out.println("借书证注册失败！");
        }
        baseDao.close();
    }

    /**
     * 查询借书证：1表示查看注销的借书证
     * 2、表示查看可用的借书证
     * 3、表示查看全部借书证
     */
    public void selectState() throws SQLException, ClassNotFoundException {
        System.out.print("请输入要查看借书证的状态[1、注销 2、可用 3、全部]:");
        String state = scanner.next();
        if ("1".equals(state)||"2".equals(state)||"3".equals(state)){
            String sql = "select * from libraryCard where state!=?";
            ResultSet rs = baseDao.query(sql,Integer.parseInt(state));
            System.out.println("借书证编号\t读者姓名\t借书证状态\t可借数量\t已借数量");
            while (rs.next()){
                String cardid = rs.getString("Cardid");
                String name = rs.getString("name");
                String state1 = rs.getInt("state")==1?"可用":"注销";
                int number = rs.getInt("number");
                int totalNumber = rs.getInt("totalNumber");
                System.out.println(cardid+"\t"+name+"\t\t"+state1+"\t\t\t"+number+"\t\t\t"+totalNumber);
            }
            baseDao.close();
        }else {
            System.out.println("输入的状态错误,请重新输入!");
            this.selectState();
        }
    }

    /**
     * 修改借书证信息：根据编号修改
     */
    public void alterLibraryCard(String name) throws SQLException, ClassNotFoundException {
        String sql = "select * from LibraryCard where name=? and state=1";
        ResultSet rs = baseDao.query(sql,name);
        if (!rs.next()){
            System.out.println("该读者未找到或已注销，不可编辑！");
            return;
        }
        System.out.print("请输入要修改的借书证信息【1、修改姓名 2、修改可借数量 3、注销】");
        int x=bookTest.isMenu(1,3);
        switch (x){
            case 1:
                while(true){
                    System.out.print("请输入要修改的姓名:");
                    String name1 = scanner.next();
                    if(this.checkCardName(name1)){
                        System.out.println("借书系统已经存在该读者姓名,请重新输入！");
                        continue;
                    }else {
                        try {
                            sql = "update libraryCard set name=? where name=? and state!=2;";
                            baseDao.update(sql, name1, name);
                        }catch (SQLException S){
                            throw new SQLException("修改借书证读者姓名失败！");
                        }
                        baseDao.close();
                        break;
                    }
                }
                System.out.println("修改成功！");
                break;
            case 2:
                System.out.print("请输入修改的数量【0-5】");
                int totalNumber=3;
                totalNumber = bookTest.isMenu(0,5);
                            sql = "select number,totalNumber from libraryCard where name=?";
                            rs = baseDao.query(sql,name);
                            if (rs.next()){
                                int number = rs.getInt("number");
                                if (number<=totalNumber){
                                    sql = "update libraryCard set totalNumber=? where name=?";
                                    baseDao.update(sql,totalNumber,name);
                                    System.out.println("修改可借数量成功！");
                                }else{
                                    System.out.println("输入的可借数量小于已借数量，请先还书之后再修改可借数量！修改失败");
                                }
                            }else {
                                System.out.println("未在系统中找到该用户！");
                            }
                            break;
            case 3:
                this.outState(name);//调用注销借书证方法
                break;
            default:
                System.out.println("输入错误,返回到上一层!");
                break;
        }
    }


    /**
     * 根据借书证名称判断该借书证是否可以借书
     */
    public boolean ifCard(String cardname) throws SQLException, ClassNotFoundException {
        if (!checkCardName(cardname)) {
            System.out.println("读者姓名未找到！");
            return false;
        }
        String sql = "select totalNumber from LibraryCard where state=1 and name=?;";
        ResultSet rs = baseDao.query(sql,cardname);
        boolean jg = false;
        try {
            if (rs.next()) {
                int x = rs.getInt("totalNumber");
                if (x == 0) {
                    jg = false;
                } else {
                    jg = true;
                }
            }
            baseDao.close();
        }catch (SQLException SE){
            throw new SQLException("查询借书证有误！");
        }
        return jg;
    }

    /**
     * 根据借书证读者姓名修改数量
     * @param cardname
     */
    public void updateBookNumber(String cardname,int i) throws SQLException, ClassNotFoundException {
        String sql = "update LibraryCard set number=number-?,totalNumber=totalNumber+? where name=? and state!=2;";
        try {
            baseDao.update(sql,i,i, cardname);
        }catch (SQLException SE){
            throw new SQLException("修改图书数量有误！");
        }
    }

    /**
     * 根据名字查看借书证姓名是否有重名
     * @param cardname
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean checkCardName(String cardname) throws SQLException, ClassNotFoundException {
        String sql = "select count(name) as name from LibraryCard where name=? and state=1;";//查询借书证中是否有重复可用的姓名
        ResultSet rs = baseDao.query(sql,cardname);
        if (rs.next()){
            int count = rs.getInt("name");
            if (count>0){
                return true;
            }else if (count==0){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取借书证编号
     * @param cardName 读者姓名
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String getCardID(String cardName) throws SQLException, ClassNotFoundException {
        String sql = "select cardid from librarycard where name=? and state=1;";
        ResultSet rs = baseDao.query(sql,cardName);
        if (rs.next()){
            return rs.getString("cardid");

        }else {
            return null;
        }
    }

    /**
     * 注销借书证
     * @param name 借书证编号
     * @throws SQLException
     */
    public void outState(String name) throws SQLException, ClassNotFoundException {
       String sql1 = "select number from libraryCard where name=?;";
       ResultSet rs = baseDao.query(sql1,name);
       if (rs.next()){
           int number = rs.getInt("number");
           if (number>0){
               System.out.println("该读者还有未归还的书籍，请归还后再进行注销！");
               return;
           }
       }
        String sql = "update libraryCard set state=2 where name=? and state=1;";
        try {
            int i=baseDao.update(sql,name);
            if (i>0){
                System.out.println("注销成功!");
            }else {
                System.out.println("该用户未注册借书证！");
            }
        } catch (SQLException throwables) {
            throw new SQLException("注销借书证代码错误！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
