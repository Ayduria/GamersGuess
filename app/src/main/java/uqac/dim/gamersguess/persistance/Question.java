package uqac.dim.gamersguess.persistance;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="questions")
public class Question {
    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @NonNull
    @ColumnInfo(name = "question")
    public String question;

    @NonNull
    @ColumnInfo(name = "difficulte")
    public String difficulte;

    public Question(@NonNull String question, @NonNull String difficulte) {
        this.question = question;
        this.difficulte = difficulte;
    }

    public Integer getQuestionId() {
        return id;
    }
}
