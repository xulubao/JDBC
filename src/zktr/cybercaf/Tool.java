package zktr.cybercaf;

import java.util.Scanner;

/**
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.cybercaf
 * @date 2020/8/17 11:06
 */
public class Tool {
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
}
