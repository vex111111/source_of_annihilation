/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.sourceofannihilation as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.sourceofannihilation;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Style;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screens.Screen;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.ClickEvent;
import com.mojang.blaze3d.vertex.BufferUploader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Renderable;

@OnlyIn(Dist.CLIENT)
public class ExtendsDeathScreen extends Screen implements Renderable{
   private int delayTicker;
   private final Component causeOfDeath;
   private final boolean hardcore;
   private Component deathScore;
   private final List<Button> exitButtons = Lists.newArrayList();
   @Nullable
   private Button exitToTitleButton;
   private Minecraft minecraft = Minecraft.getInstance();
   private int screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
   private int screenHeight = this.minecraft.getWindow().getGuiScaledHeight();

   public ExtendsDeathScreen(@Nullable Component p_95911_, boolean p_95912_) {
      super(Component.translatable(p_95912_ ? "deathScreen.title.hardcore" : "deathScreen.title"));
      this.causeOfDeath = p_95911_;
      this.hardcore = p_95912_;
   }

   protected void init() {
      this.delayTicker = 0;
      this.exitButtons.clear();
      Component component = this.hardcore ? Component.translatable("deathScreen.spectate") : Component.translatable("deathScreen.respawn");
      this.exitButtons.add(this.addRenderableWidget(Button.builder(component, (p_280794_) -> {
         this.minecraft.player.respawn();
         p_280794_.active = false;
      }).bounds(this.width / 2 - 100, this.height / 4 + 72, 200, 20).build()));
      this.exitToTitleButton = this.addRenderableWidget(Button.builder(Component.translatable("deathScreen.titleScreen"), (p_280796_) -> {
         this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::handleExitToTitleScreen, true);
      }).bounds(this.width / 2 - 100, this.height / 4 + 96, 200, 20).build());
      this.exitButtons.add(this.exitToTitleButton);
      this.setButtonsActive(false);
      this.deathScore = Component.translatable("deathScreen.score").append(": ").append(Component.literal(Integer.toString(this.minecraft.player.getScore())).withStyle(ChatFormatting.YELLOW));
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   private void handleExitToTitleScreen() {
      if (this.hardcore) {
         this.exitToTitleScreen();
      } else {
         ConfirmScreen confirmscreen = new ExtendsDeathScreen.TitleConfirmScreen((p_280795_) -> {
            if (p_280795_) {
               this.exitToTitleScreen();
            } else {
               this.minecraft.player.respawn();
               this.minecraft.setScreen((Screen)null);
            }

         }, Component.translatable("deathScreen.quit.confirm"), CommonComponents.EMPTY, Component.translatable("deathScreen.titleScreen"), Component.translatable("deathScreen.respawn"));
         this.minecraft.setScreen(confirmscreen);
         confirmscreen.setDelay(20);
      }
   }

   private void exitToTitleScreen() {
      if (this.minecraft.level != null) {
         this.minecraft.level.disconnect();
      }

      this.minecraft.clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
      this.minecraft.setScreen(new TitleScreen());
   }

   public void render(GuiGraphics p_283488_, int p_283551_, int p_283002_, float p_281981_) {
      p_283488_.fillGradient(0, 0, this.width, this.height, 1615855616, -1602211792);
      p_283488_.pose().pushPose();
      p_283488_.pose().scale(2.0F, 2.0F, 2.0F);
      p_283488_.drawCenteredString(this.font, this.title, this.width / 2 / 2, 30, 16777215);
      p_283488_.pose().popPose();
      if (this.causeOfDeath != null) {
         p_283488_.drawCenteredString(this.font, this.causeOfDeath, this.width / 2, 85, 16777215);
      }

      p_283488_.drawCenteredString(this.font, this.deathScore, this.width / 2, 100, 16777215);
      if (this.causeOfDeath != null && p_283002_ > 85 && p_283002_ < 85 + 9) {
         Style style = this.getClickedComponentStyleAt(p_283551_);
         p_283488_.renderComponentHoverEffect(this.font, style, p_283551_, p_283002_);
      }

      for(Renderable renderable : this.renderables) {
         renderable.render(p_283488_, p_283551_, p_283002_, p_281981_);
      }
      if (this.exitToTitleButton != null && this.minecraft.getReportingContext().hasDraftReport()) {
         p_283488_.blit(AbstractWidget.WIDGETS_LOCATION, this.exitToTitleButton.getX() + this.exitToTitleButton.getWidth() - 17, this.exitToTitleButton.getY() + 3, 182, 24, 15, 15);
      }

   }

   @Nullable
   private Style getClickedComponentStyleAt(int p_95918_) {
      if (this.causeOfDeath == null) {
         return null;
      } else {
         int i = this.minecraft.font.width(this.causeOfDeath);
         int j = this.width / 2 - i / 2;
         int k = this.width / 2 + i / 2;
         return p_95918_ >= j && p_95918_ <= k ? this.minecraft.font.getSplitter().componentStyleAtWidth(this.causeOfDeath, p_95918_ - j) : null;
      }
   }

   public boolean mouseClicked(double p_95914_, double p_95915_, int p_95916_) {
      if (this.causeOfDeath != null && p_95915_ > 85.0D && p_95915_ < (double)(85 + 9)) {
         Style style = this.getClickedComponentStyleAt((int)p_95914_);
         if (style != null && style.getClickEvent() != null && style.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
            this.handleComponentClicked(style);
            return false;
         }
      }

      return super.mouseClicked(p_95914_, p_95915_, p_95916_);
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void tick() {
      super.tick();
      ++this.delayTicker;
      if (this.delayTicker == 20) {
         this.setButtonsActive(true);
      }

   }

   private void setButtonsActive(boolean p_273413_) {
      for(Button button : this.exitButtons) {
         button.active = p_273413_;
      }

   }

   @OnlyIn(Dist.CLIENT)
   public static class TitleConfirmScreen extends ConfirmScreen {
      public TitleConfirmScreen(BooleanConsumer p_273707_, Component p_273255_, Component p_273747_, Component p_273434_, Component p_273416_) {
         super(p_273707_, p_273255_, p_273747_, p_273434_, p_273416_);
      }
   }

   public static void setScreen(@Nullable Screen p_91153_,Minecraft minecraft){
      if (p_91153_ == null && minecraft.level == null) {
         p_91153_ = new TitleScreen();
      } else if (p_91153_ == null && minecraft.player.isDeadOrDying()) {
         if (minecraft.player.shouldShowDeathScreen()) {
            p_91153_ = new DeathScreen((Component)null, minecraft.level.getLevelData().isHardcore());
         } else {
            minecraft.player.respawn();
         }
      }
      Screen old = minecraft.screen;
      if (p_91153_ != null) {
      }

      if (old != null && p_91153_ != old) {
         old.removed();
      }

      minecraft.screen = p_91153_;
      if (minecraft.screen != null) {
         minecraft.screen.added();
      }

      BufferUploader.reset();
      if (p_91153_ != null) {
         minecraft.mouseHandler.releaseMouse();
         KeyMapping.releaseAll();
         p_91153_.init(minecraft, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
         minecraft.noRender = false;
      } else {
         minecraft.getSoundManager().resume();
         minecraft.mouseHandler.grabMouse();
      }

      minecraft.updateTitle();
      }
}