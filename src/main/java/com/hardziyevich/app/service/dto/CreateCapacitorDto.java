package com.hardziyevich.app.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateCapacitorDto {

    String value;
    String unit;
    String voltage;
    String caseValue;
    String tempLow;
    String tempHigh;

}
