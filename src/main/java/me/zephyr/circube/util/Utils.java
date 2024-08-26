package me.zephyr.circube.util;

import me.zephyr.circube.CirCube;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Utils {
    public static final Random random = new Random();

    public static String getOrCreateBeaconName(String id) {
        return id == null || id.isEmpty() ? generateRandomId() : id;
    }

    public static String getOrCreateHashString(String hash, Level level, BlockPos pos) {
        return hash == null ? Utils.getSHA256(
                "<POS X:" + pos.getX() +
                        ", Y:" + pos.getY() +
                        ", Z:" + pos.getZ() +
                        ", WORLD: \">" + level.dimension().location() + "\">"
        ) : hash;
    }

    public static String getSHA256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            CirCube.LOGGER.error(e.getMessage());
        }
        return "";
    }

    private static String generateRandomId() {
        if (random.nextDouble() < 1e-4) {
            return "DeatHunter was here";
        }
        var sb = new StringBuilder();
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};
        char[] consonants = {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'};
        for (int i = 0; i < 4; ++i) {
            var consonant = consonants[Utils.random.nextInt(consonants.length)];
            if (i == 0) {
                consonant = Character.toUpperCase(consonant);
            }
            sb.append(consonant);
            sb.append(vowels[Utils.random.nextInt(vowels.length)]);
        }
        return sb.toString();
    }
}
