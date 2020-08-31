package zktr.Entity;

/**
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.Entity
 * @date 2020/8/21 17:27
 */
public class QuestionEntity {
    public QuestionEntity() {
    }

    public QuestionEntity(int questionId, String qusetion, String optionA, String optionB, String optionC, String optionD, int subject, String answer) {
        this.questionId = questionId;
        this.question = qusetion;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.subject = subject;
        this.answer = answer;
    }

    private int questionId;//试题编号
    private String question;//试题题干
    private String optionA;//选项A
    private String optionB;//选项B
    private String optionC;//选项C
    private String optionD;//选项D
    private int subject;//科目[【1】代表java【2】代表C#【3】代表JSP]
    private String answer;//正确答案

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return questionId +
                "、" + question + '\n' +
                "\t\t\t选项A:" + optionA + '\n' +
                "\t\t\t选项B:" + optionB + '\n' +
                "\t\t\t选项C:" + optionC + '\n' +
                "\t\t\t选项D:" + optionD + '\n' +
                "\t\t\t答案:" + answer;
    }
}
