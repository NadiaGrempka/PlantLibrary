package com.example.plantslibrary.config;

import com.example.plantslibrary.domain.usecase.CreateRoomUseCase;
import com.example.plantslibrary.domain.usecase.GetRoomsQueryUseCase;
import com.example.plantslibrary.domain.usecase.UpdateRoomUseCase;
import com.example.plantslibrary.ports.in.CreateRoomUsePort;
import com.example.plantslibrary.ports.in.GetRoomsQueryUsePort;
import com.example.plantslibrary.ports.in.UpdateRoomUsePort;
import com.example.plantslibrary.ports.out.RoomRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomConfig {

    @Bean
    public CreateRoomUsePort createRoomUsePort(RoomRepositoryPort roomRepositoryPort) {
        return new CreateRoomUseCase(roomRepositoryPort);
    }

    @Bean
    public GetRoomsQueryUsePort getRoomsQueryUsePort(RoomRepositoryPort roomRepositoryPort) {
        return new GetRoomsQueryUseCase(roomRepositoryPort);
    }

    @Bean
    public UpdateRoomUsePort updateRoomUsePort(RoomRepositoryPort roomRepositoryPort) {
        return new UpdateRoomUseCase(roomRepositoryPort);
    }
}
