package Modelo;

import config.Conexion;
import config.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {

    public Cliente buscar(String dni) {
        String sql = "select IdCliente, Dni, Nombres, Direccion, Estado from cliente where Dni=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId(rs.getInt("IdCliente"));
                    c.setDni(rs.getString("Dni"));
                    c.setNom(rs.getString("Nombres"));
                    c.setDir(rs.getString("Direccion"));
                    c.setEs(rs.getString("Estado"));
                    return c;
                }
            }
            return new Cliente();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo buscar cliente.", e);
        }
    }
}
