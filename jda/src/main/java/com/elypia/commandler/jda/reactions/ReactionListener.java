package com.elypia.commandler.reactions;

import java.util.*;

public class ReactionListener implements IReactionListener {

    private Map<Long, ReactionRecord> trackers;

    public ReactionListener() {
        trackers = new HashMap<>();
    }

    @Override
    public void startTrackingEvents(ReactionRecord record) {
        trackers.put(record.getMessageId(), record);
    }

    @Override
    public void stopTrackingEvents(ReactionRecord record) {
        trackers.remove(record.getMessageId());
    }

    @Override
    public void alterTrackingEvent(ReactionRecord record, int newCommandId) {
        trackers.get(record.getMessageId()).setCommandId(newCommandId);
    }

    @Override
    public ReactionRecord getReactionRecord(long messageId) {
        return trackers.get(messageId);
    }
}
