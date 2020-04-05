package lk.apiit.eea.stylouse.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.apiit.eea.stylouse.dto.request.ProductRequest;
import lk.apiit.eea.stylouse.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class StringToProductRequestConverter implements Converter<String, ProductRequest> {
    private ObjectMapper objectMapper;

    @Autowired
    public StringToProductRequestConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ProductRequest convert(String s) {
        try {
            return objectMapper.readValue(s, ProductRequest.class);
        } catch (JsonProcessingException e) {
            throw new CustomException("Product request could not convert.", HttpStatus.BAD_REQUEST);
        }
    }
}
