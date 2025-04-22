package com.akanton.kpo.zoorest.presentation;

import com.akanton.kpo.zoorest.application.services.interfaces.IEntityService;
import com.akanton.kpo.zoorest.application.services.interfaces.IFeedingOrganisationService;
import com.akanton.kpo.zoorest.application.services.interfaces.IStatisticsService;
import com.akanton.kpo.zoorest.domain.FeedingSchedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeedingScheduleController.class)
class FeedingScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFeedingOrganisationService feedingOrganisationService;

    @MockBean
    private IStatisticsService statisticsService;

    @Test
    void addFeedingSchedule_ShouldAddScheduleAndReturnOk() throws Exception {
        // Arrange
        LocalTime feedingTime = LocalTime.of(8, 0);
        String timeStr = feedingTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
        
        // Act & Assert
        mockMvc.perform(post("/feeding_schedule/add")
                .param("animalId", "1")
                .param("foodId", "2")
                .param("feedingTime", timeStr)
                .param("amount", "5")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
        
        verify(feedingOrganisationService).addFeedingSchedule(any(FeedingSchedule.class));
    }
    
    @Test
    void removeFeedingSchedule_ShouldRemoveScheduleAndReturnOk() throws Exception {
        // Arrange
        int scheduleId = 1;
        
        // Act & Assert
        mockMvc.perform(delete("/feeding_schedule/{id}", scheduleId))
                .andExpect(status().isOk());
        
        verify(feedingOrganisationService).removeFeedingSchedule(scheduleId);
    }
    
    @Test
    void getAllFeedingSchedules_ShouldReturnAllSchedules() throws Exception {
        // Arrange
        LocalTime morningTime = LocalTime.of(8, 0);
        LocalTime eveningTime = LocalTime.of(18, 0);
        
        FeedingSchedule schedule1 = new FeedingSchedule(1, 1, morningTime, 5);
        schedule1.setId(1);
        FeedingSchedule schedule2 = new FeedingSchedule(2, 2, eveningTime, 3);
        schedule2.setId(2);
        
        List<FeedingSchedule> schedules = Arrays.asList(schedule1, schedule2);
        
        when(statisticsService.getAllFeedingSchedules()).thenReturn(schedules);
        
        // Act & Assert
        mockMvc.perform(get("/feeding_schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].animalId", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].foodId", is(2)));
    }
    
    @Test
    void getFeedingScheduleById_WithExistingId_ShouldReturnSchedule() throws Exception {
        // Arrange
        int scheduleId = 1;
        LocalTime feedingTime = LocalTime.of(8, 0);
        
        FeedingSchedule schedule = new FeedingSchedule(1, 2, feedingTime, 5);
        schedule.setId(scheduleId);
        
        when(statisticsService.getFeedingScheduleById(scheduleId)).thenReturn(schedule);
        
        // Act & Assert
        mockMvc.perform(get("/feeding_schedule/{id}", scheduleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(scheduleId)))
                .andExpect(jsonPath("$.animalId", is(1)))
                .andExpect(jsonPath("$.foodId", is(2)))
                .andExpect(jsonPath("$.amount", is(5)));
    }
    
    @Test
    void getFeedingScheduleById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        int scheduleId = 999;
        when(statisticsService.getFeedingScheduleById(scheduleId)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/feeding_schedule/{id}", scheduleId))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void whenExceptionThrown_ShouldReturnInternalServerError() throws Exception {
        // Arrange
        when(statisticsService.getAllFeedingSchedules()).thenThrow(new RuntimeException("Error"));
        
        // Act & Assert
        mockMvc.perform(get("/feeding_schedule"))
                .andExpect(status().isInternalServerError());
    }
} 