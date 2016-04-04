package acs.tabbychat.util;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import com.google.gson.annotations.Expose;

public class TCChatLineFake extends ChatLine {
    protected int updateCounterCreated = -1;
    @Expose
    protected ITextComponent chatComponent;
    protected int chatLineID;

    public TCChatLineFake() {
        super(-1, new TextComponentString(""), 0);
    }

    public TCChatLineFake(int _counter, ITextComponent _string, int _id) {
        super(_counter, _string, _id);
        this.updateCounterCreated = _counter;
        if (_string == null)
            this.chatComponent = new TextComponentString("");
        else
            this.chatComponent = _string;
        this.chatLineID = _id;
    }

    @Override
    public ITextComponent getChatComponent() {
        return this.chatComponent;
    }

    @Override
    public int getUpdatedCounter() {
        return this.updateCounterCreated;
    }

    @Override
    public int getChatLineID() {
        return this.chatLineID;
    }
}
