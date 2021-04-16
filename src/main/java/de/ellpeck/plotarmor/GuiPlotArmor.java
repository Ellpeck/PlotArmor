package de.ellpeck.plotarmor;

import de.ellpeck.plotarmor.network.PacketHandler;
import de.ellpeck.plotarmor.network.PacketPlayerList;
import de.ellpeck.plotarmor.network.PacketToggle;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiPlotArmor extends GuiScreen {

    private static final ResourceLocation TEXTURE = new ResourceLocation(PlotArmor.ID, "textures/ui.png");
    private static final int MAX_WIDGET_AMT_X = 3;
    private static final int MAX_WIDGET_AMT_Y = 3;
    private static final int SIZE_X = 278;
    private static final int SIZE_Y = 166;

    private final List<Widget> widgets = new ArrayList<>();

    private int guiLeft;
    private int guiTop;
    private List<PacketPlayerList.Player> players;
    private int widgetAmountY;
    private int scrollOffset;
    private boolean isScrolling;

    public GuiPlotArmor() {

    }

    @Override
    public void initGui() {
        this.guiLeft = (this.width - SIZE_X) / 2;
        this.guiTop = (this.height - SIZE_Y) / 2;
        this.updatePlayerList();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // background
        this.mc.getTextureManager().bindTexture(TEXTURE);
        drawModalRectWithCustomSizedTexture(this.guiLeft, this.guiTop, 0, 0, SIZE_X, SIZE_Y, 512, 512);

        // scroll bar
        if (this.widgetAmountY > MAX_WIDGET_AMT_Y) {
            float percentage = this.scrollOffset / (float) (this.widgetAmountY - MAX_WIDGET_AMT_Y);
            drawModalRectWithCustomSizedTexture(this.guiLeft + 261, this.guiTop + 19 + (int) (percentage * (142 - 15)), 232, 241, 12, 15, 512, 512);
        } else {
            drawModalRectWithCustomSizedTexture(this.guiLeft + 261, this.guiTop + 19, 244, 241, 12, 15, 512, 512);
        }

        // widgets
        this.fontRenderer.drawString(I18n.format("info." + PlotArmor.ID + ".plot_armor"), this.guiLeft + 6, this.guiTop + 6, 4210752);
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (Widget widget : this.widgets)
            widget.draw();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        if (button == 0 && mouseX >= this.guiLeft + 261 && mouseY >= this.guiTop + 19 && mouseX < this.guiLeft + 261 + 12 && mouseY < this.guiTop + 19 + 120) {
            this.isScrolling = true;
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (button == 0)
            this.isScrolling = false;
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (this.isScrolling && this.widgetAmountY > MAX_WIDGET_AMT_Y) {
            float percentage = MathHelper.clamp(((float) mouseY - (this.guiTop + 19)) / (142 - 15), 0, 1);
            int offset = (int) (percentage * (float) (this.widgetAmountY - MAX_WIDGET_AMT_Y));
            if (offset != this.scrollOffset) {
                this.scrollOffset = offset;
                this.updatePlayerList();
            }
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int scroll = Mouse.getEventDWheel();
        if (scroll != 0 && this.widgetAmountY > MAX_WIDGET_AMT_Y) {
            int offset = MathHelper.clamp(this.scrollOffset - (int) Math.signum(scroll), 0, this.widgetAmountY - MAX_WIDGET_AMT_Y);
            if (offset != this.scrollOffset) {
                this.scrollOffset = offset;
                this.updatePlayerList();
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        for (Widget widget : this.widgets) {
            if (widget.actionPerformed(button))
                return;
        }
    }

    public void setPlayers(List<PacketPlayerList.Player> players) {
        this.players = players;
        this.updatePlayerList();
    }

    private void updatePlayerList() {
        if (this.players == null)
            return;

        this.buttonList.clear();
        this.widgets.clear();

        this.widgetAmountY = MathHelper.ceil(this.players.size() / (float) MAX_WIDGET_AMT_X);
        for (int y = 0; y < MAX_WIDGET_AMT_Y; y++) {
            for (int x = 0; x < MAX_WIDGET_AMT_X; x++) {
                int index = (this.scrollOffset + y) * MAX_WIDGET_AMT_X + x;
                if (index >= this.players.size())
                    return;
                this.widgets.add(new Widget(this.players.get(index),
                        this.guiLeft + 5 + 85 * x, this.guiTop + 20 + 50 * y));
            }
        }
    }

    private class Widget {

        public final PacketPlayerList.Player player;
        public final int x;
        public final int y;

        private final GuiButton toggleButton;

        public Widget(PacketPlayerList.Player player, int x, int y) {
            this.player = player;
            this.x = x;
            this.y = y;

            this.toggleButton = new GuiButton(-1, x, y + 20, 82, 20, null);
            GuiPlotArmor.this.buttonList.add(this.toggleButton);
            this.updateButtonText();
        }

        public void draw() {
            // draw player name
            int nameX = this.x + 81 / 2 - GuiPlotArmor.this.fontRenderer.getStringWidth(this.player.name) / 2;
            GuiPlotArmor.this.fontRenderer.drawString(this.player.name, nameX, this.y, 4210752);

            // draw health
            GlStateManager.color(1, 1, 1, 1);
            GuiPlotArmor.this.mc.getTextureManager().bindTexture(ICONS);
            int health = MathHelper.ceil(this.player.healthPercentage * 20F);
            for (int i = 0; i < 10; i++) {
                int x = this.x + i * 8;
                int y = this.y + 10;
                GuiPlotArmor.this.drawTexturedModalRect(x, y, 16, 0, 9, 9);

                if (i * 2 + 1 < health) {
                    GuiPlotArmor.this.drawTexturedModalRect(x, y, 16 + 36, 0, 9, 9);
                } else if (i * 2 + 1 == health) {
                    GuiPlotArmor.this.drawTexturedModalRect(x, y, 16 + 45, 0, 9, 9);
                }
            }
        }

        public boolean actionPerformed(GuiButton button) {
            if (button != this.toggleButton)
                return false;
            this.player.plotArmorEnabled = !this.player.plotArmorEnabled;
            PacketHandler.sendToServer(new PacketToggle(this.player.id, this.player.plotArmorEnabled));
            this.updateButtonText();
            return true;
        }

        private void updateButtonText() {
            this.toggleButton.displayString = I18n.format("info." + PlotArmor.ID + ".plot_armor_" + (this.player.plotArmorEnabled));
        }
    }
}
