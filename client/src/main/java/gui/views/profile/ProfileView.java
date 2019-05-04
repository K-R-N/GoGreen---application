package gui.views.profile;

import gui.views.general.View;
import models.User;

public class ProfileView extends View {

    protected User user;

    private ProfileView() {}

    /**
     * Creates the profile view.
     * @param user object containing information about the user
     */
    public ProfileView(User user) {
        this.user = user;

        // add the profile page view
        View profileView = Profile.getProfile();
        addSubview(profileView);
        setInsetsFor(profileView, 0);
    }
}
