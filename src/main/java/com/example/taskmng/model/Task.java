package com.example.taskmng.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titre;
    private String description;
    
    @Column(name = "date_echeance")
    private LocalDate dateEcheance;
    private Statut statut;
    
    // Constructor
    public Task(Long id, String titre, String description, LocalDate dateEcheance, Statut statut){
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateEcheance = dateEcheance;
        this.statut = statut;
    }
    public Task(String titre, String description, LocalDate dateEcheance, Statut statut){
        this.titre = titre;
        this.description = description;
        this.dateEcheance = dateEcheance;
        this.statut = statut;
    }
    
    
    
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
    
    public LocalDate getDateEcheance(){
        return dateEcheance;
    }
    public void setDateEcheance(LocalDate dateEcheance){
        this.dateEcheance = dateEcheance;
    }
    
    public Statut getStatut(){
        return statut;
    }
    public void setStatut(Statut statut){
        this.statut = statut;
    }
    
}