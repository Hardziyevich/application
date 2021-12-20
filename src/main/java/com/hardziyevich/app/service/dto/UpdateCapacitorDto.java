package com.hardziyevich.app.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateCapacitorDto {

    String id;
    String value;
    String unit;
    String voltage;

}
