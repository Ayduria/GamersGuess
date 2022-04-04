package uqac.dim.gamersguess.persistance;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="scores")
public class Score {
    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @NonNull
    @ColumnInfo(name = "score")
    public Integer score;

    @NonNull
    @ColumnInfo(name = "nom")
    public String nom;

    public Score(@NonNull Integer score, @NonNull String nom) {
        this.score = score;
        this.nom = nom;
    }
}
