package br.com.rodovia.dao;
 
import java.sql.Date;
 
public record IntervencaoOperacionalRecord(
    int idIntervencao,
    String tipoIntervencao,
    int idEquipe,
    String idTrecho,
    Date dataRegistro
) {}