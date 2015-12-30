package com.fyelci.sorumania.web.rest.mapper;

import com.fyelci.sorumania.domain.*;
import com.fyelci.sorumania.web.rest.dto.StuffDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Stuff and its DTO StuffDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StuffMapper {

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    StuffDTO stuffToStuffDTO(Stuff stuff);

    @Mapping(source = "branchId", target = "branch")
    Stuff stuffDTOToStuff(StuffDTO stuffDTO);

    default Lov lovFromId(Long id) {
        if (id == null) {
            return null;
        }
        Lov lov = new Lov();
        lov.setId(id);
        return lov;
    }
}
