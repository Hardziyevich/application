package com.hardziyevich.app.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateDto {

    String value;
    String unit;
    String voltage;
    String power;
    String caseValue;
    String tempLow;
    String tempHigh;

}
