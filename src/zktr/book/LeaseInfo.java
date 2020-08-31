package zktr.book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

/**
 * 借阅表
 *
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.book
 * @date 2020/8/14 11:16
 */
public class LeaseInfo {
    Scanner scanner = new Scanner(System.in);
    BaseDao bd = new BaseDao();
    Employee employee = new Employee();
    LibraryCard lc = new LibraryCard();
    BookInfo bookInfo = new BookInfo();
    BookTest bookTest = new BookTest();

    public void leaseMenu() throws Exception {
        //管理员登陆了可以借阅跟归还
        if (employee.isEmpState()) {
        } else {
            System.out.println("管理员未登陆，不可进行租借图书！");
            return;
        }
        boolean menuState = true;//程序运行状态
        int menu = 0;//菜单

        System.out.println("**********请输入菜单进行选择**********");
        while (menuState) {
            System.out.println("【1】借阅图书\t【2】归还图书\t【0】返回上一层");
            menu = bookTest.isMenu(0, 4);
            switch (menu) {
                //借阅图书
                case 1:
                    this.borrowBook(employee.getEmpId());
                    bd.close();
                    break;
                case 2:
                    this.returnBook(employee.getEmpId());
                    bd.close();
                    break;
                default:
                    menuState = false;
                    break;
            }
        }
    }

    /**
     * 自动生成借阅编号
     */
    public String getCardInfo() throws Exception {
        //获取当前系统日期
        Date date = new Date();
        //取年\月\日
        int year = date.getYear() + 1900;//因为取出来的是两位数的，所以要加1900
        int mouth = date.getMonth() + 1;
        int day = date.getDay();
        String id = "lease" + year + (mouth<10?"0"+mouth:mouth) + day;
        String sql = "select max(leaseid) as id from lease where leaseid like ?";
        ResultSet rs = bd.query(sql, id + "%");
        if (rs.next()) {
            String x = rs.getString("id");//这里上面查询的时候取的别名是id，所以用id而不用leaseid
            if (x == null) {
                id = id + "001";
            } else {
                x = x.substring(13);
                int y = Integer.parseInt(x) + 1;
                if (y < 10) {
                    id = id + "00" + y;
                } else if (y < 100) {
                    id = id + "0" + y;
                } else {
                    id = id + y;
                }
            }
        }
        return id;
    }


    /**
     * 借阅图书
     */
    public void borrowBook(String empID) throws Exception {
        System.out.print("请输入读者姓名:");
        String cardName = scanner.next();
        if (!lc.ifCard(cardName)) {//如果为false，表示不可借，true表示可借
            System.out.println("该借书证不可借阅！");
            return;
        }//true表示可借
        System.out.print("请输入图书名称:");
        String bookname = scanner.next();
        //验证要求1：判断借书证是否可借
        if (!bookInfo.ifBorrowBook(bookname)) {
            return;
        }
        //前面都没有问题可以借阅
        //添加借阅表操作
        String sql = "insert into lease(leaseid,Cardid,bookid,borrowdate,Empid1) values(?,?,?,NOW(),?);";
        try {
            bd.update(sql, this.getCardInfo(), lc.getCardID(cardName), bookInfo.getBookIdName(bookname), empID);
        } catch (SQLException s) {
            throw new SQLException("添加失败！");
        }
        lc.updateBookNumber(cardName, -1);//修改借书证已借数量
        bookInfo.updateBookNumber(bookname, -1);//修改藏书已借数量
        System.out.println("借阅成功！");
    }

    public void returnBook(String empId) throws SQLException, ClassNotFoundException {
        String sql = "select leaseid,name,bookname,borrowdate from lease inner join librarycard on lease.Cardid=librarycard.Cardid inner join book on  lease.bookid=book.Bookid where returndate is null;";
        ResultSet rs = bd.query(sql, null);
        System.out.println("借阅编号\t\t读者姓名\t\t藏书名称\t\t借出时间");
        while (rs.next()) {
            String leaseid = rs.getString("leaseid");
            String name = rs.getString("name");
            String bookname = rs.getString("bookname");
            Date date = rs.getDate("borrowdate");
            System.out.println(leaseid + "\t" + name + "\t" + bookname + "\t" + date.toString());
        }
        if (rs.isAfterLast() == rs.isBeforeFirst()) {
            System.out.println("暂未有借出的书籍！");
            return;
        }
        String leaseid = null;
        while (true) {
            System.out.print("请输入要归还的借阅编号:");
            leaseid = scanner.next();
            if ("0".equals(leaseid)) {
                return;
            } else if (!this.checkLeaseId(leaseid)) {
                System.out.println("借阅编号未在系统中找到，请重新输入(【0】返回上一层):");
                continue;
            } else {
                //说明输入的借阅编号正确，进行归还
                break;
            }
        }
        sql = "select name,bookname from lease inner join librarycard on lease.Cardid=librarycard.Cardid inner join book on  lease.bookid=book.Bookid where leaseid=? and returndate is null";
        rs = bd.query(sql, leaseid);
        String name = null;//通过租借表查询出来要归还此本书的读者姓名
        String bookname = null;//通过租借表查询出来要归还此本书的藏书名称
        if (rs.next()) {
            name = rs.getString("name");
            bookname = rs.getString("bookname");
        }
        lc.updateBookNumber(name, 1);//修改借书证已借数量
        bookInfo.updateBookNumber(bookname, 1);//修改藏书已借数量
        sql = "update lease set returndate=NOW(),empid2=? where leaseid=?;";
        bd.update(sql, empId, leaseid);
        System.out.println("归还成功！");
    }

    /**
     * 借阅表是否有检查编号
     *
     * @param leaseid 要从借阅表中查询的编号
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean checkLeaseId(String leaseid) throws SQLException, ClassNotFoundException {
        String sql = "select * from lease where leaseid=?";
        ResultSet rs = bd.query(sql, leaseid);
        //查询到了数据代表借阅表中有借阅编号
        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }
}
