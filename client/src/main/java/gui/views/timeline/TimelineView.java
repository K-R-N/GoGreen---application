package gui.views.timeline;

import static gui.helpers.StyleSheet.MouseState.HOVERED;
import static gui.helpers.StyleSheet.MouseState.NONE;
import static gui.helpers.StyleSheet.MouseState.PRESSED;

import gui.helpers.StyleSheet;
import gui.views.general.AlertView;
import gui.views.general.ListView;
import gui.views.general.PopupView;
import gui.views.general.ScrollView;
import gui.views.general.View;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Achievement;
import models.Activity;
import models.Contribution;

import java.time.LocalDate;
import java.util.List;

/**
 * View to show a list of activities.
 * @author Jules van der Toorn
 */
public class TimelineView extends ScrollView {

    /**
     * Handle to refresh list of activities.
     */
    private RangeRefresher<List<Contribution>> refresher;

    /**
     * Variable that holds shown contributions in list.
     */
    private List<Contribution> contributions;

    /**
     * View that holds all contribution cards.
     */
    private ListView listView;

    private boolean didTriggerFetch;
    private boolean didReachEnd;

    private int fetchMonthRange;

    private LocalDate toDate;
    private LocalDate fromDate;

    private boolean showMoreShown;

    private Stage stage;

    /**
     * Define default constructor as private to enforce passing contributions.
     */
    private TimelineView() {}

    /**
     * Constructor of the contribution list component.
     * @param refresher object with get() method which returns most recent contribution list
     */
    public TimelineView(
            RangeRefresher<List<Contribution>> refresher,
            int fetchMonthRange,
            String title,
            Stage stage
    ) {
        super(title);

        this.refresher = refresher;
        this.stage = stage;
        this.toDate = LocalDate.now();
        this.fromDate = toDate.minusMonths(fetchMonthRange);

        this.fetchMonthRange = fetchMonthRange;

        this.didTriggerFetch = false;
        this.didReachEnd = false;
        this.showMoreShown = false;

        this.listView = new ListView();
        this.setSubview(listView);

        // disable horizontal scrollbar
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.vvalueProperty().addListener(
            (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                double contentHeight = ((VBox) scrollPane.getContent()).getHeight();
                double pxOffset = scrollPane.getVvalue() * contentHeight;
                double pxBottomOffset = contentHeight - pxOffset;

                if (!didReachEnd) {
                    if (pxBottomOffset <= 200.0 && !this.didTriggerFetch) {
                        loadOlderContributions();
                    }
                }
            }
        );
    }

    private void loadOlderContributions() {
        this.didTriggerFetch = true;

        List<Contribution> newContributions = this.refresher.get(
            fromDate.minusMonths(fetchMonthRange),
            fromDate.minusDays(1)
        );
        appendContributions(newContributions);

        if (newContributions.size() > 0) {
            toDate = fromDate.minusDays(1);
            fromDate = fromDate.minusMonths(fetchMonthRange);
        }

        this.didTriggerFetch = false;
    }

    /**
     * Setter to update list of contributions.
     * @param contributions list of Contribution
     *                     objects which will be shown in the window
     */
    public void setContributions(List<Contribution> contributions) {
        this.contributions = contributions;

        render();

        appendShowMoreButton();
    }

    /**
     * Appends dynamically loaded contributions to the timeline.
     * @param newContributions list of new contributions
     */
    public void appendContributions(List<Contribution> newContributions) {
        removeShowMoreButton();

        if (newContributions.size() == 0) {
            System.out.println("Reached the end!");

            if (!didReachEnd) {
                didReachEnd = true;

                View view = new View();
                view.setPrefHeight(50.0);
                view.setMinHeight(50.0);
                view.setMaxHeight(50.0);
                Text endText = new Text("nothing to see here anymore :)");
                view.addSubview(endText);

                StyleSheet styleSheet = new StyleSheet();
                styleSheet.set("font-family", "'Roboto Light', sans-serif");
                styleSheet.set("fill", "#344c39");
                styleSheet.set("font-size", "16");
                styleSheet.applyOn(endText);

                listView.append(view);
            }

            return;
        }

        this.contributions.addAll(newContributions);

        for (Contribution contribution: newContributions) {
            if (contribution instanceof Achievement) {
                listView.append(new AchievementView((Achievement) contribution));
            } else if (contribution instanceof Activity) {
                listView.append(new ActivityView((Activity) contribution));
            } else {
                System.out.println("Couldn't cast contribution to achievement or activity!");
            }
        }

        appendShowMoreButton();
    }

    private void appendShowMoreButton() {
        if (!showMoreShown) {

            View view = new View();
            view.setPrefHeight(50.0);
            view.setMinHeight(50.0);
            view.setMaxHeight(50.0);
            Text endText = new Text("click here to load older contributions!");
            view.addSubview(endText);

            view.set(NONE, "fill", "#344c39");
            view.set(HOVERED, "fill", "#1a261c");
            view.set(PRESSED, "fill", "#0f1610");
            view.set("font-family", "'Roboto Light', sans-serif");
            view.set("font-size", "16");

            endText.styleProperty().bind(view.styleProperty());

            view.setOnMouseClicked(e -> {
                removeShowMoreButton();
                loadOlderContributions();
            });

            listView.append(view);

            showMoreShown = true;
        }
    }

    private void removeShowMoreButton() {
        if (showMoreShown && listView.getChildren().size() > 0) {
            listView.vbox.getChildren().remove(listView.vbox.getChildren().size() - 1);
            showMoreShown = false;
        }
    }

    /**
     * Set a value for a CSS attribute.
     * @param attribute string of the JavaFX CSS attribute
     * @param value string of the JavaFX CSS value
     */
    @Override
    public void set(StyleSheet.MouseState state, String attribute, String value) {
        super.set(state, attribute, value);
        if (listView != null) {
            setNode(state, listView, attribute, value);
        }
    }

    /**
     * Refreshes the view contents.
     */
    @Override
    public void refresh() {
        // reset all initialisation values
        this.listView = new ListView();
        this.setSubview(listView);

        this.toDate = LocalDate.now();
        this.fromDate = toDate.minusMonths(fetchMonthRange);

        this.didTriggerFetch = false;
        this.didReachEnd = false;
        this.showMoreShown = false;

        if (refresher.get(fromDate, toDate) == null) {
            PopupView.init(stage);
            new AlertView("Could not retrieve timeline",
                    "Failed to retrieve timeline, "
                            + "this probably means you don't have an internet connection",
                    "okay :(");
            return;
        }

        setContributions(refresher.get(fromDate, toDate));

        scrollPane.setVvalue(0.0);
    }

    /**
     * Adds all subviews and sets constraints.
     */
    private void render() {
        listView.clear();

        if (contributions == null) {
            return;
        }

        for (Contribution contribution: contributions) {
            if (contribution instanceof Achievement) {
                listView.append(new AchievementView((Achievement) contribution));
            } else if (contribution instanceof Activity) {
                listView.append(new ActivityView((Activity) contribution));
            } else {
                System.out.println("Couldn't cast contribution to achievement or activity!");
            }
        }
    }
}
