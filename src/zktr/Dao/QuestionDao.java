package zktr.Dao;

import zktr.Entity.QuestionEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.Dao
 * @date 2020/8/21 17:33
 */
public class QuestionDao {
    BaseDao bd = new BaseDao();
    ResultSet rs = null;
    /**
     * 列出所有试题
     * @return
     */
    public List<QuestionEntity> selectAll() throws Exception {
        List<QuestionEntity> list = new ArrayList<QuestionEntity>();
        String sql = "select * from question_info";
        try{
        rs = bd.query(sql,null);
        while (rs.next()){
            QuestionEntity qe = new QuestionEntity();
            qe.setQuestionId(rs.getInt("questionId"));
            qe.setQuestion(rs.getString("question"));
            qe.setOptionA(rs.getString("optionA"));
            qe.setOptionB(rs.getString("optionB"));
            qe.setOptionC(rs.getString("optionC"));
            qe.setOptionD(rs.getString("optionD"));
            qe.setSubject(rs.getInt("subject"));
            qe.setAnswer(rs.getString("answer"));
            list.add(qe);
        }
        }catch (Exception e){
            throw new Exception("列出所有试题错误！");
        }
        return list;
    }

    /**
     * 按科目查询
     * @return
     */
    public List<QuestionEntity> selectFindSubject(int subject) throws Exception {
        String sql = "select * from question_info where subject=?";
        List<QuestionEntity> list = new ArrayList<QuestionEntity>();
        try{
        rs = bd.query(sql,subject);
        while (rs.next()){
            QuestionEntity qe = new QuestionEntity();
            qe.setQuestionId(rs.getInt("questionId"));
            qe.setQuestion(rs.getString("question"));
            qe.setOptionA(rs.getString("optionA"));
            qe.setOptionB(rs.getString("optionB"));
            qe.setOptionC(rs.getString("optionC"));
            qe.setOptionD(rs.getString("optionD"));
            qe.setSubject(rs.getInt("subject"));
            qe.setAnswer(rs.getString("answer"));
            list.add(qe);
        }
        }catch (Exception e){
            throw new Exception("按科目查询错误！");
        }
        return list;
    }

    /**
     * 按题干模糊查询
     * @param question
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<QuestionEntity> selectLikeQuestion(String question) throws SQLException, ClassNotFoundException {
        String sql = "select * from question_info where question LIKE ?";
        List<QuestionEntity> list = new ArrayList<QuestionEntity>();
        try {
            rs = bd.query(sql, "%"+question+"%");
            while (rs.next()) {
                QuestionEntity qe = new QuestionEntity();
                qe.setQuestionId(rs.getInt("questionId"));
                qe.setQuestion(rs.getString("question"));
                qe.setOptionA(rs.getString("optionA"));
                qe.setOptionB(rs.getString("optionB"));
                qe.setOptionC(rs.getString("optionC"));
                qe.setOptionD(rs.getString("optionD"));
                qe.setSubject(rs.getInt("subject"));
                qe.setAnswer(rs.getString("answer"));
                list.add(qe);
            }
        }catch (SQLException E){
            throw new SQLException("按题干模糊查询出错误！");
        }
        return list;
    }

    /**
     * 添加试题
     * @param qe
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int insertQuestion(QuestionEntity qe) throws SQLException, ClassNotFoundException {
        String sql = "insert into question_info value(?,?,?,?,?,?,?,?);";
        int row=0;
        try {
            row = bd.update(sql, qe.getQuestionId(), qe.getQuestion(), qe.getOptionA(), qe.getOptionB(), qe.getOptionC(), qe.getOptionD(), qe.getSubject(), qe.getAnswer());
        }catch (SQLException e){
            throw new SQLException("添加试题错误！");
        }
        return row;
    }

    /**
     * 根据试题编号删除试题
     * @param questionId
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int deleteQuestion(int questionId) throws SQLException, ClassNotFoundException {
        String sql = "delete from question_info where questionId=?;";
        int row;
        try {
            row = bd.update(sql, questionId);
        }catch (SQLException e){
            throw new SQLException("删除试题错误！");
        }
        return row;
    }


    /**
     * 获取最大题号
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int getQuestionsId() throws SQLException, ClassNotFoundException {
        String sql = "select max(questionId) as questionId from question_info";
        int x=0;
        try {
            ResultSet rs = bd.query(sql, null);
            if (rs.next()) {
                x = rs.getInt("questionId");
                if (x == 0) {
                    x = 1;
                } else {
                    x = x + 1;
                }
            }
        }catch (SQLException E){
            throw new SQLException("获取最大编号错误！");
        }
        return x;
    }

    public int updateQuestionId(int questionId) throws SQLException, ClassNotFoundException {
        String sql = "update question_info set questionId=questionId-1 where questionId>?";
        int row = bd.update(sql,questionId);
        return row;
    }


}
