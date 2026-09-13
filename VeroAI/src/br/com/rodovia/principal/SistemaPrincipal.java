package br.com.rodovia.principal;
 
import br.com.rodovia.db.ConexaoBD;
import br.com.rodovia.dao.*;
import br.com.rodovia.modelo.*;
import br.com.rodovia.servico.GerenciadorPrioridade;
 
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
 
public class SistemaPrincipal {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" INICIANDO SISTEMA MOTIVA - SPRINT 3 ");
        System.out.println("==================================================\n");
 
        // 1. Testar conexão 
        System.out.println("1. Testando conexão com o banco...");
        ConexaoBD banco = ConexaoBD.getInstancia();
        Connection conn = banco.conectar();
 
        if (conn == null) {
            System.out.println("❌ Parando o sistema, o banco não conectou.");
            System.out.println("💡 Lembrete: Execute este código na rede da FIAP ou com a VPN ligada.");
            return;
        }
 
        try {
            EquipeManutencaoDAO daoEquipe = new EquipeManutencaoDAO();
            TrechoRodoviaDAO daoTrecho = new TrechoRodoviaDAO();
            IntervencaoOperacionalDAO daoIntervencao = new IntervencaoOperacionalDAO();
            RelatorioPrioridadeDAO daoRelatorio = new RelatorioPrioridadeDAO();
 
            // 2. Testar CRUD de Equipe
            System.out.println("\n--- 2. TESTANDO CRUD DE EQUIPES ---");
            String nomeEquipeTeste = "Equipe-Teste-" + System.currentTimeMillis();
            daoEquipe.inserir(new EquipeManutencaoRecord(0, nomeEquipeTeste));
            System.out.println("Listando Equipes cadastradas no banco:");
            daoEquipe.listarTodas().forEach(e -> System.out.println(" - ID " + e.idEquipe() + ": " + e.nomeEquipe()));
 
            // 3. Testar CRUD de Trechos
            System.out.println("\n--- 3. TESTANDO CRUD DE TRECHOS ---");
            String idTrechoTeste = "Trecho-Teste-ABC";
            daoTrecho.inserir(new TrechoRodoviaRecord(idTrechoTeste, 0.0, 10.0, 5.0, false, false));
            TrechoRodoviaRecord trechoBuscado = daoTrecho.buscarPorId(idTrechoTeste);
            System.out.println("Buscando trecho recém inserido: " + (trechoBuscado != null ? trechoBuscado.identificador() : "Falha na busca"));
 
            // 4. Testar Intervenções
            System.out.println("\n--- CARREGANDO DADOS DO BANCO PARA O SISTEMA ---");
            List<TrechoRodoviaRecord> registrosBanco = daoTrecho.listarTodas();
            List<TrechoRodovia> malhaViaria = new ArrayList<>();

            for (TrechoRodoviaRecord rec : registrosBanco) {
                if (rec.isIot()) {
                    malhaViaria.add(new TrechoIoT(rec.identificador(), rec.kmInicial(), rec.kmFinal(), rec.nivelVegetacao(), rec.isUmido()));
                } else {
                    malhaViaria.add(new TrechoRodovia(rec.identificador(), rec.kmInicial(), rec.kmFinal(), rec.nivelVegetacao(), rec.isUmido()));
                }
            }
 
            // Simulação de Crescimento 
            System.out.println("\n--- APÓS 15 DIAS (Simulação de Crescimento) ---");
            for(TrechoRodovia trecho : malhaViaria) {
                trecho.simularCrescimento(15);
                System.out.println(trecho.toString());
            }
 
            // 5. Gerar relatório com persistência
            System.out.println("\n--- 5. GERANDO RELATÓRIO E SALVANDO NO BANCO ---");
            GerenciadorPrioridade motorRegras = new GerenciadorPrioridade();
            motorRegras.gerarRelatorioEProcessar(malhaViaria);
 
            // 6. Consultar histórico de relatórios
            System.out.println("\n--- 6. CONSULTANDO HISTÓRICO DE RELATÓRIOS (DO BANCO) ---");
            List<RelatorioPrioridadeRecord> historico = daoRelatorio.listarTodas();
            for (RelatorioPrioridadeRecord rel : historico) {
                System.out.println(" - Relatório ID: " + rel.idRelatorio() + " | Data: " + rel.dataGeracao() + " | Resumo: " + rel.resumo());
            }
            // Limpeza do trecho de teste para não poluir o banco nas próximas execuções
            daoTrecho.deletar(idTrechoTeste);
 
            // Mantendo os testes unitários manuais
            System.out.println("\n\n==================================================");
            System.out.println(" ÁREA TÉCNICA: TESTES DE ARQUITETURA");
            System.out.println("==================================================");
            System.out.print("Testando bloqueio de instanciacao (Classe Abstrata)... ");
            if (Modifier.isAbstract(IntervencaoOperacional.class.getModifiers())) {
                System.out.println("[TESTE OK]");
            } else {
                System.out.println("[TESTE FALHA]");
            }
 
            System.out.print("Testando captura de dados com Mock IoT... ");
            MonitoravelViaIoT droneSimuladoMock = new MonitoravelViaIoT() {
                public double transmitirDadosSensor() { return 45.5; }
            };
            if (droneSimuladoMock.transmitirDadosSensor() == 45.5) {
                System.out.println("[TESTE OK]");
            } else {
                System.out.println("[TESTE FALHA]");
            }
 
        } catch (Exception e) {
            System.err.println("\n[FALHA NO SISTEMA]: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 7. Fechar conexão
            banco.desconectar();
            System.out.println("\n🔌 Sistema encerrado em segurança.");
        }
    }
}