package org.example.ecochef.dao;

import java.util.List;

public interface Dao<T> {
    void guardar(T t);
    void actualizar(T t);
    void eliminar(int id);
    T buscarPorId(int id);
    List<T> listarTodos();
}