package fr.uge.revevue.information;

import fr.uge.revevue.entity.Code;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record CodeInformation(
        long id,
        UserInformation userInformation,
        String title,
        String description,
        String javaContent,
        String unitContent,
        long scoreVote,
        Date date,
        Set<CommentInformation> comments
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
                code.getScore(),
                code.getDate(),
                code.getComments().stream().map(CommentInformation::from).collect(Collectors.toSet())
        );
    }
}

