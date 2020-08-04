package pet.yoko.apps.cimaiapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ItemPrae.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DaoItemPrae itemPraeDao();

}
