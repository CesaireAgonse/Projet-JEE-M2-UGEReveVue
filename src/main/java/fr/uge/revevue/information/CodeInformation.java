package fr.uge.revevue.information;

import fr.uge.revevue.entity.Code;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

public record CodeInformation(
        long id,
        UserInformation userInformation,
        String title,
        String description,
        String javaContent,
        String unitContent,
        int scoreVote,
        Date date
) {
    public static CodeInformation from(Code code){
        Objects.requireNonNull(code, "[CodeInformation] code is null");
        return new CodeInformation(
                code.getId(),
                UserInformation.from(code.getUser()),
                code.getTitle(),
                code.getDescription(),
                code.getJavaContent(),
                code.getUnitContent(),
                code.getScoreVote(),
                code.getDate()
        );
    }
}

