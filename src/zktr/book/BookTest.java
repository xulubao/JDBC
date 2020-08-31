package zktr.book;

import java.util.Scanner;

/**
 * 图书管理系统
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.book
 * @date 2020/8/15 18:57
 */
public class BookTest {
    public static void main(String[] args) throws Exception {
        Employee emp = new Employee();//员工管理
        BookInfo book = new BookInfo();//图书管理
        LibraryCard card = new LibraryCard();//借书证管理
        LeaseInfo lease = new LeaseInfo();//租借管理
        BookTest bookTest = new BookTest();
        Scanner scanner = new Scanner(System.in);
        boolean menuState = true;//程序运行状态
        int menu =0;//菜单
        System.out.println("**********欢迎进入中科韬睿图书管理系统1.0版本**********");
        while (menuState){
            System.out.println("【1】图书管理\t\t【2】借书证管理\t【3】租借图书\t【4】员工管理\t【0】退出程序");
            menu = bookTest.isMenu(0,4);
            switch (menu){
                //图书管理
                case 1:
                    book.bookMenu();
                    break;
                //借书证管理
                case 2:
                    card.cardMenu();
                    break;
                //租借图书
                case 3:
                    lease.leaseMenu();
                    break;
                //员工管理
                case 4:
                    emp.menuEmp();
                    break;
                default:
                    menuState=false;
                    BaseDao baseDao = new BaseDao();
                    baseDao.close();
                    System.exit(0);
                    break;
            }

        }
    }

    /**
     * 获取用户选择菜单选项
     * @param start 开始的选项数
     * @param end 末尾的选项数
     * @return
     */
    public int isMenu(int start,int end){
        Scanner scanner = new Scanner(System.in);
        int menu =0;
        while (true){
            if (scanner.hasNextInt()){
                menu = scanner.nextInt();
                if (menu<start||menu>end){
                    System.out.print("输入的选项只能在【"+start+","+end+"】之间:");
                    continue;
                }else{
                    break;
                }
            }else {
                scanner.next();
                System.out.println("输入菜单选项错误，只能输入数字！");
                continue;
            }
        }
        return menu;
    }
}
