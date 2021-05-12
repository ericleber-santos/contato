package br.com.ericleber.contatos.repository

import br.com.ericleber.contatos.data.db.entity.ContatoEntity

interface ContatoRepository {

    suspend fun insertContato(name: String, celular: String, email: String) : Long

    suspend fun updateContato(id: Long, name: String, celular: String, email: String)

    suspend fun deleteContato(id: Long)

    suspend fun deleteAllContatos()

    suspend fun getAllContatos() : List<ContatoEntity>
}