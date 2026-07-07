/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import config.DatabaseException;
import config.Conexion;
import config.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    public Empleado Validar(String user, String password) {
        String sql = "select IdEmpleado, Dni, Nombres, Telefono, Estado, User, PasswordHash, Rol from empleado where User=? and Estado='Activo'";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empleado em = map(rs);
                    if (PasswordUtil.verify(password, em.getPasswordHash())) {
                        return em;
                    }
                }
            }
            return new Empleado();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo validar el usuario.", e);
        }
    }

    public List<Empleado> listar() {
        String sql = "select IdEmpleado, Dni, Nombres, Telefono, Estado, User, PasswordHash, Rol from empleado order by IdEmpleado";
        List<Empleado> lista = new ArrayList<>();
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(map(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo listar empleados.", e);
        }
    }

    public void agregar(Empleado em, String password) {
        String sql = "insert into empleado(Dni, Nombres, Telefono, Estado, User, PasswordHash, Rol) values(?,?,?,?,?,?,?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, em.getDni());
            ps.setString(2, em.getNom());
            ps.setString(3, em.getTel());
            ps.setString(4, em.getEstado());
            ps.setString(5, em.getUser());
            ps.setString(6, PasswordUtil.hash(password));
            ps.setString(7, em.getRol());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo agregar empleado.", e);
        }
    }

    public Empleado listarId(int id) {
        String sql = "select IdEmpleado, Dni, Nombres, Telefono, Estado, User, PasswordHash, Rol from empleado where IdEmpleado=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
            return new Empleado();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo buscar empleado.", e);
        }
    }

    public void actualizar(Empleado em, String password) {
        boolean updatePassword = password != null && !password.trim().isEmpty();
        String sql = updatePassword
                ? "update empleado set Dni=?, Nombres=?, Telefono=?, Estado=?, User=?, PasswordHash=?, Rol=? where IdEmpleado=?"
                : "update empleado set Dni=?, Nombres=?, Telefono=?, Estado=?, User=?, Rol=? where IdEmpleado=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, em.getDni());
            ps.setString(2, em.getNom());
            ps.setString(3, em.getTel());
            ps.setString(4, em.getEstado());
            ps.setString(5, em.getUser());
            if (updatePassword) {
                ps.setString(6, PasswordUtil.hash(password));
                ps.setString(7, em.getRol());
                ps.setInt(8, em.getId());
            } else {
                ps.setString(6, em.getRol());
                ps.setInt(7, em.getId());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo actualizar empleado.", e);
        }
    }

    public void delete(int id) {
        String sql = "update empleado set Estado='Inactivo' where IdEmpleado=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo desactivar empleado.", e);
        }
    }

    private Empleado map(ResultSet rs) throws SQLException {
        Empleado em = new Empleado();
        em.setId(rs.getInt("IdEmpleado"));
        em.setDni(rs.getString("Dni"));
        em.setNom(rs.getString("Nombres"));
        em.setTel(rs.getString("Telefono"));
        em.setEstado(rs.getString("Estado"));
        em.setUser(rs.getString("User"));
        em.setPasswordHash(rs.getString("PasswordHash"));
        em.setRol(rs.getString("Rol"));
        return em;
    }
}
