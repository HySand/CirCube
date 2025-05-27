package me.zephyr.circube.content.treasure;

public class PitBoxItem extends BaseTreasureItem {
    public PitBoxItem(Properties builder) {
        super(builder);
    }

    @Override
    protected void addReward() {
        addGun("m1911", 21, 7);
        addGun("ak47", 21, 7);
        addGun("rpk", 21, 7);
        addGun("m700", 21, 7);
        addGun("m16a4", 21, 7);
        addGun("uzi", 21, 7);
        addAttachment("sight_deltapoint_pistol", 3, 1);
        addAttachment("sight_deltapoint_rifle", 3, 1);
        addAttachment("sight_okp7", 3, 1);
        addAttachment("sight_552", 3, 1);
        addAttachment("scope_acog_ta31", 3, 1);
        addAttachment("scope_scout", 3, 1);
        addAttachment("muzzle_silencer_ursus", 3, 1);
        addAttachment("muzzle_silencer_knight_qd", 3, 1);
    }
}
