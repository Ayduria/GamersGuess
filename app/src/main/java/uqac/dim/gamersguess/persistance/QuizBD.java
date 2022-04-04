package uqac.dim.gamersguess.persistance;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Question.class, Reponse.class, Score.class}, version = 1)
public abstract class QuizBD extends RoomDatabase {
    private static QuizBD INSTANCE;
    public abstract QuizDao quizDao();

    public static QuizBD getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
            Room.databaseBuilder(context, QuizBD.class, "quiz.db")
                    .createFromAsset("database/quiz.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() { INSTANCE = null; }
}