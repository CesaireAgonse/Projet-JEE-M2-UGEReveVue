package fr.uge.revevue.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "Roles")
public class Role {

    public enum TypeRole {
        USER,
        ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private TypeRole typeRole;

    public Role() {}

    public Role(TypeRole typeRole) {
        this.typeRole = typeRole;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public TypeRole getTypeRole() {return typeRole;}

    public void setTypeRole(String name) {this.typeRole = typeRole;}

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", typeRole='" + typeRole + '\'' +
                '}';
    }
}
