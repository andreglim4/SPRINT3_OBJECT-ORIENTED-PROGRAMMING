package br.com.rodovia.dao;
 
public record TrechoRodoviaRecord(
    String identificador, 
    double kmInicial, 
    double kmFinal, 
    double nivelVegetacao, 
    boolean isUmido, 
    boolean isIot
) {}