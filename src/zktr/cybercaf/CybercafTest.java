package zktr.cybercaf;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * 网吧系统
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.cybercaf
 * @date 2020/8/17 11:00
 */
public class CybercafTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Employee emp = new Employee();//员工管理
        Computer cpt = new Computer();//电脑管理
        Member mem = new Member();//会员管理
        UpandDown upandDown = new UpandDown();//上下机管理
        Tool tool = new Tool();//工具类
        Scanner scanner = new Scanner(System.in);
        boolean menuState = true;//程序运行状态
        int menu =0;//菜单
        System.out.println("**********欢迎进入网吧管理系统1.0版本**********");
        while (menuState){
            System.out.println("【1】电脑管理\t\t【2】会员管理管理\t\t【3】上下机管理\t【4】员工管理\t【5】登陆\t【6】退出登陆\t【0】退出程序");
            menu=tool.isMenu(0,6);
            switch (menu){
                case 1:
                    cpt.cptMenu();
                    break;
                case 2:
                    mem.meMenu();
                    break;
                case 3:
                    upandDown.UDMenu();
                    break;
                case 4:
                    emp.menuEmp();
                    break;
                case 5:
                    emp.loginEmployee();
                    break;
                case 6:
                    emp.outEmployee();
                    break;
                default:
                    menuState=false;
                    System.exit(0);
                    break;
            }

        }
    }
}
