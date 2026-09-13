package br.com.rodovia.modelo;

import java.lang.reflect.Modifier;

public abstract class IntervencaoOperacional {
    protected String equipeResponsavel;

    public IntervencaoOperacional(String equipeResponsavel) {
        this.equipeResponsavel = equipeResponsavel;
    }

    public abstract void executarServico(TrechoRodovia trecho);

    // Validação da estrutura (Teste Unitário Manual)
    public static void main(String[] args) {
        // Verifica os modificadores da classe para garantir a abstração
        int modificadores = IntervencaoOperacional.class.getModifiers();
        boolean ehAbstrata = Modifier.isAbstract(modificadores);

        if (ehAbstrata) {
            System.out.println("[TESTE OK] A classe IntervencaoOperacional é abstrata e impede instanciação.");
        } else {
            System.out.println("[TESTE FALHA] A classe permite o uso do operador 'new'.");
        }
    }
}