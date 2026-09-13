package br.com.rodovia.dao;
 
import java.sql.Date;
 
public record RelatorioPrioridadeRecord(
    int idRelatorio,
    int qtUrgente,
    int qtCritico,
    int qtAtencao,
    int qtNormal,
    String resumo,
    Date dataGeracao
) {}