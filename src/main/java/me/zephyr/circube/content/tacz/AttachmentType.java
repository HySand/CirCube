package me.zephyr.circube.content.tacz;


import com.google.gson.annotations.SerializedName;

public enum AttachmentType {
    /**
     * 瞄具
     */
    @SerializedName("scope")
    SCOPE,
    /**
     * 枪口组件
     */
    @SerializedName("muzzle")
    MUZZLE,
    /**
     * 枪托
     */
    @SerializedName("stock")
    STOCK,
    /**
     * 握把
     */
    @SerializedName("grip")
    GRIP,
    /**
     * 激光指示器
     */
    @SerializedName("laser")
    LASER,
    /**
     * 扩容弹夹（匣）
     */
    @SerializedName("extended_mag")
    EXTENDED_MAG,
    /**
     * 词条1
     */
    @SerializedName("trait1")
    TRAIT1,
    /**
     * 词条2
     */
    @SerializedName("trait2")
    TRAIT2,
    /**
     * 用来表示物品不是配件的情况。
     */
    NONE
}
