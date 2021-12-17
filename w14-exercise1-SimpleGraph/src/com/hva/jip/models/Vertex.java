package com.hva.jip.models;

import com.hva.jip.interfaces.Identifiable;

/**
 * This class <description of functionality>
 *
 * @author jipderksen
 */
public class Vertex extends SUGraph<Vertex, String> implements Identifiable<String> {
    String identifier;

    public Vertex(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getId() {
        return this.identifier;
    }
}
