package com.genieLogiciel.Umons.auth.service;

public enum EmailDomain {
    STUDENT("@Illumis.student.ac.be"),
    PROFESSOR("@Illumis.professeur.ac.be"),
    ADMIN("@Illumis.admin.ac.be"),
    ASSISTANT("@Illumis.assistant.ac.be"),
    SERVICE_INSCRIPTION("@Illumis.inscription.ac.be");

    private final String domain;

    EmailDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }
}