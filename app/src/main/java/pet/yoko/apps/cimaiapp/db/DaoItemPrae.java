package pet.yoko.apps.cimaiapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoItemPrae {

    @Query("SELECT * FROM ItemPrae ORDER BY ano")
    List<ItemPrae> getAll();

    @Query("DELETE FROM ItemPrae")
    void delete_all();

    @Insert
    void insert(ItemPrae itemPrae);

}
