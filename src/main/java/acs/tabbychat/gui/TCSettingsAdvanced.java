package acs.tabbychat.gui;

import java.io.File;
import java.util.Properties;

import net.minecraft.client.resources.I18n;
import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.settings.TCSettingBool;
import acs.tabbychat.settings.TCSettingSlider;
import acs.tabbychat.settings.TCSettingTextBox;
import acs.tabbychat.util.TabbyChatUtils;

public class TCSettingsAdvanced extends TCSettingsGUI {
    private static final int CHAT_SCROLL_HISTORY_ID = 9401;
    private static final int MAXLENGTH_CHANNEL_NAME_ID = 9402;
    private static final int MULTICHAT_DELAY_ID = 9403;
    private static final int CHATBOX_UNFOC_HEIGHT_ID = 9406;
    private static final int CHAT_FADE_TICKS_ID = 9408;
    private static final int TEXT_IGNORE_OPACITY_ID = 9410;
    private static final int CONVERT_UNICODE_TEXT_ID = 9411;

    {
        this.propertyPrefix = "settings.advanced";
    }

    public TCSettingTextBox chatScrollHistory = new TCSettingTextBox("100", "chatScrollHistory",
            this.propertyPrefix, CHAT_SCROLL_HISTORY_ID);
    public TCSettingTextBox maxLengthChannelName = new TCSettingTextBox("10",
            "maxLengthChannelName", this.propertyPrefix, MAXLENGTH_CHANNEL_NAME_ID);
    public TCSettingTextBox multiChatDelay = new TCSettingTextBox("500", "multiChatDelay",
            this.propertyPrefix, MULTICHAT_DELAY_ID);
    public TCSettingSlider chatBoxUnfocHeight = new TCSettingSlider(50.0f, "chatBoxUnfocHeight",
            this.propertyPrefix, CHATBOX_UNFOC_HEIGHT_ID, 20.0f, 100.0f);
    public TCSettingSlider chatFadeTicks = new TCSettingSlider(200.0f, "chatFadeTicks",
            this.propertyPrefix, CHAT_FADE_TICKS_ID, 10.0f, 2000.0f);
    public TCSettingBool textIgnoreOpacity = new TCSettingBool(false, "textignoreopacity",
            this.propertyPrefix, TEXT_IGNORE_OPACITY_ID);
    public TCSettingBool convertUnicodeText = new TCSettingBool(false, "convertunicodetext",
            this.propertyPrefix, CONVERT_UNICODE_TEXT_ID);

    public TCSettingsAdvanced(TabbyChat _tc) {
        super(_tc);
        this.name = I18n.format("settings.advanced.name");
        this.settingsFile = new File(tabbyChatDir, "advanced.cfg");
        this.bgcolor = 0x66802e94;
        this.chatScrollHistory.setCharLimit(3);
        this.maxLengthChannelName.setCharLimit(2);
        this.multiChatDelay.setCharLimit(4);
        this.defineDrawableSettings();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void defineDrawableSettings() {
        this.buttonList.add(this.chatScrollHistory);
        this.buttonList.add(this.maxLengthChannelName);
        this.buttonList.add(this.multiChatDelay);
        this.buttonList.add(this.chatBoxUnfocHeight);
        this.buttonList.add(this.chatFadeTicks);
        this.buttonList.add(this.textIgnoreOpacity);
        this.buttonList.add(this.convertUnicodeText);
    }

    @Override
    public void initDrawableSettings() {
        int col1x = (this.width - DISPLAY_WIDTH) / 2 + 55;

        int buttonColor = (this.bgcolor & 0x00ffffff) + 0xff000000;

        this.chatScrollHistory.setLabelLoc(col1x);
        this.chatScrollHistory.setButtonLoc(
                col1x + 5 + mc.fontRendererObj.getStringWidth(this.chatScrollHistory.description),
                this.rowY(1));
        this.chatScrollHistory.setButtonDims(30, 11);

        this.maxLengthChannelName.setLabelLoc(col1x);
        this.maxLengthChannelName.setButtonLoc(
                col1x + 5 + mc.fontRendererObj.getStringWidth(this.maxLengthChannelName.description),
                this.rowY(2));
        this.maxLengthChannelName.setButtonDims(20, 11);

        this.multiChatDelay.setLabelLoc(col1x);
        this.multiChatDelay.setButtonLoc(
                col1x + 5 + mc.fontRendererObj.getStringWidth(this.multiChatDelay.description),
                this.rowY(3));
        this.multiChatDelay.setButtonDims(40, 11);

        this.chatBoxUnfocHeight.setLabelLoc(col1x);
        this.chatBoxUnfocHeight.setButtonLoc(
                col1x + 5 + mc.fontRendererObj.getStringWidth(this.chatBoxUnfocHeight.description),
                this.rowY(4));
        this.chatBoxUnfocHeight.buttonColor = buttonColor;

        this.chatFadeTicks.setLabelLoc(col1x);
        this.chatFadeTicks.setButtonLoc(
                col1x + 5 + mc.fontRendererObj.getStringWidth(this.chatFadeTicks.description),
                this.rowY(5));
        this.chatFadeTicks.buttonColor = buttonColor;
        this.chatFadeTicks.units = "";

        this.textIgnoreOpacity.setButtonLoc(col1x, this.rowY(6));
        this.textIgnoreOpacity.setLabelLoc(col1x + 19);
        this.textIgnoreOpacity.buttonColor = buttonColor;

        this.convertUnicodeText.setButtonLoc(col1x, this.rowY(7));
        this.convertUnicodeText.setLabelLoc(col1x + 19);
        this.convertUnicodeText.buttonColor = buttonColor;
    }

    @Override
    public Properties loadSettingsFile() {
        Properties result = super.loadSettingsFile();
        ChatBox.current.x = TabbyChatUtils.parseInteger(result.getProperty("chatbox.x"),
                ChatBox.absMinX, 10000, ChatBox.absMinX);
        ChatBox.current.y = TabbyChatUtils.parseInteger(result.getProperty("chatbox.y"), -10000,
                ChatBox.absMinY, ChatBox.absMinY);
        ChatBox.current.width = TabbyChatUtils.parseInteger(result.getProperty("chatbox.width"),
                ChatBox.absMinW, 10000, 320);
        ChatBox.current.height = TabbyChatUtils.parseInteger(result.getProperty("chatbox.height"),
                ChatBox.absMinH, 10000, 180);
        ChatBox.anchoredTop = Boolean.parseBoolean(result.getProperty("chatbox.anchoredtop"));
        ChatBox.pinned = Boolean.parseBoolean(result.getProperty("pinchatinterface"));
        return null;
    }

    @Override
    public void saveSettingsFile() {
        Properties settingsTable = new Properties();
        settingsTable.put("chatbox.x", Integer.toString(ChatBox.current.x));
        settingsTable.put("chatbox.y", Integer.toString(ChatBox.current.y));
        settingsTable.put("chatbox.width", Integer.toString(ChatBox.current.width));
        settingsTable.put("chatbox.height", Integer.toString(ChatBox.current.height));
        settingsTable.put("chatbox.anchoredtop", Boolean.toString(ChatBox.anchoredTop));
        settingsTable.put("pinchatinterface", Boolean.toString(ChatBox.pinned));
        super.saveSettingsFile(settingsTable);
    }
}
