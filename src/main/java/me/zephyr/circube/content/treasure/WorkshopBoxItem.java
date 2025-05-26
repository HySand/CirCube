package me.zephyr.circube.content.treasure;

public class WorkshopBoxItem extends BaseTreasureItem {
    public WorkshopBoxItem(Properties builder) {
        super(builder);
    }

    @Override
    protected void addReward() {
        addGun("glock_17", 21, 7);
        addGun("db_short", 21, 7);
        addGun("spr15hb", 21, 7);
        addGun("m4a1", 21, 7);
        addGun("m320", 21, 7);
        addGun("hk_mp5a5", 21, 7);
        addAttachment("sight_coyote", 3, 1);
        addAttachment("sight_uh1", 3, 1);
        addAttachment("grip_rk0", 3, 1);
        addAttachment("stock_m4ss", 3, 1);
        addAttachment("scope_contender", 3, 1);
        addAttachment("muzzle_brake_pioneer", 3, 1);
        addAttachment("muzzle_silencer_phantom_s1", 3, 1);
        addAttachment("muzzle_silencer_mirage", 3, 1);
    }
}
