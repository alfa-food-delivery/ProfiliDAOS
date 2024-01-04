package it.alfasoft.profilidaos;

import it.alfasoft.daosimple.IDto;
import it.alfasoft.utentidaos.Ruolo;
import it.alfasoft.utentidaos.Utente;

import java.util.Date;

public class Profilo implements IDto<Integer> {
    private Integer idProfilo;
    private Utente utente;
    private Ruolo selectedRole;
    private String nome;
    private String cognome;
    private String indirizzo;
    private String telefono;
    private Date dataDiNascita;

    public Profilo(){}

    public Profilo(int idProfilo, String nome, String cognome, String indirizzo, String telefono, Date dataDiNascita) {
        this.idProfilo = idProfilo;
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.dataDiNascita = dataDiNascita;
    }

    @Override
    public Integer getId() { return this.idProfilo; }
    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public Ruolo getRuolo(){return selectedRole;}
    public void setRuolo(Ruolo ruolo){ this.selectedRole = ruolo;}
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }

    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getIndirizzo() { return indirizzo; }

    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Date getDataDiNascita() { return dataDiNascita; }

    public void setDataDiNascita(Date dataDiNascita) { this.dataDiNascita = dataDiNascita; }
}
