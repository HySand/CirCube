package me.zephyr.circube.content.vlobby.dungeons;

import me.zephyr.circube.content.vlobby.Dungeon;
import net.minecraft.core.BlockPos;

public class Workshop extends Dungeon {
    public Workshop(int dungeonId, String dungeonName, int difficulty, int maxPlayers) {
        super(dungeonId, dungeonName, difficulty, maxPlayers, new BlockPos(1, 15, 1));
    }

    @Override
    protected void initDungeon() {

    }
}
