package uqac.dim.gamersguess.persistance;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.File;

@Database(entities = {Question.class, Reponse.class, Score.class}, version = 1)
public abstract class QuizBD extends RoomDatabase {
    private static QuizBD INSTANCE;
    public abstract QuizDao quizDao();

    public static QuizBD getDatabase(Context context) {
        if (!doesDatabaseExist(context)) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context, QuizBD.class, "quiz.db")
                                .createFromAsset("database/quiz.db")
                                .allowMainThreadQueries()
                                .fallbackToDestructiveMigration()
                                .build();
            }
        } else {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context, QuizBD.class, "quiz.db")
                                .allowMainThreadQueries()
                                .fallbackToDestructiveMigration()
                                .build();
            }
        }
        return INSTANCE;
    }

    private static boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath("quiz.db");
        return dbFile.exists();
    }

    public static void destroyInstance() { INSTANCE = null; }
}