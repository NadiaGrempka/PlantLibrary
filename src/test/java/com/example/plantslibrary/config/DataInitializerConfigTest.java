package com.example.plantslibrary.config;

import com.example.plantslibrary.adapters.out.mongo.SpringDataCareEventRepository;
import com.example.plantslibrary.adapters.out.mongo.SpringDataPlantRepository;
import com.example.plantslibrary.adapters.out.mongo.SpringDataRoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerConfigTest {

    @Test
    void initData_shouldInsertDemoData_whenRepositoriesEmpty() throws Exception {
        SpringDataRoomRepository roomRepo = mock(SpringDataRoomRepository.class);
        SpringDataPlantRepository plantRepo = mock(SpringDataPlantRepository.class);
        SpringDataCareEventRepository careRepo = mock(SpringDataCareEventRepository.class);

        when(roomRepo.count()).thenReturn(0L);
        when(plantRepo.count()).thenReturn(0L);
        when(careRepo.count()).thenReturn(0L);

        // save(...) ma zwracać argument
        when(roomRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(plantRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(careRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DataInitializerConfig config = new DataInitializerConfig();

        config.initData(roomRepo, plantRepo, careRepo).run();

        verify(roomRepo, atLeastOnce()).save(any());
        verify(plantRepo, atLeastOnce()).save(any());
        verify(careRepo, atLeastOnce()).save(any());
    }

    @Test
    void initData_shouldDoNothing_whenDataAlreadyPresent() throws Exception {
        SpringDataRoomRepository roomRepo = mock(SpringDataRoomRepository.class);
        SpringDataPlantRepository plantRepo = mock(SpringDataPlantRepository.class);
        SpringDataCareEventRepository careRepo = mock(SpringDataCareEventRepository.class);

        when(roomRepo.count()).thenReturn(1L);
        when(plantRepo.count()).thenReturn(0L);
        when(careRepo.count()).thenReturn(0L);

        DataInitializerConfig config = new DataInitializerConfig();

        config.initData(roomRepo, plantRepo, careRepo).run();

        verify(roomRepo, never()).save(any());
        verify(plantRepo, never()).save(any());
        verify(careRepo, never()).save(any());
    }
}
