package wtf.nebula.event;

import me.bush.eventbus.event.Event;

public class SafewalkEvent extends Event {
    @Override
    protected boolean isCancellable() {
        return true;
    }
}