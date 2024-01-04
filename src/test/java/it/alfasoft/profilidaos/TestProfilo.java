package it.alfasoft.profilidaos;

import it.alfasoft.daosimple.DaoException;
import it.alfasoft.utentidaos.RuoliDAOS;
import it.alfasoft.utentidaos.Ruolo;
import it.alfasoft.utentidaos.Utente;
import it.alfasoft.utentidaos.UtentiDAOS;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class TestProfilo {

    private static ProfiliDAOS profiliDAO = new ProfiliDAOS();
    private static UtentiDAOS utentiDAO = new UtentiDAOS();
    private static RuoliDAOS ruoliDAO = new RuoliDAOS();

    private static Profilo profiloDeleted;

    @BeforeAll
    public static void beforeAll() throws DaoException {
        profiliDAO.setTableName("food_delivery.profili");
        utentiDAO.setTableName("food_delivery.utenti");
        ruoliDAO.setTableName("food_delivery.ruoli");
    }
    /**
     * Scenario Base
     * TEST CREATE PRODOTTO ( INSERT )
     * @throws DaoException
     */
    @Test
    public void testCreaProfilo() throws DaoException {
        Profilo prof1 = new Profilo();

        //Prendo un utente esistente nel database
        Utente user = utentiDAO.getById(7);
        Ruolo selectedUserRole = ruoliDAO.getById(1); //ruolo CLIENTE , 1
        user.addRuolo(selectedUserRole);
        utentiDAO.assegnaCategoria("CLIENTE",7);

        //Imposto i valori del profilo
        prof1.setNome("Carla");
        prof1.setCognome("Bruni");
        prof1.setUtente(user);
        prof1.setRuolo(selectedUserRole);

        //Nota : verificare che se il ruolo non è ancora associato all'utente, venga inserito anche il record nella rispettiva tabella utenti_ruoli (altrimenti andrà in errore)
        Assertions.assertTrue( profiliDAO.create(prof1) > 0 );
    }

    /**
     * Variante Scenario
     * TEST CREATE ENTITA' VUOTA
     */
    @Test
    public void testCreaProfiloNull() {
        Assertions.assertThrows(
                DaoException.class,
                () -> profiliDAO.create(null),
                "Il null non restituisce un errore corretto!!"
        );
    }

    @AfterAll
    public static void reinserisciProfiloRimosso() throws SQLException {
        try ( Statement stmt = profiliDAO.getConnection().createStatement() )
        {
            //stmt.executeUpdate("insert into food_delivery.profili (id_profilo,id_utente,id_ruolo,nome,cognome,indirizzo,telefono,data_nascita) VALUES (1,1,1, 'Mario', 'Rossi', 'Via Guido Reni 104', '123456789', '1990-05-15');");
        }

        try ( Statement stmt = utentiDAO.getConnection().createStatement() )
        {
            //stmt.executeUpdate("DELETE FROM food_delivery.utenti_ruoli  x WHERE x.id_utente = 7;");
        }
    }
}