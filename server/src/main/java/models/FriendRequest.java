package models;

public class FriendRequest {
    private int requestId;
    private String fromUser;

    /**
     * Encapsulate default constructor to enforce passing all attributes.
     */
    private FriendRequest() {}

    /**
     * A constructor for friend request object.
     * @param id request id
     * @param fromUser the user who made the request
     */
    public FriendRequest(int id, String fromUser) {
        this.requestId = id;
        this.fromUser = fromUser;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

}
