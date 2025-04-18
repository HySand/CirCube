package me.zephyr.circube.content.vlobby;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomEntry {
    private final String roomName;
    private final int difficulty;
    private final int maxPlayers;
    private final List<UUID> players;

    public RoomEntry(String roomName, int difficulty, int maxPlayers) {
        this.roomName = roomName;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>(maxPlayers);
    }

    public String getName() {
        return roomName;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void addPlayer(UUID player) {
        this.players.add(player);
    }

    public void removePlayer(UUID player) {
        this.players.remove(player);
    }
}
