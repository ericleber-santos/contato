package br.com.ericleber.contatos.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.ericleber.contatos.data.db.entity.ContatoEntity

@Dao
interface ContatoDAO {
    @Insert
    suspend fun insert(contato: ContatoEntity) : Long

    @Update
    suspend fun update(contato: ContatoEntity)

    @Query("DELETE FROM contato WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM contato")
    suspend fun deleteAll()

    @Query("SELECT * FROM contato")
    suspend fun getAll() : List<ContatoEntity>
}