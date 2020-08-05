package pet.yoko.apps.cimaiapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoItemPrograd {

    @Query("SELECT * FROM ItemPrograd ORDER BY ano")
    List<ItemPrograd> getAll();

    @Query("DELETE FROM ItemPrograd")
    void delete_all();

    @Insert
    void insert(ItemPrograd itemPrograd);

}
