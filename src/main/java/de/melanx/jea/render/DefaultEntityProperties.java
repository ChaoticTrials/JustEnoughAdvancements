package de.melanx.jea.render;

public class DefaultEntityProperties {
    
    public static final DefaultEntityProperties DEFAULT = new DefaultEntityProperties(false, false);
    public static final DefaultEntityProperties FIRE = new DefaultEntityProperties(true, false);
    public static final DefaultEntityProperties BABY = new DefaultEntityProperties(false, true);
    
    public final boolean fire;
    public final boolean baby;

    public DefaultEntityProperties(boolean fire, boolean baby) {
        this.fire = fire;
        this.baby = baby;
    }
}
