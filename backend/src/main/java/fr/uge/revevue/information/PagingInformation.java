package fr.uge.revevue.information;

public record PagingInformation(Integer codePageNumber, Integer reviewPageNumber, Integer commentPageNumber, Integer followedPageNumber) {
    public PagingInformation setDefaultsIfNull() {
        var codePageNumber = codePageNumber();
        var reviewPageNumber = reviewPageNumber();
        var commentPageNumber = commentPageNumber();
        var followedPageNumber = followedPageNumber();
        if (codePageNumber() == null || codePageNumber() < 0) {
            codePageNumber = 0;
        }
        if (reviewPageNumber() == null || reviewPageNumber() < 0) {
            reviewPageNumber = 0;
        }
        if (commentPageNumber() == null || commentPageNumber() < 0) {
            commentPageNumber = 0;
        }
        if (followedPageNumber() == null || followedPageNumber() < 0) {
            followedPageNumber = 0;
        }
        return new PagingInformation(codePageNumber, reviewPageNumber, commentPageNumber, followedPageNumber);
    }

    @Override
    public String toString() {
        return "PagingInformation{" +
                "codePageNumber=" + codePageNumber +
                ", reviewPageNumber=" + reviewPageNumber +
                ", commentPageNumber=" + commentPageNumber +
                ", followedPageNumber=" + followedPageNumber +
                '}';
    }

}