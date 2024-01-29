package fr.uge.revevue.form;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.entity.Vote;

import javax.validation.constraints.NotBlank;

public class VoteForm {

    @NotBlank
    private String voteType;

    public String getVoteType() {return voteType;}

    public void setVoteType(String voteType) {this.voteType = voteType;}
}
