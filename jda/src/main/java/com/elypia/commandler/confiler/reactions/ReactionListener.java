package com.elypia.commandler.confiler.reactions;

import com.elypia.commandler.impl.IReactionController;
import com.elypia.commandler.impl.IReactionListener;

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
