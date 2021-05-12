package br.com.ericleber.contatos.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "contato")
data class ContatoEntity (
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val nome: String,
        val celular: String,
        val email: String
) : Parcelable