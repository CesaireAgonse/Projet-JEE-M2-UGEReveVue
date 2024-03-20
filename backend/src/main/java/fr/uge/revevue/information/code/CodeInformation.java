package fr.uge.revevue.information.code;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.information.user.SimpleUserInformation;
import fr.uge.revevue.information.UnitTestResultInformation;

import java.util.*;

public record CodeInformation(
        long id,
        SimpleUserInformation userInformation,
        Vote.VoteType voteType,
        String title,
        String description,
        String javaContent,
        String unitContent,
        UnitTestResultInformation testResultsInformation,
        long score,
        Date date,
        int comments,
        int reviews
) {

    private static Vote.VoteType getVoteOfAuth(User auth, Code code){
        if (auth != null){
            return code.getVoteUser(auth.getUsername());
        }
        return Vote.VoteType.NotVoted;
    }

    public static CodeInformation from(Code code, User auth){
        Objects.requireNonNull(code, "[CodeInformation] code is null");
        return new CodeInformation(
                code.getId(),
                SimpleUserInformation.from(code.getUser()),
                getVoteOfAuth(auth, code),
                code.getTitle(),
                code.getDescription(),
                code.getJavaContent(),
                code.getUnitContent(),
                UnitTestResultInformation.from(code.getTestResults()),
                code.getScore(),
                code.getDate(),
                code.getComments().size(),
                code.getReviews().size()
        );
    }
}

