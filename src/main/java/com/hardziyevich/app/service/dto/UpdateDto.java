package com.hardziyevich.app.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateDto {

    String id;
    String value;
    String unit;
    String voltage;
    String power;

}
