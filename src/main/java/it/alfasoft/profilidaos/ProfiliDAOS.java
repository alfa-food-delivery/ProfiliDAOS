package it.alfasoft.profilidaos;

import it.alfasoft.daosimple.DaoException;
import it.alfasoft.daosimple.DaoImpl;
import it.alfasoft.utentidaos.RuoliDAOS;
import it.alfasoft.utentidaos.Ruolo;
import it.alfasoft.utentidaos.Utente;
import it.alfasoft.utentidaos.UtentiDAOS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class ProfiliDAOS extends DaoImpl<Profilo,Integer> {

    public ProfiliDAOS(){
        utentiDAO.setTableName("food_delivery.utenti");
        ruoliDAO.setTableName("food_delivery.ruoli");
    }
    UtentiDAOS utentiDAO = new UtentiDAOS();
    RuoliDAOS ruoliDAO = new RuoliDAOS();
    @Override
    public String getSelectByIdQuery(Integer id) {
        return "SELECT * FROM " + this.getTableName() + " x WHERE x.id_profilo = " + id;
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM " + this.getTableName();
    }

    //NB : Profilo > Utente > Set<Ruolo> ruoli . Come scelgo quello corretto ?
    @Override
    public String getInsertQuery(Profilo profilo) {
        Utente relatedUser = profilo.getUtente();
        Integer idUtente = 0;
        Integer idRuolo = 0;

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + this.getTableName() + " (");
        //INTESTAZIONE
        if(relatedUser!=null){
            idUtente = profilo.getUtente().getId();
            queryBuilder.append("id_utente,");
        }
        if(profilo.getRuolo()!=null){
            idRuolo = profilo.getRuolo().getId();
            queryBuilder.append("id_ruolo,");
        }
        if(profilo.getNome()!=null){ queryBuilder.append("nome,");}
        if(profilo.getCognome()!=null){ queryBuilder.append("cognome,");}
        if(profilo.getIndirizzo()!=null){ queryBuilder.append("indirizzo,");}
        if(profilo.getTelefono()!=null){ queryBuilder.append("telefono,");}
        if(profilo.getDataDiNascita()!=null){ queryBuilder.append("data_nascita");}
        //Cancella ultima virgola "," se esiste
        int lastIndex = queryBuilder.length() - 1; //lunghezza di ","
        if(lastIndex > 0 && queryBuilder.substring(lastIndex).equals(",")){
            queryBuilder.delete(lastIndex,lastIndex+1);
        }

        queryBuilder.append(") VALUES (");
        //VALORI
        queryBuilder.append(idUtente + "," + idRuolo + ",");
        if(profilo.getNome()!=null){ queryBuilder.append("'" + profilo.getNome() +"',");}
        if(profilo.getCognome()!=null){ queryBuilder.append("'" + profilo.getCognome() +"',");}
        if(profilo.getIndirizzo()!=null){ queryBuilder.append("'" + profilo.getIndirizzo() +"',");}
        if(profilo.getTelefono()!=null){ queryBuilder.append("'" + profilo.getTelefono() +"',");}
        if(profilo.getDataDiNascita()!=null){ queryBuilder.append("'" + profilo.getDataDiNascita() +"'");}
        //Cancella ultima virgola "," se esiste
        lastIndex = queryBuilder.length() - 1; //lunghezza di ","
        if(lastIndex > 0 && queryBuilder.substring(lastIndex).equals(",")){
            queryBuilder.delete(lastIndex,lastIndex+1);
        }

        return queryBuilder.toString();
    }

    @Override
    public String getDeleteQuery(Integer id) {
        return "DELETE FROM " + this.getTableName() + " x WHERE x.id_profilo = " + id + ";";
    }

    @Override
    public String getUpdateQuery(Integer id, Profilo profilo) {
        //Utente user = profilo.getUtente();
        //Ruolo selectedRole = profilo.getRuolo();
        //Integer idUtente = 0;
        //Integer idRuolo = 0;

        StringBuilder queryBuilder = new StringBuilder("UPDATE " + this.getTableName() + " x SET ");
        //INTESTAZIONE (non voglio poter modificare utente e ruolo del profilo)
        if(profilo.getNome()!=null){queryBuilder.append(" x.nome = '" + profilo.getNome() + "',");}
        if(profilo.getCognome()!=null){ queryBuilder.append(" x.cognome = '" + profilo.getCognome() + "',");}
        if(profilo.getIndirizzo()!=null){ queryBuilder.append(" x.indirizzo = '" + profilo.getIndirizzo() + "',");}
        if(profilo.getTelefono()!=null){ queryBuilder.append(" x.telefono = '" + profilo.getTelefono() + "',");}

        queryBuilder.append(" WHERE x.id_profilo = " + id);

        return queryBuilder.toString();
    }

    @Override
    public String getReplaceQuery(Integer id, Profilo profilo) {
        return getUpdateQuery(id,profilo);
    }


    //Ricerca per nome oppure cognome
    @Override
    public String getSearchByStringQuery(String searchText) {
        StringBuilder qb = new StringBuilder("SELECT * FROM " + this.getTableName() + " x WHERE x.nome LIKE '%" + searchText + "%' ");
        qb.append(" OR x.cognome LIKE '%" + searchText + "%';");
        return qb.toString();
    }

    @Override
    public String getSearchByObjectQuery(Profilo searchObj) {
        String nomeProfilo = searchObj.getNome();
        String cognomeProfilo = searchObj.getCognome();
        //Eccezione : oggetto passato non valido perché è tutto vuoto
        if(nomeProfilo==null && cognomeProfilo==null){ return "SELECT * FROM " + this.getTableName() + " x WHERE x.id_ruolo = 0";}

        StringBuilder qb = new StringBuilder("SELECT * FROM " + this.getTableName() + " x WHERE" );
        if(nomeProfilo!=null){qb.append(" x.nome LIKE '%" + nomeProfilo + "%' AND");}
        if(cognomeProfilo!=null){qb.append(" x.cognome LIKE '%" + cognomeProfilo + "%' AND");}

        // rimuovi l'ultimo "AND" se esiste
        int lastIndex = qb.length() - 4; // lunghezza di " AND"
        if (lastIndex > 0 && qb.substring(lastIndex).equals(" AND")) {
            qb.delete(lastIndex, lastIndex + 4);
        }
        return qb.toString();
    }
    @Override
    public Profilo convertToDto(ResultSet resultSet) throws DaoException {
        Profilo p = null;
        Utente utenteAssociato = null;
        Ruolo ruoloAssociato = null;
        try{
            //Utente Associato al Profilo
            int idUtente = resultSet.getInt("id_utente");

            if(idUtente!=0){
                utenteAssociato = utentiDAO.getById(idUtente);
            }
            //Ruolo Associato al Profilo
            int idRuolo = resultSet.getInt("id_ruolo");
            if(idRuolo!=0){
                ruoloAssociato = ruoliDAO.getById(idRuolo);
            }
            //Gestione data a parte
            java.sql.Date sqlDate = resultSet.getDate("data_nascita");
            java.util.Date utilDate = new java.util.Date(sqlDate.getTime());

            p = new Profilo(
                    resultSet.getInt("id_profilo"),
                    resultSet.getString("nome"),
                    resultSet.getString("cognome"),
                    resultSet.getString("indirizzo"),
                    resultSet.getString("telefono"),
                    utilDate
            );

            if(utenteAssociato!=null){ p.setUtente(utenteAssociato); }
            if(ruoloAssociato!=null){ p.setRuolo(ruoloAssociato);}
            return p;
        }catch(Exception sqle){ sqle.printStackTrace(); throw new DaoException();}
    }

    @Override
    public boolean checkOggetto(Profilo ruolo) throws DaoException {
        return true;
    }

    @Override
    public Integer getGeneratedKey(Statement statement) throws DaoException {
        try{
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e) { e.printStackTrace(); throw new DaoException(); }
    }
}
