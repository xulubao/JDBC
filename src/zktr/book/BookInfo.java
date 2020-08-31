package zktr.book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 图书类
 * @author XuLuBao
 * @version V1.0
 * @package zktr.book
 * @date 2020/8/14 9:15
 */
public class BookInfo {
    BaseDao bd = new BaseDao();
    Scanner scanner = new Scanner(System.in);
    BookTest bookTest = new BookTest();


    public void bookMenu() throws Exception {
        boolean menuState = true;//程序运行状态
        int menu =0;//菜单
        System.out.println("**********请输入菜单进行选择**********");
        while (menuState){
            System.out.println("【1】图书采购\t【2】查询图书\t【3】编辑藏书名称\t【4】统计低库存书籍\t【0】返回上一层");
            menu = bookTest.isMenu(0,4);
            switch (menu){
                //图书采购
                case 1:
                    this.addBook();
                    break;
                //查询图书
                case 2:
                    System.out.print("请输入查询种类【1】查看全部\t【2】书名查询");
                    int selectbook = bookTest.isMenu(1,2);
                    this.printAllBook(selectbook);
                    break;
                //编辑藏书名称
                case 3:
                    this.alterBookName();
                    break;
                //统计低库存量
                case 4:
                    this.lowRemain();
                    break;
                default:
                    menuState=false;
                    break;
            }
            bd.close();
        }
    }

    /**
     * 定义方法一：根据书名查询图书是否存在
     *
     * @param bookName:接收一个书名
     * @return 返回该书是否存在，true表示存在，false表示不存在
     * @throws Exception
     */
    public boolean ifBook(String bookName) throws Exception {
        //编写sql语句
        String sql = "select * from book where bookname=?";
        //用于表示是否有图书，true表示有，false表示无
        boolean jg = true;
        ResultSet rs=null;
        try {
            //调用BaseDao的查询方法
            rs = bd.query(sql, bookName);
        } catch (Exception e) {
            throw new Exception("根据书名查询图书有误！");
        }
            //处理结果
            if (rs.next()) {//如果结果集有数据，则true
                jg = true;
            } else {//无数据，则false;
                jg = false;
            }
            //关闭
            bd.close();
            //返回
            return jg;

    }

    /**
     * 自动生成图书编号
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public String getBookId() throws ClassNotFoundException, SQLException {
        String id = "book";
        //编写sql
        String sql = "select max(bookid) as bookid from book";
        try {
            //调用查询
            ResultSet rs = bd.query(sql, null);
            if (rs.next()) {
                //取最大id
                String x = rs.getString("bookid");
                //如果没有最大id，则表示第一个
                if (x == null) {
                    id = "book00001";//id直接为book00001
                } else {
                    //取x最大的编号
                    x = x.substring(4);
                    //将序号转为int再加1
                    int y = Integer.parseInt(x) + 1;
                    if (y < 10) {
                        id = "book0000" + y;
                    } else if (y < 100) {
                        id = "book000" + y;
                    } else if (y < 1000) {
                        id = "book00" + y;
                    } else if (y < 10000) {
                        id = "book0" + y;
                    } else {
                        id = "book" + y;
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new SQLException("自动生成编号错误！");
        }
        return id;
    }

    /**
     * 采购图书
     */
    public void addBook() throws Exception {
        //输入信息
        String bookName = null;
        System.out.print("请输入书名:");
        while (true) {
            bookName = scanner.next();
            //要求一：书名不重复
            if (this.ifBook(bookName)) {
                System.out.println("该书已存在！请重新输入书名！");
                continue;
            } else {
                break;
            }
        }
        System.out.print("请输入数量:");
        int bookNumber = 6;
        while (true) {
            bookNumber = scanner.nextInt();
            //要求二：数量大于5
            if (bookNumber <= 5) {
                System.out.println("采购的数量不达标，请重新输入数量！");
                continue;
            } else {
                break;
            }
        }
        //要求三：添加图书
        String sql = "insert into book values(?,?,?,?,?);";
        int row = bd.update(sql, this.getBookId(), bookName, bookNumber, 0, bookNumber);
        if (row > 0) {
            System.out.println("图书采购成功！");
        } else {
            System.out.println("图书采购失败！");
        }
    }

    /**
     * 查询图书
     */
    public void printAllBook(int number) throws Exception {
        String sql=null;
        ResultSet set = null;
        if (number==1){
            sql = "select Bookid,Bookname,total,already,remain from book;";
            set = bd.query(sql,null);
        }else {
            System.out.print("请输入要查询的图书名称:");
            String bookName = scanner.next();
            if (this.ifBook(bookName)) {
                sql = "select * from book where bookname=?;";
                set = bd.query(sql,bookName);
            }else {
                System.out.println("查询失败，查询的图书未找到！");
                return;
            }
        }
            System.out.println("图书编号\t\t图书名称\t 总数量\t已借数量\t未借数量");
            while (set.next()){
                String bookId = set.getString("bookid");
                String bookName1 = set.getString("bookname");
                int total = set.getInt("total");
                int already = set.getInt("already");
                int remain = set.getInt("remain");
                System.out.println(bookId+"\t"+bookName1+"\t"+total+"\t"+already+"\t"+remain);
            }
    }

    /**
     * 根据书名判断藏书是否可借阅
     */
    public boolean ifBorrowBook(String bookname) throws Exception {
        if (!this.ifBook(bookname)) {
            System.out.println("藏书未找到！");
            return false;
        }
        //编写SQL语句
        String sql = "select remain from book where bookname = ?";
        boolean jg = true;//表示是否可以借阅，默认为true，false不可借
        try {
            ResultSet rs = bd.query(sql,bookname);
            if (rs.next()){
                int x=rs.getInt("remain");
                if (x==0){
                    //如果数量为0表示不可借阅
                    jg=false;
                    System.out.println("该图书无库存，不可借阅！");
                }else {
                    jg=true;
                }
            }
            //关闭
            bd.close();
            return jg;
        } catch (SQLException throwables) {
            throw  new SQLException("图书查询有误！");
        }
    }

    /**
     * 修改图书名称
     * @throws Exception
     */
    public void alterBookName() throws Exception {
        System.out.print("请输入要修改的图书名称:");
        String bookName=null;
        String reBookName = null;
        String sql = null;
        bookName = scanner.next();
        if (ifBook(bookName)){
            while (true){
                System.out.print("请输入新的图书名称:");
                reBookName = scanner.next();
                if (!ifBook(reBookName)){//说明在图书馆中没有现在要修改的图书名称
                    sql = "update book set bookname=? where bookname=?;";
                    break;
                }else {
                    System.out.println("输入的图书名称，图书馆中已存在，请重新输入！");
                    continue;
                }
            }
        }else {
            System.out.println("图书馆中未找到该图书，修改失败！");
        }
        int i=bd.update(sql,reBookName,bookName);
        if (i>0){
            System.out.println("名称修改成功！");
        }else {
            System.out.println("图书名称修改失败！");
        }
    }

    /**
     * 根据图书名称，修改图书数量
     */
    public void updateBookNumber(String bookname ,int number) throws SQLException {

        String sql="update book set already=already-?,remain=remain+? where bookName=?";
            try {
                bd.update(sql,number,number,bookname);
            } catch (SQLException throwables) {
                throw new SQLException("图书数量有误！");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

    }
    /**
     * 统计低库存
     */
    public void lowRemain( ) throws SQLException, ClassNotFoundException {
        String sql = "select bookid,Bookname,total,already,remain from book where remain<=5";
        ResultSet rs = bd.query(sql,null);
        if (!rs.next()){
            System.out.println("暂无低库存数据！");
            return;
        }
        System.out.println("---------------低库存书籍---------------\n图书编号\t图书名称\t\t 总数量\t已借数量\t未借数量\t");
        while (rs.next()){
            String bookId = rs.getString("bookid");
            String bookName1 = rs.getString("bookname");
            int total = rs.getInt("total");
            int already = rs.getInt("already");
            int remain = rs.getInt("remain");
            System.out.println(bookId+"\t"+bookName1+"\t"+total+"\t"+already+"\t"+remain);
        }
    }

    /**
     * 获取图书ID
     * @param bookName 图书名称
     * @return
     * @throws SQLException
     */
    public String getBookIdName(String bookName) throws SQLException {
        String sql = "select bookid from book where bookname=?";
        ResultSet rs = null;
        try {
            rs = bd.query(sql,bookName);
        } catch (SQLException throwables) {
            throw new SQLException("查询图书编号错误！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (rs.next()){
            return rs.getString("bookid");
        }return null;
    }

    /**
     * 借阅图书
     *
     * 编号自动产生                 借阅表
     * 未借数量为0不可借阅           用户表
     * 图书没有不可借阅              用户表
     * 借阅表添加                   借阅表
     * 图书表修改数量                图书表
     * 借书证表修改数量              借书证表
     * 借书证可借数量为0不可借书       借书证表
     */


}
