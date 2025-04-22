package com.akanton.kpo.zoorest.application.handlers;

import com.akanton.kpo.zoorest.application.events.FeedingTimeEvent;
import com.akanton.kpo.zoorest.application.services.implementations.FeedingOrganisationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedingTimeEventHandlerTest {

    @Mock
    private FeedingOrganisationService feedingOrganisationService;

    @InjectMocks
    private FeedingTimeEventHandler eventHandler;

    @Test
    void handleFeedingTimeEvent_ShouldCallFeedingService() {
        // Arrange
        int scheduleId = 1;
        OffsetDateTime feedingTime = OffsetDateTime.now();
        FeedingTimeEvent event = new FeedingTimeEvent(this, scheduleId, feedingTime);
        
        // Act
        eventHandler.handleFeedingTimeEvent(event);
        
        // Assert
        verify(feedingOrganisationService).feedAnimal(scheduleId);
    }
    
    @Test
    void handleFeedingTimeEvent_ShouldLogCorrectMessage() {
        // Arrange
        int scheduleId = 1;
        OffsetDateTime feedingTime = OffsetDateTime.now();
        FeedingTimeEvent event = new FeedingTimeEvent(this, scheduleId, feedingTime);
        
        // Redirect System.out to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            // Act
            eventHandler.handleFeedingTimeEvent(event);
            
            // Assert
            String output = outputStream.toString();
            assertTrue(output.contains("Feeding time event processed for schedule ID: " + scheduleId));
            assertTrue(output.contains("at time: " + feedingTime));
        } finally {
            // Restore System.out
            System.setOut(originalOut);
        }
    }
    
    @Test
    void handleFeedingTimeEvent_ShouldHandleException() {
        // Arrange
        int scheduleId = 1;
        OffsetDateTime feedingTime = OffsetDateTime.now();
        FeedingTimeEvent event = new FeedingTimeEvent(this, scheduleId, feedingTime);
        
        doThrow(new RuntimeException("Test exception")).when(feedingOrganisationService).feedAnimal(scheduleId);
        
        // Redirect System.out to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            // Act
            eventHandler.handleFeedingTimeEvent(event);
            
            // Assert
            String output = outputStream.toString();
            assertTrue(output.contains("Error processing feeding time event: Test exception"));
        } finally {
            // Restore System.out
            System.setOut(originalOut);
        }
    }
} 