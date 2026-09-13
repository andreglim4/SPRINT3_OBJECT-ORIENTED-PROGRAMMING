package br.com.rodovia.dao;
 
import br.com.rodovia.db.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
 
public class IntervencaoOperacionalDAO {
 
    private static final String SQL_INSERT = "INSERT INTO TB_INTERVENCAO_OPERACIONAL (tipo_intervencao, id_equipe, id_trecho) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM TB_INTERVENCAO_OPERACIONAL";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM TB_INTERVENCAO_OPERACIONAL WHERE id_intervencao = ?";
    private static final String SQL_UPDATE = "UPDATE TB_INTERVENCAO_OPERACIONAL SET tipo_intervencao = ?, id_equipe = ?, id_trecho = ? WHERE id_intervencao = ?";
    private static final String SQL_DELETE = "DELETE FROM TB_INTERVENCAO_OPERACIONAL WHERE id_intervencao = ?";
 
    public IntervencaoOperacionalDAO() {
    }
 
    public void inserir(IntervencaoOperacionalRecord intervencao) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {
            stmt.setString(1, intervencao.tipoIntervencao());
            stmt.setInt(2, intervencao.idEquipe());
            stmt.setString(3, intervencao.idTrecho());
            stmt.execute();
            System.out.println("✅ Intervenção (" + intervencao.tipoIntervencao() + ") registrada para o trecho " + intervencao.idTrecho() + "!");
        } catch (SQLException e) {
            System.err.println("Erro ao registrar intervenção: " + e.getMessage());
        }
    }
 
    public List<IntervencaoOperacionalRecord> listarTodas() {
        List<IntervencaoOperacionalRecord> lista = new ArrayList<>();
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return lista;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
 
            while (rs.next()) {
                lista.add(new IntervencaoOperacionalRecord(
                    rs.getInt("id_intervencao"),
                    rs.getString("tipo_intervencao"),
                    rs.getInt("id_equipe"),
                    rs.getString("id_trecho"),
                    rs.getDate("data_registro")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar intervenções: " + e.getMessage());
        }
        return lista;
    }
 
    public IntervencaoOperacionalRecord buscarPorId(int idIntervencao) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return null;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            stmt.setInt(1, idIntervencao);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new IntervencaoOperacionalRecord(
                        rs.getInt("id_intervencao"),
                        rs.getString("tipo_intervencao"),
                        rs.getInt("id_equipe"),
                        rs.getString("id_trecho"),
                        rs.getDate("data_registro")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar intervenção: " + e.getMessage());
        }
        return null;
    }
 
    public void atualizar(IntervencaoOperacionalRecord intervencao) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
            stmt.setString(1, intervencao.tipoIntervencao());
            stmt.setInt(2, intervencao.idEquipe());
            stmt.setString(3, intervencao.idTrecho());
            stmt.setInt(4, intervencao.idIntervencao());
            if (stmt.executeUpdate() > 0) {
                System.out.println("✅ Intervenção ID " + intervencao.idIntervencao() + " atualizada!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar intervenção: " + e.getMessage());
        }
    }
 
    public void deletar(int idIntervencao) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
            stmt.setInt(1, idIntervencao);
            if (stmt.executeUpdate() > 0) {
                System.out.println("✅ Intervenção ID " + idIntervencao + " deletada!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar intervenção: " + e.getMessage());
        }
    }
}