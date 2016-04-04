package acs.tabbychat.jazzy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.gui.ITCSettingsGUI;

import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

public class TCSpellCheckManager {
    private static TCSpellCheckManager instance = null;

    private TCSpellCheckListener listener;
    private HashMap<Integer, String> errorCache = new HashMap<Integer, String>();
    private final ReentrantReadWriteLock errorLock = new ReentrantReadWriteLock();
    private final Lock errorReadLock = errorLock.readLock();
    private final Lock errorWriteLock = errorLock.writeLock();
    private String lastAttemptedLocale;

    private TCSpellCheckManager() {
        this.reloadDictionaries();
    }

    /**
     * Add word to ignore list
     * 
     * @param word
     */
    public void addToIgnoredWords(String word) {
        if (!listener.spellCheck.isIgnored(word))
            listener.spellCheck.ignoreAll(word);
    }

    public void drawErrors(GuiScreen screen, List<GuiTextField> inputFields) {
        List<String> inputCache = new ArrayList<String>();
        int activeFields = 0;
        for (GuiTextField field : inputFields) {
            if (field.getVisible())
                activeFields++;
            inputCache.add(field.getText());
        }
        if (activeFields == 0)
            return;

        errorReadLock.lock();
        try {
            Iterator<Map.Entry<Integer, String>> errors = errorCache.entrySet().iterator();
            ListIterator<String> inputs;
            Map.Entry<Integer, String> error;

            while (errors.hasNext()) {
                error = errors.next();
                inputs = inputCache.listIterator(activeFields);
                if (!inputs.hasPrevious())
                    break;
                String input = inputs.previous();
                if (input.length() == 0)
                    break;

                int y = screen.height - 4 - 12 * (activeFields - 1);
                int x = 4;
                int width = 0;
                int wordIndex = error.getKey();
                int errLength = error.getValue().length();

                while (wordIndex >= input.length()) {
                    wordIndex -= input.length();
                    y += 12;
                    if (!inputs.hasPrevious()) {
                        return;
                    }
                    input = inputs.previous();
                }

                if (wordIndex + errLength > input.length()) {
                    // Misspelled word spans line break
                    x += Minecraft.getMinecraft().fontRendererObj.getStringWidth(input.substring(0,
                            wordIndex));
                    width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(input.substring(
                            wordIndex, input.length()));
                    this.drawUnderline(screen, x, y, width);

                    if (inputs.hasPrevious()) {
                        int remainder = errLength - input.length() + wordIndex;
                        input = inputs.previous();
                        if (input.length() == 0)
                            continue;
                        else if (remainder > input.length())
                            return;
                        y += 12;
                        x = 4;
                        width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(input
                                .substring(0, remainder));
                    }
                } else {
                    x += Minecraft.getMinecraft().fontRendererObj.getStringWidth(input.substring(0,
                            wordIndex));
                    width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(error.getValue());
                }

                this.drawUnderline(screen, x, y, width);
            }
        } finally {
            errorReadLock.unlock();
        }
    }

    /**
     * Marks word as misspelled
     * 
     * @param screen
     * @param x
     * @param y
     * @param width
     */
    private void drawUnderline(GuiScreen screen, int x, int y, int width) {
        int next = x + 1;
        while (next - x < width) {
            Gui.drawRect(next - 1, y, next, y + 1, 0xaaff0000);
            next += 2;
        }
    }

    public static TCSpellCheckManager getInstance() {
        if (instance == null) {
            instance = new TCSpellCheckManager();
        }
        return instance;
    }

    protected void handleListenerEvent(SpellCheckEvent event) {
        errorWriteLock.lock();
        try {
            errorCache.put(event.getWordContextPosition(), event.getInvalidWord());
        } finally {
            errorWriteLock.unlock();
        }
    }

    /**
     * Loads dictionary
     * 
     * @return
     */
    public boolean loadLocaleDictionary() {
        File localeDict = new File(ITCSettingsGUI.tabbyChatDir,
                Minecraft.getMinecraft().gameSettings.language + ".dic");
        if (localeDict.canRead()) {
            listener = new TCSpellCheckListener(localeDict);
            return true;
        } else
            return false;
    }

    /**
     * Load user dictionary
     */
    public void loadUserDictionary() {
        File userDict = new File(ITCSettingsGUI.tabbyChatDir, "user.dic");
        BufferedReader in = null;
        if (userDict.canRead()) {
            try {
                in = new BufferedReader(new FileReader(userDict));
                String word;
                while ((word = in.readLine()) != null) {
                    listener.spellCheck.ignoreAll(word);
                }
            } catch (Exception e) {
                TabbyChat.printException("Unable to load user dictionary for spell checking", e);
            } finally {
                try {
                    if (in != null)
                        in.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Reloads dictionaries
     */
    public void reloadDictionaries() {
        if (!this.loadLocaleDictionary())
            listener = new TCSpellCheckListener();
        lastAttemptedLocale = Minecraft.getMinecraft().gameSettings.language;
        this.loadUserDictionary();
    }

    public void update(List<GuiTextField> inputFields) {
        if (lastAttemptedLocale != Minecraft.getMinecraft().gameSettings.language)
            this.reloadDictionaries();
        // Clear stored error words and locations
        errorWriteLock.lock();
        try {
            errorCache.clear();
        } finally {
            errorWriteLock.unlock();
        }
        // Clear and re-populate contents of input fields, initiate spell
        // checker
        String inputCache = "";
        for (GuiTextField inputField : inputFields) {
            if (inputField.getVisible()) {
                inputCache = inputField.getText() + inputCache;
            }
        }
        listener.checkSpelling(inputCache);
    }

    @SuppressWarnings("unchecked")
    public List<String> getSuggestions(String word, int threshold) {
        return this.listener.spellCheck.getSuggestions(word, threshold);
    }

    public boolean isSpelledCorrectly(String word) {
        return this.listener.spellCheck.checkSpelling(new StringWordTokenizer(word)) == SpellChecker.SPELLCHECK_OK;
    }
}
