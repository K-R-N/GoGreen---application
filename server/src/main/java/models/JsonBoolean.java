package models;


public class JsonBoolean {
    private Boolean requestSucces;

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

}
