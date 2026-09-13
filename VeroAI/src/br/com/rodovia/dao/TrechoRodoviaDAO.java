package br.com.rodovia.dao;
 
import br.com.rodovia.db.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
public class TrechoRodoviaDAO {
 
    // Queries SQL como constantes
    private static final String SQL_INSERT = "INSERT INTO TB_TRECHO_RODOVIA (identificador, km_inicial, km_final, nivel_vegetacao, is_umido, is_iot) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM TB_TRECHO_RODOVIA";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM TB_TRECHO_RODOVIA WHERE identificador = ?";
    private static final String SQL_UPDATE = "UPDATE TB_TRECHO_RODOVIA SET km_inicial = ?, km_final = ?, nivel_vegetacao = ?, is_umido = ?, is_iot = ? WHERE identificador = ?";
    private static final String SQL_DELETE = "DELETE FROM TB_TRECHO_RODOVIA WHERE identificador = ?";
 
    // Construtor padrão
    public TrechoRodoviaDAO() {
    }
 
    public void inserir(TrechoRodoviaRecord trecho) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        // O try-with-resources já fecha o PreparedStatement automaticamente
        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {
            stmt.setString(1, trecho.identificador());
            stmt.setDouble(2, trecho.kmInicial());
            stmt.setDouble(3, trecho.kmFinal());
            stmt.setDouble(4, trecho.nivelVegetacao());
            stmt.setInt(5, trecho.isUmido() ? 1 : 0); 
            stmt.setInt(6, trecho.isIot() ? 1 : 0);
 
            stmt.execute();
            System.out.println("✅ Trecho " + trecho.identificador() + " inserido no banco com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir trecho: " + e.getMessage());
        }
    }
 
    public List<TrechoRodoviaRecord> listarTodas() {
        List<TrechoRodoviaRecord> lista = new ArrayList<>();
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return lista;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
 
            while (rs.next()) {
                TrechoRodoviaRecord trecho = new TrechoRodoviaRecord(
                    rs.getString("identificador"),
                    rs.getDouble("km_inicial"),
                    rs.getDouble("km_final"),
                    rs.getDouble("nivel_vegetacao"),
                    rs.getInt("is_umido") == 1,
                    rs.getInt("is_iot") == 1
                );
                lista.add(trecho);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar trechos: " + e.getMessage());
        }
        return lista;
    }
 
    public TrechoRodoviaRecord buscarPorId(String identificador) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return null;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            stmt.setString(1, identificador);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TrechoRodoviaRecord(
                        rs.getString("identificador"),
                        rs.getDouble("km_inicial"),
                        rs.getDouble("km_final"),
                        rs.getDouble("nivel_vegetacao"),
                        rs.getInt("is_umido") == 1,
                        rs.getInt("is_iot") == 1
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar trecho por ID: " + e.getMessage());
        }
        return null;
    }
 
    public void atualizar(TrechoRodoviaRecord trecho) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
            stmt.setDouble(1, trecho.kmInicial());
            stmt.setDouble(2, trecho.kmFinal());
            stmt.setDouble(3, trecho.nivelVegetacao());
            stmt.setInt(4, trecho.isUmido() ? 1 : 0);
            stmt.setInt(5, trecho.isIot() ? 1 : 0);
            stmt.setString(6, trecho.identificador());
 
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("✅ Trecho " + trecho.identificador() + " atualizado no banco!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar trecho: " + e.getMessage());
        }
    }
 
    public void deletar(String identificador) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        if (conn == null) return;
 
        try (PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
            stmt.setString(1, identificador);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("✅ Trecho " + identificador + " deletado do banco!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar trecho: " + e.getMessage());
        }
    }
}
