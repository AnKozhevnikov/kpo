package com.akanton.kpo.zoorest.application.handlers;

import com.akanton.kpo.zoorest.application.events.AnimalMovedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AnimalMovedEventHandlerTest {

    @InjectMocks
    private AnimalMovedEventHandler eventHandler;

    @Test
    void handleAnimalMovedEvent_ShouldLogCorrectMessage() {
        // Arrange
        int animalId = 1;
        int fromEnclosureId = 2;
        int toEnclosureId = 3;
        Date movedAt = new Date();
        
        AnimalMovedEvent event = new AnimalMovedEvent(this, animalId, fromEnclosureId, toEnclosureId, movedAt);
        
        // Redirect System.out to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            // Act
            eventHandler.handleAnimalMovedEvent(event);
            
            // Assert
            String output = outputStream.toString().trim();
            System.err.println("DEBUG OUTPUT: " + output); // Print debug output to help diagnose
            assertTrue(output.contains("Animal moved event processed for animal ID: " + animalId), "Output should contain animal ID");
            assertTrue(output.contains("from enclosure ID: " + fromEnclosureId), "Output should contain from enclosure ID");
            assertTrue(output.contains("to enclosure ID: " + toEnclosureId), "Output should contain to enclosure ID");
            assertTrue(output.contains("at time: " + movedAt), "Output should contain time");
        } finally {
            // Restore System.out
            System.setOut(originalOut);
        }
    }
    
    @Test
    void handleAnimalMovedEvent_ShouldHandleException() {
        // Arrange
        AnimalMovedEvent event = null; // Null event will cause NullPointerException
        
        // Redirect System.out to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            // Act
            eventHandler.handleAnimalMovedEvent(event);
            
            // Assert
            String output = outputStream.toString().trim();
            assertTrue(output.contains("Error processing animal moved event:"));
        } finally {
            // Restore System.out
            System.setOut(originalOut);
        }
    }
} 