package gui.views.profile;

import gui.App;
import gui.helpers.ObjectPasser;
import gui.views.general.ButtonView;
import gui.views.general.View;
import gui.views.timeline.RangeRefresher;
import gui.views.timeline.TimelineView;
import javafx.stage.Stage;
import models.Activity;
import models.Contribution;
import models.User;

import java.util.List;

public class ProfilePageView extends View {

    private TimelineView timeline;

    private ObjectPasser<Activity> activityPasser;

    private ProfilePageView() {}

    /**
     * the profile page.
     * @param user the user of which to show the details.
     * @param refresher refreshes.
     * @param activityPasser passed an activity.
     * @param mainStage stage.
     */
    public ProfilePageView(
            User user,
            RangeRefresher<List<Contribution>> refresher,
            ObjectPasser<Activity> activityPasser,
            Stage mainStage
    ) {

        this.activityPasser = activityPasser;

        this.timeline = new TimelineView(refresher, 6,"Your timeline",
                mainStage);

        addSubview(timeline);
        setInsetsFor(timeline, 0);

        ButtonView settingsButton = new ButtonView("SETTINGS",true, () -> {
            View maskRoot = new View();
            ProfileSettingsView settingsView = new ProfileSettingsView(this, mainStage, maskRoot);
            addSubview(settingsView);
            setInsetsFor(settingsView, 0, 0, 0, null);
            settingsView.setMaxWidth(300.0);
            settingsView.setPrefWidth(300.0);
            settingsView.setMinWidth(300.0);
            App.root.addSubview(maskRoot);
            App.root.setInsetsFor(maskRoot, 0, 300, 0, 0);
            maskRoot.set("background-color", "black");
            maskRoot.setOpacity(0.3);
        });

        timeline.getHeaderView().addSubview(settingsButton);
        timeline.getHeaderView().setInsetsFor(settingsButton, 17, 33, null, null);
    }

    /**
     * Refreshes the view contents.
     */
    @Override
    public void refresh() {
        timeline.refresh();
    }
}
