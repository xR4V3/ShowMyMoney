package net.k773.smm;

import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;

public class MoneyBar
{
    public Change current;
    public Change next;
    double balance;
    String plured;
    long time;
    double newbal;
    
    public MoneyBar(final double balance) {
        this.balance = 0.0;
        this.plured = "";
        this.time = 0L;
        this.newbal = 24567.0;
        this.set(balance);
    }
    
    public void draw(final double x, final double y) {
        if (this.current != null && this.current.state != 2) {
            final double sum = this.current.draw(x, y - 11.0);
            if (this.current.state == 2) {
                if (this.current.getValue() > 0.0) {
                    this.set(this.current.result);
                }
                this.current = ((this.next == null) ? null : this.next);
                this.next = null;
            }
            else {
                if (this.current.state == 0 && this.current.getValue() < 0.0 && this.balance != this.current.result) {
                    this.balance = this.current.result;
                }
                this.plured = "§5" + Main.plurals(String.valueOf((int)(this.balance + ((this.current.state == 1) ? sum : 0.0))), Main.money);
            }
        }
        GL11.glTranslated(x, y, 0.0);
        Gui.drawRect(-2, -2, Minecraft.getMinecraft().fontRenderer.getStringWidth(this.plured) + 2, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1, Integer.MIN_VALUE);
        Minecraft.getMinecraft().fontRenderer.drawString(this.plured, 0, 0, Integer.MAX_VALUE);
        GL11.glTranslated(-x, -y, 0.0);
    }
    
    public void onChange(final double balance) {
        final double razn = (this.current == null) ? (balance - this.balance) : (balance - this.current.result);
        if (razn != 0.0) {
            final Change ch = new Change(balance, razn);
            if (this.current == null) {
                this.current = ch;
            }
            else {
                this.next = ch;
            }
        }
    }
    
    public void set(final double balance) {
        this.balance = balance;
        this.plured = "§5" + Main.plurals(String.valueOf((int)this.balance), Main.money);
    }
}
