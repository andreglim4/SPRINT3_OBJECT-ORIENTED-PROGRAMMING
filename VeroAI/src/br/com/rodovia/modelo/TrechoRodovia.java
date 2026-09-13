package br.com.rodovia.modelo;

public class TrechoRodovia {

    public static final double LIMITE_CRITICO = 30.0;

    protected String identificador;
    protected double quilometroInicial;
    protected double quilometroFinal;
    protected double nivelVegetacao;
    protected boolean isTrechoUmido; 

    public TrechoRodovia(String identificador, double kmInicial, double kmFinal, double nivelVegetacaoInicial, boolean isTrechoUmido) {
        this.identificador = identificador;
        setQuilometros(kmInicial, kmFinal);
        this.nivelVegetacao = Math.max(0, nivelVegetacaoInicial);
        this.isTrechoUmido = isTrechoUmido;
    }

    public void setQuilometros(double inicial, double fim) {
        if (inicial < 0 || fim < 0) {
            throw new IllegalArgumentException("[ERRO] Quilometragem não pode ser negativa (Informado: " + inicial + " a " + fim + ")");
        }
        if (fim <= inicial) {
            throw new IllegalArgumentException("[ERRO] O KM final deve ser maior que o inicial (Informado: " + inicial + " a " + fim + ")");
        }
        this.quilometroInicial = inicial;
        this.quilometroFinal = fim;
    }

    // Motor de crescimento inteligente (Sprint 2)
    public void simularCrescimento(int dias) {
        // Trechos úmidos crescem mais rápido (ex: 1.5cm/dia). Secos crescem menos (0.5cm/dia)
        double taxaDiaria = this.isTrechoUmido ? 1.5 : 0.5;
        this.nivelVegetacao += (taxaDiaria * dias);
    }

    public boolean estaCritico() {
        return this.nivelVegetacao >= LIMITE_CRITICO;
    }

    public String getIdentificador() { return identificador; }
    public double getQuilometroInicial() { return quilometroInicial; }
    public double getQuilometroFinal() { return quilometroFinal; }
    public double getNivelVegetacao() { return nivelVegetacao; }

    public void setNivelVegetacao(double nivelVegetacao) {
        this.nivelVegetacao = Math.max(0, nivelVegetacao);
    }

    @Override
    public String toString() {
        double extensao = quilometroFinal - quilometroInicial;
        String status = estaCritico() ? " [CRÍTICO]" : " [OK]";
        String clima = isTrechoUmido ? "Úmido" : "Seco";

        return String.format(
            "%s (KM %.1f ao %.1f | Ext: %.1fkm | Clima: %s) | Vegetação: %.2f/%.0f cm%s",
            identificador, quilometroInicial, quilometroFinal, extensao, clima, nivelVegetacao, LIMITE_CRITICO, status
        );
    }
}
