package zktr.Biz;

import zktr.Dao.QuestionDao;
import zktr.Entity.QuestionEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * @author XuLuBao
 * @version V1.0
 * @Package zktr.Biz
 * @date 2020/8/22 9:32
 */
public class BIZ {
    QuestionDao qd = new QuestionDao();

    /**
     * 列出所有试题
     * @return
     * @throws Exception
     */
    public List<QuestionEntity> selectAll() throws Exception {
        List<QuestionEntity> list = qd.selectAll();
        return list;
    }

    /**
     * 按科目查询试题
     * @param subject
     * @return
     * @throws Exception
     */
    public List<QuestionEntity> selectFindSubject(int subject) throws Exception {
        List<QuestionEntity> list = qd.selectFindSubject(subject);
        return  list;
    }

    /**
     * 按题干模糊查询
     * @param question
     * @return
     */
    public List<QuestionEntity> selectLikeQuestion(String question) throws SQLException, ClassNotFoundException {
        List<QuestionEntity> list = qd.selectLikeQuestion(question);
        return list;
    }
    /**
     * 插入试题
     * @param qe
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int insertQuestion(QuestionEntity qe) throws SQLException, ClassNotFoundException {
        qe.setQuestionId(qd.getQuestionsId());
        int row = qd.insertQuestion(qe);
        return row;
    }

    /**
     * 根据题号删除试题
     * @param questionId
     * @return
     */
    public int deleteQuestion(int questionId) throws SQLException, ClassNotFoundException {
        int row = qd.deleteQuestion(questionId);
        if (row>0){
            qd.updateQuestionId(questionId);
        }
        return row;
    }

}
