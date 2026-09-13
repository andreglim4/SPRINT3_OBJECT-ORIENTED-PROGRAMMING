package br.com.rodovia.dao;
 
import br.com.rodovia.db.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
public class EquipeManutencaoDAO {
 
    private static final String SQL_INSERT = "INSERT INTO TB_EQUIPE_MANUTENCAO (nome_equipe) VALUES (?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM TB_EQUIPE_MANUTENCAO";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM TB_EQUIPE_MANUTENCAO WHERE id_equipe = ?";
    private static final String SQL_UPDATE = "UPDATE TB_EQUIPE_MANUTENCAO SET nome_equipe = ? WHERE id_equipe = ?";
    private static final String SQL_DELETE = "DELETE FROM TB_EQUIPE_MANUTENCAO WHERE id_equipe = ?";
 
    public EquipeManutencaoDAO() {
    }
 
    public void inserir(EquipeManutencaoRecord equipe) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {
            stmt.setString(1, equipe.nomeEquipe());
            stmt.execute();
            System.out.println("✅ Equipe '" + equipe.nomeEquipe() + "' cadastrada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir equipe: " + e.getMessage());
        }
    }
 
    public List<EquipeManutencaoRecord> listarTodas() {
        List<EquipeManutencaoRecord> lista = new ArrayList<>();
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return lista;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
 
            while (rs.next()) {
                lista.add(new EquipeManutencaoRecord(
                    rs.getInt("id_equipe"),
                    rs.getString("nome_equipe")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar equipes: " + e.getMessage());
        }
        return lista;
    }
 
    public EquipeManutencaoRecord buscarPorId(int idEquipe) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return null;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            stmt.setInt(1, idEquipe);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EquipeManutencaoRecord(
                        rs.getInt("id_equipe"),
                        rs.getString("nome_equipe")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar equipe: " + e.getMessage());
        }
        return null;
    }
 
    public void atualizar(EquipeManutencaoRecord equipe) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
            stmt.setString(1, equipe.nomeEquipe());
            stmt.setInt(2, equipe.idEquipe());
            if (stmt.executeUpdate() > 0) {
                System.out.println("✅ Equipe ID " + equipe.idEquipe() + " atualizada!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar equipe: " + e.getMessage());
        }
    }
 
    public void deletar(int idEquipe) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
            stmt.setInt(1, idEquipe);
            if (stmt.executeUpdate() > 0) {
                System.out.println("✅ Equipe ID " + idEquipe + " deletada!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar equipe: " + e.getMessage());
        }
    }
}