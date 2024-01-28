package fr.uge.revevue.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Codes")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User user;

    @NotNull
    private String title;

    @NotNull
    @Column(columnDefinition = "VARCHAR(MAX)")
    private String description;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String javaContent;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String unitContent;

    public Code(){}

    public Code(User user, String title, String description, String javaContent){
        this.user = user;
        this.title = title;
        this.description = description;
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

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

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
