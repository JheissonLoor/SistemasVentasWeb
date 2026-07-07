package Modelo;

import config.Conexion;
import config.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public Cliente buscar(String dni) {
        String sql = "select IdCliente, Dni, Nombres, Direccion, Estado from cliente where Dni=? and Estado='Activo'";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
            return new Cliente();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo buscar cliente.", e);
        }
    }

    public List<Cliente> listar() {
        String sql = "select IdCliente, Dni, Nombres, Direccion, Estado from cliente order by IdCliente desc";
        List<Cliente> lista = new ArrayList<>();
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(map(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo listar clientes.", e);
        }
    }

    public Cliente listarId(int id) {
        String sql = "select IdCliente, Dni, Nombres, Direccion, Estado from cliente where IdCliente=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
            return new Cliente();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo buscar cliente.", e);
        }
    }

    public void agregar(Cliente c) {
        String sql = "insert into cliente(Dni, Nombres, Direccion, Estado) values(?,?,?,?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getDni());
            ps.setString(2, c.getNom());
            ps.setString(3, c.getDir());
            ps.setString(4, c.getEstado());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo agregar cliente.", e);
        }
    }

    public void actualizar(Cliente c) {
        String sql = "update cliente set Dni=?, Nombres=?, Direccion=?, Estado=? where IdCliente=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getDni());
            ps.setString(2, c.getNom());
            ps.setString(3, c.getDir());
            ps.setString(4, c.getEstado());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo actualizar cliente.", e);
        }
    }

    public void delete(int id) {
        String sql = "update cliente set Estado='Inactivo' where IdCliente=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo desactivar cliente.", e);
        }
    }

    private Cliente map(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("IdCliente"));
        c.setDni(rs.getString("Dni"));
        c.setNom(rs.getString("Nombres"));
        c.setDir(rs.getString("Direccion"));
        c.setEstado(rs.getString("Estado"));
        c.setEs(rs.getString("Estado"));
        return c;
    }
}
