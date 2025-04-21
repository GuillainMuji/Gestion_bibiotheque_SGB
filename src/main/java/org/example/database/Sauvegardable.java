package org.example.database;

import java.util.List;

public interface Sauvegardable<T> {
    void sauvegarder(String var1, List<T> var2);

    List<T> charger(String var1);
}
