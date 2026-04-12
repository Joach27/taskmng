package com.example.taskmng.dto;

import java.time.LocalDate;

import com.example.taskmng.model.Statut;

public class TaskResponse {
    private Long id;
    private String titre;
    private String description;
    private LocalDate dateEcheance;
    private Statut statut;
    
    // CONSTRUCTORS
    // Constructeur vide (obligatoire pour frameworks comme Jackson)
    public TaskResponse() {
    }

    // Constructeur sans id
    public TaskResponse(String titre, String description, LocalDate dateEcheance, Statut statut) {
        this.titre = titre;
        this.description = description;
        this.dateEcheance = dateEcheance;
        this.statut = statut;
    }

    // Constructeur complet
    public TaskResponse(Long id, String titre, String description, LocalDate dateEcheance, Statut statut) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateEcheance = dateEcheance;
        this.statut = statut;
    }
    
    // Getters and setters
    public void setId(Long id){
        this.id = id;
    }   
    public Long getId(){
        return id;
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