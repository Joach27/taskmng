package com.example.taskmng.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

enum Statut {
    TODO, IN_PROGRESS, DONE
}

@Entity
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titre;
    private String description;
    private Date date_echeance;
    private Statut statut;
    
    // Getters and setters
    public void setId(long id){
        this.id = id;
    }    
    
    public String getTitre(){
        return titre;
    }
    public void setTitre(String titre){
        this.titre = titre;
    }
    
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    
    public Date getDateEcheance(){
        return date_echeance;
    }
    public void setTitre(Date date_echeance){
        this.date_echeance = date_echeance;
    }
    
    public Statut setStatut(){
        return statut;
    }
    public void setTitre(Statut statut){
        this.statut = statut;
    }
    
}