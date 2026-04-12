package com.example.taskmng.dto;

import java.time.LocalDate;

import com.example.taskmng.model.Statut;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskRequest {
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;
    
    
    private String description;
    
    @NotNull(message = "La date d'échéance est obligatoire")
    @FutureOrPresent(message = "L'échéance doit être aujourd'hui ou dans le futur")
    private LocalDate dateEcheance;
    
    @NotNull
    private Statut statut;
    
    // CONSTRUCTORS
    // Constructeur vide
    public TaskRequest() {
    }

    // Constructeur avec tous les champs
    public TaskRequest(String titre, String description, LocalDate dateEcheance, Statut statut) {
        this.titre = titre;
        this.description = description;
        this.dateEcheance = dateEcheance;
        this.statut = statut;
    }
    
    // Getters and setters  
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