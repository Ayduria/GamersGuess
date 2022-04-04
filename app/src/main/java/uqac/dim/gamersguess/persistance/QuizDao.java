package uqac.dim.gamersguess.persistance;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizDao {
    @Query("SELECT * FROM questions WHERE difficulte = 'f'")
    List<Question> getEasyQuestions();

    @Query("SELECT * FROM questions WHERE difficulte = 'm'")
    List<Question> getMediumQuestions();

    @Query("SELECT * FROM questions WHERE difficulte = 'd'")
    List<Question> getHardQuestions();

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    Question findQuestionById(int id);

    @Query("SELECT * FROM reponses WHERE questionID = :questionID")
    List<Reponse> loadAllQuestionAnswers(int questionID);

    @Query("SELECT * FROM scores ORDER BY score DESC")
    List<Score> getAllScores();

    @Query("delete from scores")
    void deleteScores();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addScore(Score score);

    @Delete
    void delete(Score score);
}
