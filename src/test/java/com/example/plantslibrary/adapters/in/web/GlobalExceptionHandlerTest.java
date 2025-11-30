package com.example.plantslibrary.adapters.in.web;

import com.example.plantslibrary.adapters.in.web.dto.ErrorResponse;
import com.example.plantslibrary.domain.exception.PlantNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handlePlantNotFound_shouldReturn404WithProperErrorResponse() {
        // given
        PlantNotFoundException ex = new PlantNotFoundException("plant-1");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/plants/plant-1");

        // when
        ResponseEntity<ErrorResponse> response = handler.handlePlantNotFound(ex, request);

        // then
        assertEquals(404, response.getStatusCode().value());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.getStatus());
        assertEquals("PLANT_NOT_FOUND", body.getErrorCode());
        assertEquals("/api/plants/plant-1", body.getPath());
        assertEquals(ex.getMessage(), body.getMessage());
        assertNotNull(body.getTimestamp());
    }

    // pomocnicza metoda tylko po to, żeby mieć Method Parameter dla wyjątku
    static class DummyController {
        @SuppressWarnings("unused")
        public void dummy(String param) {}
    }

    @Test
    void handleValidation_shouldReturn400WithJoinedFieldErrors() throws NoSuchMethodException {
        // given BindingResult z błędami pól
        Object target = new Object();
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(target, "request");

        bindingResult.addError(new FieldError("request", "name", "must not be blank"));
        bindingResult.addError(new FieldError("request", "temperature", "must be greater than 0"));

        Method method = DummyController.class.getDeclaredMethod("dummy", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(parameter, bindingResult);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/rooms");

        // when
        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex, request);

        // then
        assertEquals(400, response.getStatusCode().value());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("VALIDATION_ERROR", body.getErrorCode());
        assertEquals("/api/rooms", body.getPath());

        String msg = body.getMessage();
        assertTrue(msg.contains("name must not be blank"));
        assertTrue(msg.contains("temperature must be greater than 0"));
        assertNotNull(body.getTimestamp());
    }

    @Test
    void handleGeneric_shouldReturn500InternalError() {
        // given
        Exception ex = new RuntimeException("Boom");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/anything");

        // when
        ResponseEntity<ErrorResponse> response = handler.handleGeneric(ex, request);

        // then
        assertEquals(500, response.getStatusCode().value());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.getStatus());
        assertEquals("INTERNAL_ERROR", body.getErrorCode());
        assertEquals("/anything", body.getPath());
        assertEquals("Boom", body.getMessage());
        assertNotNull(body.getTimestamp());
    }
}
