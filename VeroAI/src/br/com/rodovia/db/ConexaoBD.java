package br.com.rodovia.db;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
public class ConexaoBD {
 
    // Instância única da classe (Padrão Singleton)
    private static ConexaoBD instancia;
    // Objeto que guarda a conexão ativa
    private Connection conexao;
 
    // Credenciais do banco Oracle FIAP
    private static final String HOST = "oracle.fiap.com.br";
    private static final String PORT = "1521";
    private static final String SID = "ORCL";
    private static final String USER = "seu_rm";    // TODO: Substitua pelo seu RM (ex: rm12345)
    private static final String PASSWORD = "sua_senha"; // TODO: Substitua pela sua senha (ex: 050308)
 
    // Construtor privado impede que a classe seja instanciada com "new" fora daqui
    private ConexaoBD() {
    }
 
    // Método para obter a instância única do Singleton
    public static ConexaoBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoBD();
        }
        return instancia;
    }
 
    // Método para abrir a conexão
    public Connection conectar() {
        try {
            // Se a conexão já existir e estiver aberta, reaproveita
            if (conexao != null && !conexao.isClosed()) {
                return conexao;
            }
 
            // Registra o Driver do Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");
 
            // Monta a string de conexão
            String url = "jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + SID;
 
            // Estabelece a conexão com o banco
            conexao = DriverManager.getConnection(url, USER, PASSWORD);
            System.out.println("✅ [SUCESSO] Conectado ao Oracle FIAP!");
            return conexao;
 
        } catch (ClassNotFoundException e) {
            System.err.println("❌ [ERRO] Driver OJDBC não encontrado. Verifique se o ojdbc17.jar está no Classpath.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("❌ [ERRO SQL] Falha ao conectar. Verifique RM, Senha ou se está na rede/VPN da FIAP.");
            System.err.println("Detalhe: " + e.getMessage());
            return null;
        }
    }
 
    // Método para fechar a conexão
    public void desconectar() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("🔌 [INFO] Conexão com o banco fechada.");
            }
        } catch (SQLException e) {
            System.err.println("❌ [ERRO] Falha ao fechar a conexão: " + e.getMessage());
        }
    }
}