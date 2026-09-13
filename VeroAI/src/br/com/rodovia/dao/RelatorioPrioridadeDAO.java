package br.com.rodovia.dao;
 
import br.com.rodovia.db.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
public class RelatorioPrioridadeDAO {
 
    private static final String SQL_INSERT = "INSERT INTO TB_RELATORIO_PRIORIDADE (qt_urgente, qt_critico, qt_atencao, qt_normal, resumo) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM TB_RELATORIO_PRIORIDADE";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM TB_RELATORIO_PRIORIDADE WHERE id_relatorio = ?";
    private static final String SQL_UPDATE = "UPDATE TB_RELATORIO_PRIORIDADE SET qt_urgente = ?, qt_critico = ?, qt_atencao = ?, qt_normal = ?, resumo = ? WHERE id_relatorio = ?";
    private static final String SQL_DELETE = "DELETE FROM TB_RELATORIO_PRIORIDADE WHERE id_relatorio = ?";
 
    public RelatorioPrioridadeDAO() {
    }
 
    public void inserir(RelatorioPrioridadeRecord relatorio) {
        salvarRelatorio(
            relatorio.qtUrgente(), 
            relatorio.qtCritico(), 
            relatorio.qtAtencao(), 
            relatorio.qtNormal(), 
            relatorio.resumo()
        );
    }
 
    public void salvarRelatorio(int qtUrgente, int qtCritico, int qtAtencao, int qtNormal, String resumo) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {
            stmt.setInt(1, qtUrgente);
            stmt.setInt(2, qtCritico);
            stmt.setInt(3, qtAtencao);
            stmt.setInt(4, qtNormal);
            stmt.setString(5, resumo);
            stmt.execute();
            System.out.println("✅ Relatório de prioridades salvo no banco de dados!");
        } catch (SQLException e) {
            System.err.println("Erro ao salvar relatório: " + e.getMessage());
        }
    }
 
    public List<RelatorioPrioridadeRecord> listarTodas() {
        List<RelatorioPrioridadeRecord> lista = new ArrayList<>();
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return lista;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
 
            while (rs.next()) {
                lista.add(new RelatorioPrioridadeRecord(
                    rs.getInt("id_relatorio"),
                    rs.getInt("qt_urgente"),
                    rs.getInt("qt_critico"),
                    rs.getInt("qt_atencao"),
                    rs.getInt("qt_normal"),
                    rs.getString("resumo"),
                    rs.getDate("data_geracao")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar relatórios: " + e.getMessage());
        }
        return lista;
    }
 
    public RelatorioPrioridadeRecord buscarPorId(int idRelatorio) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return null;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            stmt.setInt(1, idRelatorio);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new RelatorioPrioridadeRecord(
                        rs.getInt("id_relatorio"),
                        rs.getInt("qt_urgente"),
                        rs.getInt("qt_critico"),
                        rs.getInt("qt_atencao"),
                        rs.getInt("qt_normal"),
                        rs.getString("resumo"),
                        rs.getDate("data_geracao")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar relatório: " + e.getMessage());
        }
        return null;
    }
 
    public void atualizar(RelatorioPrioridadeRecord relatorio) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
            stmt.setInt(1, relatorio.qtUrgente());
            stmt.setInt(2, relatorio.qtCritico());
            stmt.setInt(3, relatorio.qtAtencao());
            stmt.setInt(4, relatorio.qtNormal());
            stmt.setString(5, relatorio.resumo());
            stmt.setInt(6, relatorio.idRelatorio());
            if (stmt.executeUpdate() > 0) {
                System.out.println("✅ Relatório ID " + relatorio.idRelatorio() + " atualizado!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar relatório: " + e.getMessage());
        }
    }
 
    public void deletar(int idRelatorio) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
            stmt.setInt(1, idRelatorio);
            if (stmt.executeUpdate() > 0) {
                System.out.println("✅ Relatório ID " + idRelatorio + " deletado!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar relatório: " + e.getMessage());
        }
    }
}
