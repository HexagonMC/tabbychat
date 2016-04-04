package acs.tabbychat.core;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import acs.tabbychat.util.TabbyChatUtils;

@Mod(name = TabbyChatUtils.name, modid = TabbyChatUtils.modid, version = TabbyChatUtils.version)
public class TabbyChatMod {

    @EventHandler
    public void load(FMLInitializationEvent event) {
        if (willBeLiteLoaded()) {
            TabbyChatUtils.log
                    .warn("LiteModTabbyChat detected and enabled.  Will use that instead.");
            return;
        }
        TabbyChatUtils.startup();
        FMLCommonHandler.instance().bus().register(this);
        TabbyChat.modLoaded = true;
    }

    @SubscribeEvent
    public void postLoad(ClientConnectedToServerEvent event) {
        if (!TabbyChat.liteLoaded)
            GuiNewChatTC.getInstance();
    }

    @SubscribeEvent
    public void onTick(RenderTickEvent event) {
        if (!event.phase.equals(Phase.START) && Minecraft.getMinecraft().theWorld != null) {
            onTickInGui(Minecraft.getMinecraft());
        }
    }

    private boolean onTickInGui(Minecraft minecraft) {
        TabbyChatUtils.chatGuiTick(minecraft);
        return true;
    }

    private boolean willBeLiteLoaded() {
        try {
            Class.forName("com.mumfrey.liteloader.core.LiteLoader");
            Class.forName("acs.tabbychat.core.LiteModTabbyChat");
            // Add -Dtabbychat.ignoreLiteMod=true to JVM args to ignore
            if ("true".equals(System.getProperty("tabbychat.ignoreLiteMod"))) {
                TabbyChatUtils.log
                        .info("LiteModTabbyChat was detected, but will register FML events anyway.");
                return false;
            }
            return true;

        } catch (ClassNotFoundException e) {
        }
        return false;
    }
}
