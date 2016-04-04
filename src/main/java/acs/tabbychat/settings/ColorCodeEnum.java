package acs.tabbychat.settings;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public enum ColorCodeEnum {
    DEFAULT(I18n.format("colors.default"), "", null),
    BLACK(I18n.format("colors.black"), "\u00A70", TextFormatting.BLACK),
    DARKBLUE(I18n.format("colors.darkblue"), "\u00A71", TextFormatting.DARK_BLUE),
    DARKGREEN(I18n.format("colors.darkgreen"), "\u00A72", TextFormatting.DARK_GREEN),
    DARKAQUA(I18n.format("colors.darkaqua"), "\u00A73", TextFormatting.DARK_AQUA),
    DARKRED(I18n.format("colors.darkred"), "\u00A74", TextFormatting.DARK_RED),
    PURPLE(I18n.format("colors.purple"), "\u00A75", TextFormatting.DARK_PURPLE),
    GOLD(I18n.format("colors.gold"), "\u00A76", TextFormatting.GOLD),
    GRAY(I18n.format("colors.gray"), "\u00A77", TextFormatting.GRAY),
    DARKGRAY(I18n.format("colors.darkgray"), "\u00A78", TextFormatting.DARK_GRAY),
    INDIGO(I18n.format("colors.indigo"), "\u00A79", TextFormatting.BLUE),
    BRIGHTGREEN(I18n.format("colors.brightgreen"), "\u00A7a", TextFormatting.GREEN),
    AQUA(I18n.format("colors.aqua"), "\u00A7b", TextFormatting.AQUA),
    RED(I18n.format("colors.red"), "\u00A7c", TextFormatting.RED),
    PINK(I18n.format("colors.pink"), "\u00A7d", TextFormatting.LIGHT_PURPLE),
    YELLOW(I18n.format("colors.yellow"), "\u00A7e", TextFormatting.YELLOW),
    WHITE(I18n.format("colors.white"), "\u00A7f", TextFormatting.WHITE);

    private String title;
    private String code;
    private TextFormatting vanilla;

    private ColorCodeEnum(String _name, String _code, TextFormatting _vanilla) {
        this.title = _name;
        this.code = _code;
        this.vanilla = _vanilla;
    }

    @Override
    public String toString() {
        return this.code + this.title + "\u00A7r";
    }

    public String toCode() {
        return this.code;
    }

    public String color() {
        return this.title;
    }

    public TextFormatting toVanilla() {
        return this.vanilla;
    }

    public static ColorCodeEnum cleanValueOf(String name) {
        try {
            return ColorCodeEnum.valueOf(name);
        } catch (Exception e) {
            return ColorCodeEnum.YELLOW;
        }
    }

}
