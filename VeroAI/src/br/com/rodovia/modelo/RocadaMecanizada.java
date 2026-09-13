package br.com.rodovia.modelo;

public class RocadaMecanizada extends IntervencaoOperacional {

    public RocadaMecanizada(String equipeResponsavel) {
        super(equipeResponsavel);
    }

    @Override
    public void executarServico(TrechoRodovia trecho) {
        System.out.println(">>> [ROÇADA MECANIZADA] Equipe " + equipeResponsavel + " atuando pesadamente no trecho: " + trecho.getIdentificador());
        trecho.setNivelVegetacao(0);
        System.out.println("    Status atualizado: Vegetação cortada para " + trecho.getNivelVegetacao() + "cm.\n");
    }
}