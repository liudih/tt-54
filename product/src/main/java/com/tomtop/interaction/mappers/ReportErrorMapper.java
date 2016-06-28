package com.tomtop.interaction.mappers;

import com.tomtop.product.models.dto.ReportErrorDto;

public interface ReportErrorMapper {

    int insertSelective(ReportErrorDto record);

}