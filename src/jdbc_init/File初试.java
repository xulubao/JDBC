package jdbc_init;

import java.io.*;

/**
 * @author XuLuBao
 * @version V1.0
 * @Package jdbc_init
 * @date 2020/8/26 23:06
 */
public class File初试 {
    public static void main(String[] args) throws IOException {
        File file=new File("F:/中科韬睿/JSP/FILE/1.txt");
        File file2=new File("F:/中科韬睿/JSP/FILE/2.txt");
        File file3=new File("F:/中科韬睿/JSP/FILE/合并.txt");
        FileInputStream fis=new  FileInputStream(file);
        FileInputStream fis2=new FileInputStream(file2);
        byte[] bs=new byte[(int)file.length()];
        byte[] bs2=new byte[(int)file2.length()];
        fis.read(bs);
        fis2.read(bs2);
        FileOutputStream fos=new FileOutputStream(file3);
        fos.write(bs);
        fos.write(bs2);
        fis.close();
        fis2.close();
        fos.close();
    }

}
