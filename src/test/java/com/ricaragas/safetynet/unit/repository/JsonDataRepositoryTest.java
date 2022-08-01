package com.ricaragas.safetynet.unit.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.dto.JsonDataDTO;
import com.ricaragas.safetynet.repository.JsonDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JsonDataRepositoryTest {

    private JsonDataRepository jsonDataRepository;

    @Mock
    ObjectMapper jsonMapper;

    @Test
    public void constructor_should_read_DTO_from_json() throws Exception {
        // ARRANGE
        when(jsonMapper.readValue(any(URL.class),eq(JsonDataDTO.class))).thenReturn(null);
        // ACT
        jsonDataRepository = new JsonDataRepository(jsonMapper, "https://example.com");
        // ASSERT
        verify(jsonMapper).readValue(any(URL.class),eq(JsonDataDTO.class));
    }

    @Test
    public void throws_exception_if_read_fails() throws Exception{
        // ARRANGE
        // ACT
        Executable action = () -> jsonDataRepository = new JsonDataRepository(jsonMapper, "");
        // ASSERT
        var thrown = assertThrows(RuntimeException.class, action);
        assertEquals("Could not initialize data structure", thrown.getMessage());
    }

}
