package com.fyelci.sorumania.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Stuff entity.
 */
public class StuffDTO implements Serializable {

    private Long id;

    private String nameSurname;

    private Long branchId;

    private String branchName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long lovId) {
        this.branchId = lovId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String lovName) {
        this.branchName = lovName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StuffDTO stuffDTO = (StuffDTO) o;

        if ( ! Objects.equals(id, stuffDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StuffDTO{" +
            "id=" + id +
            ", nameSurname='" + nameSurname + "'" +
            '}';
    }
}
