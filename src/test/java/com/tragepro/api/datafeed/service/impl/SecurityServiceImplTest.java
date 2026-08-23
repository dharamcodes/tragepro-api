package com.tragepro.api.datafeed.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.datafeed.core.repository.SecurityRepository;
import com.tragepro.api.datafeed.service.mapper.SecurityMapper;
import com.tragepro.api.domain.datafeed.entity.SecurityEntity;
import com.tragepro.api.domain.datafeed.response.SecurityResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @Mock
    private SecurityRepository securityRepository;

    @Mock
    private MapperFactory mapperFactory;

    @Mock
    private SecurityMapper securityMapper;

    @InjectMocks
    private SecurityServiceImpl securityService;

    @BeforeEach
    void setUp() {
        // Only mock the mapper factory where necessary, but since it's the first line in the method:
    }

    @Test
    void testFetSecurityBySymbol_InvalidSymbol() {
        when(mapperFactory.getMapper(SecurityMapper.class)).thenReturn(securityMapper);

        AppException exception = assertThrows(AppException.class, () -> securityService.fetSecurityBySymbol(""));
        assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());

        exception = assertThrows(AppException.class, () -> securityService.fetSecurityBySymbol(null));
        assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
    }

    @Test
    void testFetSecurityBySymbol_DataNotFound() {
        when(mapperFactory.getMapper(SecurityMapper.class)).thenReturn(securityMapper);
        when(securityRepository.findBySymbol("AAPL")).thenReturn(null);

        AppException exception = assertThrows(AppException.class, () -> securityService.fetSecurityBySymbol("AAPL"));
        assertEquals(ErrorType.DATA_NOT_FOUND, exception.getErrorType());
    }

    @Test
    void testFetSecurityBySymbol_Success() {
        when(mapperFactory.getMapper(SecurityMapper.class)).thenReturn(securityMapper);
        SecurityEntity entity = new SecurityEntity();
        entity.setSymbol("AAPL");
        entity.setName("Apple");

        SecurityResponse response = new SecurityResponse("NSE", "EQ", 123, "INE", "EQ", "AAPL", "AAPL", "Apple");

        when(securityRepository.findBySymbol("AAPL")).thenReturn(entity);
        when(securityMapper.entityToResponse(entity)).thenReturn(response);

        SecurityResponse result = securityService.fetSecurityBySymbol("AAPL");

        assertEquals("AAPL", result.symbol());
        assertEquals("Apple", result.name());
    }
}
