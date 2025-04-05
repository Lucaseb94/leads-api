package com.advocacia.leads.security;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing() // ignora caso o .env não exista nos testes
            .load();


    public static String get(String key) {
        return dotenv.get(key, "default"); // retorna "default" se não encontrar
    }
}
