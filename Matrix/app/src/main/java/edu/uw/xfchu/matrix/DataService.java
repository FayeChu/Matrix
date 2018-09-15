package edu.uw.xfchu.matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xfchu on 9/14/18.
 */

public class DataService {
    /**
     * Fake all the event data for now.
     */
    public static List<Event> getEventData() {
        List<Event> eventData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            eventData.add(
                    new Event("Event", "136 102nd", "This is a family event")
            );
        }

        return eventData;
    }
}
