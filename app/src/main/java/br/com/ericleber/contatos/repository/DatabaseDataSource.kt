package br.com.ericleber.contatos.repository

import br.com.ericleber.contatos.data.db.dao.ContatoDAO
import br.com.ericleber.contatos.data.db.entity.ContatoEntity

class DatabaseDataSource (
    private val contatoDAO: ContatoDAO

): ContatoRepository {

    override suspend fun insertContato(name: String, celular: String, email: String): Long {
        val contato = ContatoEntity(
            nome = name,
            celular = celular,
            email = email
        )

        return contatoDAO.insert(contato)
    }

    override suspend fun updateContato(id: Long, name: String, celular: String, email: String) {
        val contato = ContatoEntity(
            id=id,
            nome = name,
            celular = celular,
            email = email
        )

        contatoDAO.update(contato)
    }

    override suspend fun deleteContato(id: Long) {
        contatoDAO.delete(id)
    }

    override suspend fun deleteAllContatos() {
        contatoDAO.deleteAll()
    }

    override suspend fun getAllContatos(): List<ContatoEntity> {
        return contatoDAO.getAll()
    }
}