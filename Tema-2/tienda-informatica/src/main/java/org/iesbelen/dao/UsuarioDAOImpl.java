package org.iesbelen.dao;

import org.iesbelen.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAOImpl extends AbstractDAOImpl implements UsuarioDAO {


    @Override
    public void create(Usuario usuario) {
        String sql = "INSERT INTO usuario (usuario, password, rol) VALUES (?, ?, ?)";

        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;

            ps.setString(index++, usuario.getUsuario());
            ps.setString(index++, usuario.getPassword());
            ps.setString(index, usuario.getRol());
            ps.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Usuario> getAll() {

        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarioList = new ArrayList<>();

        try (Connection conn = connectDB();
             Statement s = conn.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = crearUsuario(rs);
                usuarioList.add(usuario);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return usuarioList;
    }

    @Override
    public Optional<Usuario> find(int id) {

        String sql = "SELECT * FROM usuario WHEN id = ?";

        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = crearUsuario(rs);
                    return Optional.of(usuario);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }


    @Override
    public void update(Usuario usuario) {

        String sql = """
                UPDATE usuario
                SET 
                    id = ?,
                    usuario = ?,
                    password = ?,
                    rol = ?
                WHERE id = ?
                """;

        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;

            ps.setInt(index++, usuario.getId());
            ps.setString(index++, usuario.getUsuario());
            ps.setString(index++, usuario.getPassword());
            ps.setString(index++, usuario.getRol());
            ps.setInt(index, usuario.getId());

            ps.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(int id) {

        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private Usuario crearUsuario(ResultSet rs) throws SQLException {
        int index = 1;

        Usuario usuario = new Usuario();

        usuario.setId(rs.getInt(index++));
        usuario.setUsuario(rs.getString(index++));
        usuario.setPassword(rs.getString(index++));
        usuario.setRol(rs.getString(index));

        return usuario;
    }
}