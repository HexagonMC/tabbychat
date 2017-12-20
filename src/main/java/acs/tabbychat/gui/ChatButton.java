package acs.tabbychat.gui;

import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import org.lwjgl.input.Keyboard;
import acs.tabbychat.core.ChatChannel;
import acs.tabbychat.core.GuiNewChatTC;
import acs.tabbychat.core.TabbyChat;

public class ChatButton extends GuiButton {

    public ChatChannel channel;

    public ChatButton() {
        super(9999, 0, 0, 1, 1, "");
    }

    public ChatButton(int _id, int _x, int _y, int _w, int _h, String _title) {
        super(_id, _x, _y, _w, _h, _title);
    }

    /**
     * Returns button width
     * 
     * @return
     */
    public int width() {
        return this.width;
    }

    /**
     * Sets button width
     * 
     * @param _w
     */
    public void width(int _w) {
        this.width = _w;
    }

    /**
     * Returns button height
     * 
     * @return
     */
    public int height() {
        return this.height;
    }

    /**
     * Sets button height
     * 
     * @param _h
     */
    public void height(int _h) {
        this.height = _h;
    }

    /**
     * Returns X-position of button
     * 
     * @return
     */
    public int x() {
        return x;
    }

    /**
     * Sets X-position of button
     * 
     * @param _x
     */
    public void x(int _x) {
        x = _x;
    }

    /**
     * Returns Y-position of button
     * 
     * @return
     */
    public int y() {
        return y;
    }

    /**
     * Sets Y-position of button
     * 
     * @param _y
     */
    public void y(int _y) {
        y = _y;
    }

    public void clear() {
        this.channel = null;
    }

    private static Rectangle translateButtonDims(Rectangle unscaled) {
        float scaleSetting = GuiNewChatTC.getInstance().getScaleSetting();
        int adjX = Math.round((unscaled.x - ChatBox.current.x) * scaleSetting + ChatBox.current.x);

        int adjY = Math.round((TabbyChat.mc.currentScreen.height - unscaled.y + ChatBox.current.y)
                * (1.0f - scaleSetting))
                + unscaled.y;

        int adjW = Math.round(unscaled.width * scaleSetting);
        int adjH = Math.round(unscaled.height * scaleSetting);
        return new Rectangle(adjX, adjY, adjW, adjH);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int par2, int par3) {
        Rectangle cursor = translateButtonDims(new Rectangle(this.x(), this.y(), this.width(),
                this.height()));
        return this.enabled && this.visible && par2 >= cursor.x && par3 >= cursor.y
                && par2 < cursor.x + cursor.width && par3 < cursor.y + cursor.height;
    }

    @Override
    public void drawButton(Minecraft mc, int cursorX, int cursorY, float partialTicks) {
        if (this.visible) {
            FontRenderer fr = mc.fontRenderer;
            float _mult = mc.gameSettings.chatOpacity * 0.9F + 0.1F;
            int _opacity = (int) (255 * _mult);
            int textOpacity = (TabbyChat.advancedSettings.textIgnoreOpacity.getValue() ? 255
                    : _opacity);

            Rectangle cursor = translateButtonDims(new Rectangle(this.x(), this.y(), this.width(),
                    this.height()));

            boolean hovered = cursorX >= cursor.x && cursorY >= cursor.y
                    && cursorX < cursor.x + cursor.width && cursorY < cursor.y + cursor.height;

            int var7 = 0xa0a0a0;
            int var8 = 0;
            if (!this.enabled) {
                var7 = -0x5f5f60;
            } else if (hovered) {
                var7 = 0xffffa0;
                var8 = 0x7f8052;
            } else if (this.channel.active) {
                var7 = 0xa5e7e4;
                var8 = 0x5b7c7b;
            } else if (this.channel.unread) {
                var7 = 0xff0000;
                var8 = 0x720000;
            }
            drawRect(this.x(), this.y(), this.x() + this.width(), this.y() + this.height(), var8
                    + (_opacity / 2 << 24));
            GlStateManager.enableBlend();
            if (hovered && Keyboard.isKeyDown(42)) {
                String special = (this.channel.getTitle().equalsIgnoreCase("*") ? "\u2398"
                        : "\u26A0");
                this.drawCenteredString(fr, special, this.x() + this.width() / 2,
                        this.y() + (this.height() - 8) / 2, var7 + (textOpacity << 24));
            } else {
                this.drawCenteredString(fr, this.displayString, this.x() + this.width() / 2,
                        this.y() + (this.height() - 8) / 2, var7 + (textOpacity << 24));
            }
        }
    }
}
