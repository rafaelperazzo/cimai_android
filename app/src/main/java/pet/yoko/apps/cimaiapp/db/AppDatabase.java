package pet.yoko.apps.cimaiapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ItemPrae.class, ItemPrograd.class},version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DaoItemPrae itemPraeDao();
    public abstract DaoItemPrograd itemProgradDao();

}
