package fr.uge.revevue.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Codes")
public class Code extends Post {

    @NotBlank
    @Column(columnDefinition = "VARCHAR(MAX)")
    private String description;

    @NotNull
    @Column(columnDefinition = "VARCHAR(MAX)")
    private byte[] javaContent;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private byte[] unitContent;

    public Code(){}

    public Code(User user, String title, String description, byte[] javaContent) {
        super(title, user);
        this.description = description;
        this.javaContent = javaContent;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public String getJavaContent() {
        return new String(javaContent, StandardCharsets.UTF_8);
    }

    public void setJavaContent(byte[] javaContent) {this.javaContent = javaContent;}

    public String getUnitContent() {
        if (unitContent == null){
            return null;
        }
        return new String(unitContent, StandardCharsets.UTF_8);
    }

    public void setUnitContent(byte[] unitContent) {this.unitContent = unitContent;}
}
