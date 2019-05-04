package gui.views.timeline;

import java.time.LocalDate;

/**
 * Interface used to pass a time range.
 */
public interface RangeRefresher<T> {

    T get(LocalDate startDate, LocalDate endDate);
}