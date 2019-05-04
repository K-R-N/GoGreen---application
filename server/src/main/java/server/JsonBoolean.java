package server;

public class JsonBoolean {

    private Boolean requestSucces;
    //    private String testingtesting = "wowow";

    public JsonBoolean() {
        this.requestSucces = false;
    }

    public JsonBoolean(Boolean requestSucces) {
        this.requestSucces = requestSucces;
    }


    public Boolean getRequestSucces() {
        return requestSucces;
    }

    public void setRequestSucces(Boolean requestSucces) {
        this.requestSucces = requestSucces;
    }


    //    public String getTestingtesting() {
    //        return testingtesting;
    //    }
    //
    //    public void setTestingtesting(String testingtesting) {
    //        this.testingtesting = testingtesting;
    //    }
}
