package uqac.dim.gamersguess.persistance;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName="reponses", foreignKeys = {
        @ForeignKey(
                entity = Question.class,
                parentColumns = "id",
                childColumns = "questionID",
                onDelete = ForeignKey.CASCADE
        )
})

public class Reponse {
    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @NonNull
    @ColumnInfo(name = "questionID")
    public Integer questionID;

    @NonNull
    @ColumnInfo(name = "choixReponse")
    public String choixReponse;

    @NonNull
    @ColumnInfo(name = "bonneReponse")
    public Integer bonneReponse;

    public Reponse(@NonNull Integer questionID, @NonNull String choixReponse, @NonNull Integer bonneReponse) {
        this.questionID = questionID;
        this.choixReponse = choixReponse;
        this.bonneReponse = bonneReponse;
    }
}
