package br.com.rodovia.modelo;

public class TrechoIoT extends TrechoRodovia implements MonitoravelViaIoT {

    public TrechoIoT(String identificador, double kmInicial, double kmFinal, double nivelVegetacaoInicial, boolean isTrechoUmido) {
        super(identificador, kmInicial, kmFinal, nivelVegetacaoInicial, isTrechoUmido);
    }

    public double transmitirDadosSensor() {
        return this.nivelVegetacao;
    }

    // Validação do comportamento (Teste Unitário Manual)
    public static void main(String[] args) {
        // Instanciação de um Mock via classe anônima
        MonitoravelViaIoT droneSimuladoMock = new MonitoravelViaIoT() {
            public double transmitirDadosSensor() {
                return 45.5; 
            }
        };

        // Captura e validação dos dados
        double alturaCapturada = droneSimuladoMock.transmitirDadosSensor();

        if (alturaCapturada == 45.5) {
            System.out.println("[TESTE OK] Mock implementado corretamente e dados capturados.");
        } else {
            System.out.println("[TESTE FALHA] Os dados capturados não correspondem à simulação.");
        }
    }
}