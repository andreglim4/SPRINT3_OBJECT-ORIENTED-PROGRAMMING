package br.com.rodovia.servico;
 
import br.com.rodovia.modelo.*;
import br.com.rodovia.dao.RelatorioPrioridadeDAO;
import java.util.List;
 
public class GerenciadorPrioridade {
 
    public void gerarRelatorioEProcessar(List<TrechoRodovia> trechos) {
        System.out.println("\n=== RELATÓRIO DE PRIORIDADE E AÇÃO (MOTOR DE REGRAS) ===");
        int qtUrgente = 0;
        int qtCritico = 0;
        int qtAtencao = 0;
        int qtNormal = 0;
        int intervencoesFeitas = 0;
 
        for (TrechoRodovia trecho : trechos) {
            double leituraAtual = trecho.getNivelVegetacao();
 
            // Interação com IoT (Interface)
            if (trecho instanceof MonitoravelViaIoT) {
                leituraAtual = ((MonitoravelViaIoT) trecho).transmitirDadosSensor();
                System.out.print("[SENSOR IoT ATUALIZADO] ");
            }
 
            System.out.println("Analisando: " + trecho.getIdentificador() + " | Leitura: " + leituraAtual + "cm");

            if (leituraAtual >= 45.0) {
                qtUrgente++;
            } else if (leituraAtual >= TrechoRodovia.LIMITE_CRITICO) {
                qtCritico++;
            } else if (leituraAtual >= 15.0) {
                qtAtencao++;
            } else {
                qtNormal++;
            }

            if (trecho.estaCritico()) {
                intervencoesFeitas++;
                IntervencaoOperacional acao;
                if (leituraAtual >= 45.0) {
                    acao = new RocadaMecanizada("Tracto-Alpha");
                } else {
                    acao = new Pulverizacao("Química-Beta");
                }
                acao.executarServico(trecho);
            }
        }
        System.out.println("=== FIM DO RELATÓRIO NO CONSOLE ===\n");
 
        // ---------------------------------------------------------
        // Salvar o resultado no banco de dados!
        // ---------------------------------------------------------
        String resumo = "Análise concluída em " + trechos.size() + " trechos. Total de intervenções executadas: " + intervencoesFeitas + ".";
        RelatorioPrioridadeDAO dao = new RelatorioPrioridadeDAO();
        dao.salvarRelatorio(qtUrgente, qtCritico, qtAtencao, qtNormal, resumo);
    }
}