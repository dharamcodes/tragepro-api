package com.tragepro.api.common.identifier.idgen;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.identifier.service.Base32IdGenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongoBase32IdGen {

    private static final String PREFIX = "A4U";
    private static final int BASE36_WIDTH = 6;

    private final Base32IdGenService base32IdGenService;

    public String generateId() {
        long seq = base32IdGenService.getNextSequence("idGenerator");

        String base36 = Long.toString(seq, 36).toUpperCase();

        if (base36.length() < BASE36_WIDTH) {
            base36 = "0".repeat(BASE36_WIDTH - base36.length()) + base36;
        } else if (base36.length() > BASE36_WIDTH) {
            throw new AppException(ErrorType.INTERNAL_ERROR);
        }
        return PREFIX + base36;
    }
}
