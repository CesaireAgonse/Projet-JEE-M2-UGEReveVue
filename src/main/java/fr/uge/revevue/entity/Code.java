package fr.uge.revevue.entity;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Codes")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User user;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String javaContent;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String unitContent;

    public Code(){}

    public Code(User user, String javaContent){
        this.user = user;
        this.javaContent = javaContent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJavaContent() {
        return javaContent;
    }

    public void setJavaContent(String javaContent) {
        this.javaContent = javaContent;
    }

    public String getUnitContent() {
        return unitContent;
    }

    public void setUnitContent(String unitContent) {
        this.unitContent = unitContent;
    }
}
