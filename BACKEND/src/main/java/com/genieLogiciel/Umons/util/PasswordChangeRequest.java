package com.genieLogiciel.Umons.util;

// Classe pour encapsuler les informations de changement de mot de passe
public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;

    // Getters et Setters
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
