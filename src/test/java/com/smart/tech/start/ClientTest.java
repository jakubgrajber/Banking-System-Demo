package com.smart.tech.start;

import com.smart.tech.start.entities.client.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {

    private Person client;
    private static final String FIRSTNAME = "Jakub";
    private static final String LASTNAME = "Grajber";


    @Test
    public void shouldHaveFullName() {
        client = new Person(FIRSTNAME, LASTNAME);

        assertEquals(FIRSTNAME, client.getFirstname());
        assertEquals(LASTNAME, client.getLastname());
    }
}
