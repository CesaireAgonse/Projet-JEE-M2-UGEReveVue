package fr.uge.revevue.form;

import java.util.List;

public class TestForm {

    public static class Test{
        public Test(){

        }
        public Test(String selectCode, String content) {
            this.selectCode = selectCode;
            this.content = content;
        }

        private String selectCode;
        private String content;

        public String getSelectCode() {
            return selectCode;
        }

        public void setSelectCode(String selectCode) {
            this.selectCode = selectCode;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "Test{" +
                    "selectCode='" + selectCode + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    private String title;

    private List<Test> content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Test> getContent() {
        return content;
    }

    public void setContent(List<Test> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TestForm{" +
                "title='" + title + '\'' +
                ", content=" + content +
                '}';
    }
}
