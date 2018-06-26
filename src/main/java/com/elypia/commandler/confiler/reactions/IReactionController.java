package com.elypia.commandler.confiler.reactions;

public interface IReactionController {

    /**
     * This is called whenever we execute a method which can handle reactions.
     * This should be implemented to store the information inside the reaction
     * record, it makes no difference to Commandler <strong>how</strong> the
     * record is stored however it is important the data persists intact and is
     * returned correctly in {@link #getReactionRecord(long)}.
     *
     * @param record
     */

    void startTrackingEvents(ReactionRecord record);
    void stopTrackingEvents(ReactionRecord record);
    void alterTrackingEvent(ReactionRecord record, int newCommandId);

    ReactionRecord getReactionRecord(long messageId);
}
