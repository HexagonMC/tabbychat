package acs.tabbychat.core;

import java.util.Date;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import acs.tabbychat.util.TCChatLineFake;

import com.google.gson.annotations.Expose;

public class TCChatLine extends TCChatLineFake {
    @Expose
    protected boolean statusMsg = false;
    @Expose
    public Date timeStamp;

    public TCChatLine(int _counter, ITextComponent _string, int _id) {
        super(_counter, _string, _id);
    }

    public TCChatLine(ChatLine _cl) {
        super(_cl.getUpdatedCounter(), _cl.getChatComponent(), _cl.getChatLineID());
        if (_cl instanceof TCChatLine) {
            timeStamp = ((TCChatLine) _cl).timeStamp;
            statusMsg = ((TCChatLine) _cl).statusMsg;
        }
    }

    public TCChatLine(int _counter, ITextComponent _string, int _id, boolean _stat) {
        this(_counter, _string, _id);
        this.statusMsg = _stat;
    }

    protected void setChatLineString(ITextComponent newLine) {
        this.chatComponent = newLine;
    }

    public ITextComponent getTimeStamp() {
        String format = TabbyChat.generalSettings.timeStamp.format(timeStamp);
        return new TextComponentString(format + " ");
    }

    public ITextComponent getChatComponentWithTimestamp() {
        ITextComponent result = getChatComponent();
        if (TabbyChat.generalSettings.timeStampEnable.getValue() && timeStamp != null) {
            result = getTimeStamp().appendSibling(result);
        }
        return result;
    }
}
