package com.example.bdm.utils;

import com.example.bdm.dto.RequestRegister;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SanitizerUtil {

    /**
     * escapes html sensible characters from a string to prevent code injection
     * @param input
     * @return escaped string
     */
    public String sanitizeForHtml(String input){
        return StringEscapeUtils.escapeHtml4(input.trim());
    }

    /**
     * Given a RequestRegister instance, will return a newly made RequestRegister
     * formed from the sanitization of the given instance's properties
     * @param inputs
     * @return sanitized RequestRegister
     */
    public RequestRegister sanitizeRegisterInputs(RequestRegister inputs){
        RequestRegister sanitizedInputs = new RequestRegister();
        sanitizedInputs.setFirstName(inputs.getFirstName().trim());
        sanitizedInputs.setLastName(inputs.getLastName().trim());
        sanitizedInputs.setEmail(inputs.getEmail().trim());
        sanitizedInputs.setPassword(inputs.getPassword().trim());
        return sanitizedInputs;
    }

}
