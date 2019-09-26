package net.k773.smm;

import cpw.mods.fml.common.event.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.*;
import java.io.*;
import io.netty.buffer.*;
import cpw.mods.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import cpw.mods.fml.client.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.player.*;
import java.util.*;

@Mod(modid = "Money", name = "money", version = "1.0")
public class Main
{
    MoneyBar mb;
    public static String[] money;
    
    public Main() {
        this.mb = new MoneyBar(0.0);
    }
    
    @Mod.EventHandler
    public void load(final FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)this);
        final FMLEventChannel ch = NetworkRegistry.INSTANCE.newEventDrivenChannel("balance");
        ch.register((Object)this);
        FMLCommonHandler.instance().bus().register((Object)this);
    }
    
    @SubscribeEvent
    public void onClientPacket(final FMLNetworkEvent.ClientCustomPacketEvent event) {
        final ByteBuf buf = event.packet.payload();
        buf.array();
        final byte[] data = buf.array();
        final ByteArrayInputStream is = new ByteArrayInputStream(data);
        try {
            final ObjectInputStream e = new ObjectInputStream(is);
            if (this.mb == null) {
                this.mb = new MoneyBar(e.readDouble());
            }
            else {
                this.mb.onChange(e.readDouble());
            }
            e.close();
            is.close();
        }
        catch (IOException var6) {
            var6.printStackTrace();
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void eventHandler(final RenderGameOverlayEvent.Post e) {
        if (e.type != RenderGameOverlayEvent.ElementType.ALL || this.mb != null) {}
    }
    
    public static String plurals(final String se, final String[] skl) {
        if (se.contains(".")) {
            return se + " " + skl[2];
        }
        int n = Integer.parseInt(se);
        if (n == 0) {
            return se + " " + skl[2];
        }
        n = Math.abs(n) % 100;
        final int n2 = n % 10;
        return (n > 10 && n < 20) ? (se + " " + skl[2]) : ((n2 > 1 && n2 < 5) ? (se + " " + skl[1]) : ((n2 == 1) ? (se + " " + skl[0]) : (se + " " + skl[2])));
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDraw1(final GuiScreenEvent.DrawScreenEvent.Post e) {
        final GuiScreen gui = e.gui;
        if (gui instanceof GuiInventory) {
            final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
            e.gui.mc.thePlayer.getActivePotionEffects();
            final int width = res.getScaledWidth();
            final int height = res.getScaledHeight();
            final int y = height / 2 - 16;
            this.mb.draw(width / 2, y);
        }
    }
    
    static {
        Main.money = new String[] { "руб.", "руб.", "руб." };
    }
}
