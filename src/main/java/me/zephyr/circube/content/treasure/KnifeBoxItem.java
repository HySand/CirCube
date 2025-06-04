package me.zephyr.circube.content.treasure;

public class KnifeBoxItem extends BaseTreasureItem {
    public KnifeBoxItem(Properties builder) {
        super(builder);
    }

    @Override
    protected void addReward() {
        addMelee("butterfly", 10);
        addMelee("bayonet", 10);
        addMelee("css", 10);
        addMelee("tactical", 10);
        addMelee("m9", 10);
        addMelee("skeleton", 10);
        addMelee("karambit", 10);
    }
}
