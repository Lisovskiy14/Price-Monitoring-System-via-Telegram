package com.example.price_monitoring_system.repository.mapper;

import com.example.price_monitoring_system.domain.CssSelectorContainer;
import com.example.price_monitoring_system.repository.entity.CssSelectorContainerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CssSelectorContainerEntityMapper {
    CssSelectorContainer toCssSelectorContainer(CssSelectorContainerEntity cssSelectorContainerEntity);
}
