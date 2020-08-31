package zktr.View;

import zktr.Biz.BIZ;
import zktr.Entity.QuestionEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.View
 * @date 2020/8/21 17:45
 */
public class QuestionView {
    Scanner scanner = new Scanner(System.in);
    BIZ biz = new BIZ();
    QuestionEntity qe = new QuestionEntity();
    public void questionMenu(){
        System.out.println("************欢迎使用试题管理系统************");
        while (true){
            System.out.println("请选择操作【1】列出所有试题 【2】按科目查询 【3】按题干模糊查询 【4】添加试题 【5】删除试题 【6】退出系统");
            switch (scanner.nextInt()){
                case 1:
                    this.selectAll();
                    break;
                case 2:
                    this.selectSubjectAll();
                    break;
                case 3:
                    this.selectQuestionAll();
                    break;
                case 4:
                    this.insertQuestion();
                    break;
                case 5:
                    this.delectQuestionId();
                    break;
                case 6:
                    System.exit(0);
                    break;
            }
        }
    }
    public void selectAll(){
        List<QuestionEntity> list = new ArrayList<QuestionEntity>();
        try {
            list = biz.selectAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i).toString());
        }
    }
    public void selectSubjectAll(){
        List<QuestionEntity> list = new ArrayList<QuestionEntity>();
        System.out.print("请输入要查询的科目【1】Java 【2】C# 【3】JSP  :");
        int subject = scanner.nextInt();
        try {
            list = biz.selectFindSubject(subject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        for (int i=0;i<list.size();i++){
            System.out.println(list.get(i).toString());
        }
    }
    public void selectQuestionAll(){
        System.out.print("请输入题干:");
        String question = scanner.next();
        List<QuestionEntity> list;
        try {
            list = biz.selectLikeQuestion(question);
            for (int i=0;i<list.size();i++){
                System.out.println(list.get(i).toString());
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void insertQuestion(){
        System.out.print("请输入科目【1】Java【2】C#【3】JSP :");
        qe.setSubject(scanner.nextInt());
        System.out.print("请输入题干：");
        qe.setQuestion(scanner.next());
        System.out.print("请输入选项A：");
        qe.setOptionA(scanner.next());
        System.out.print("请输入选项B：");
        qe.setOptionB(scanner.next());
        System.out.print("请输入选项C：");
        qe.setOptionC(scanner.next());
        System.out.print("请输入选项D：");
        qe.setOptionD(scanner.next());
        System.out.print("请输入答案：");
        qe.setAnswer(scanner.next());
        try {
            int row =  biz.insertQuestion(qe);
            if (row>0){
                System.out.println("添加新试题成功！");
            }else {
                System.out.println("添加新试题失败！");
            }

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void delectQuestionId(){
        System.out.print("请输入需要删除的试题编号：");
        try {
            int row = biz.deleteQuestion(scanner.nextInt());
            if (row>0){
                System.out.println("删除成功！");
            }else {
                System.out.println("对不起，没有你要删除的编号！");
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
