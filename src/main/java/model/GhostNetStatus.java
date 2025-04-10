package main.java.model;

//mögliche Statuswerte eines Geisternetzes
public enum GhostNetStatus {
    GEMELDET,   //Netz wurde gemeldet, aber noch nicht zur Bergung zugeordnet
    BERGUNG_BEVORSTEHEND,   //Person wurde zur Bergung eingetragen
    GEBORGEN,   //Netz wurde erfolgreich geborgen   
    VERSCHOLLEN,    //Netz befindet sich nicht an den Koordinaten
} 
    

