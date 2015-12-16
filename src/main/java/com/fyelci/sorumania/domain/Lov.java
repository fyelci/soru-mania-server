package com.fyelci.sorumania.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Lov.
 */
@Entity
@Table(name = "lov")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lov implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "str_param1")
    private String strParam1;

    @Column(name = "str_param2")
    private String strParam2;

    @Column(name = "int_param1")
    private Integer intParam1;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "sequence")
    private Integer sequence;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrParam1() {
        return strParam1;
    }

    public void setStrParam1(String strParam1) {
        this.strParam1 = strParam1;
    }

    public String getStrParam2() {
        return strParam2;
    }

    public void setStrParam2(String strParam2) {
        this.strParam2 = strParam2;
    }

    public Integer getIntParam1() {
        return intParam1;
    }

    public void setIntParam1(Integer intParam1) {
        this.intParam1 = intParam1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lov lov = (Lov) o;
        return Objects.equals(id, lov.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lov{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", name='" + name + "'" +
            ", strParam1='" + strParam1 + "'" +
            ", strParam2='" + strParam2 + "'" +
            ", intParam1='" + intParam1 + "'" +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            ", sequence='" + sequence + "'" +
            '}';
    }
}
