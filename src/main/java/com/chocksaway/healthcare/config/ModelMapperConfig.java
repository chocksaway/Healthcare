package com.chocksaway.healthcare.config;

import com.chocksaway.healthcare.domain.Action;
import com.chocksaway.healthcare.dto.ActionDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPreferNestedProperties(false);

        // configure Action -> ActionDTO mapping to skip the back-reference (ActionDTO.patient)
        TypeMap<Action, ActionDTO> actionMap = mapper.createTypeMap(Action.class, ActionDTO.class);
        actionMap.addMappings(m -> m.skip(ActionDTO::setPatient));

        return mapper;
    }
}

