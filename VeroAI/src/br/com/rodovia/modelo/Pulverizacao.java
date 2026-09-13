package br.com.rodovia.modelo;

public class Pulverizacao extends IntervencaoOperacional {

    public Pulverizacao(String equipeResponsavel) {
        super(equipeResponsavel);
    }

    @Override
    public void executarServico(TrechoRodovia trecho) {
        System.out.println(">>> [PULVERIZAÇÃO] Equipe " + equipeResponsavel + " aplicando controle químico no trecho: " + trecho.getIdentificador());
        trecho.setNivelVegetacao(5.0); // Pulverização não zera, apenas controla o crescimento
        System.out.println("    Status atualizado: Vegetação reduzida e controlada para " + trecho.getNivelVegetacao() + "cm.\n");
    }
}