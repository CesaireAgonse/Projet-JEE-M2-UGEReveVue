package fr.uge.revevue.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Codes")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User user;

    @NotBlank
    private String title;

    @NotBlank
    @Column(columnDefinition = "VARCHAR(MAX)")
    private String description;

    @NotNull
    @Column(columnDefinition = "VARCHAR(MAX)")
    private String javaContent;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String unitContent;

    private Date date;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "code")
    private Set<Vote> votes;

    public Code(){}

    public Code(User user, String title, String description, String javaContent){
        this.user = user;
        this.title = title;
        this.description = description;
        this.javaContent = javaContent;
        this.votes = new HashSet<>();
        this.date = new Date();
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

    public String getJavaContent() {return javaContent;}

    public void setJavaContent(String javaContent) {this.javaContent = javaContent;}

    public String getUnitContent() {return unitContent;}

    public void setUnitContent(String unitContent) {this.unitContent = unitContent;}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Vote> getVotes() {return votes;}

    public void setVotes(Set<Vote> votes) {this.votes = votes;}

    public int getScoreVote(){
        var score = 0;
        for (var vote : votes) {
            score += vote.getScore();
        }
        return score;
    }

    @Override
    public String toString() {
        return "Code{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", javaContent='" + javaContent + '\'' +
                ", unitContent='" + unitContent + '\'' +
                ", votes=" + votes +
                '}';
    }
}
