package slayTheMiko.characters

import basemod.abstracts.CustomPlayer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.EnergyManager
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.events.city.Vampires
import com.megacrit.cardcrawl.helpers.CardLibrary.LibraryType
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.ScreenShake
import com.megacrit.cardcrawl.localization.CharacterStrings
import com.megacrit.cardcrawl.screens.CharSelectInfo
import com.megacrit.cardcrawl.unlock.UnlockTracker
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import slayTheMiko.SlayTheMikoMod
import slayTheMiko.cards.*
import slayTheMiko.relics.DefaultClickableRelic
import slayTheMiko.relics.PlaceholderRelic2
import slayTheMiko.relics.YoimiyaEye
import java.util.ArrayList

class Yoimiya(name: String, setClass: PlayerClass) : CustomPlayer(
    name, setClass, orbTextures, "slayTheMikoResources/images/char/yoimiya/orb/vfx.png", LAYER_SPEED, null, null
) {
    companion object {
        val logger: Logger = LogManager.getLogger(SlayTheMikoMod::class.java.name)

        const val ENERGY_PER_TURN = 3
        const val STARTING_HP = 75
        const val MAX_HP = 75
        const val STARTING_GOLD = 99
        const val CARD_DRAW = 9
        const val ORB_SLOTS = 3

        private val ID = SlayTheMikoMod.makeID("Yoimiya")
        private val characterStrings: CharacterStrings = CardCrawlGame.languagePack.getCharacterString(ID)
        private val NAMES = characterStrings.NAMES
        private val TEXT = characterStrings.TEXT

        val orbTextures = arrayOf(
            "slayTheMikoResources/images/char/yoimiya/orb/layer1.png",
            "slayTheMikoResources/images/char/yoimiya/orb/layer2.png",
            "slayTheMikoResources/images/char/yoimiya/orb/layer3.png",
            "slayTheMikoResources/images/char/yoimiya/orb/layer4.png",
            "slayTheMikoResources/images/char/yoimiya/orb/layer5.png",
            "slayTheMikoResources/images/char/yoimiya/orb/layer6.png",
            "slayTheMikoResources/images/char/yoimiya/orb/layer1d.png",
            "slayTheMikoResources/images/char/yoimiya/orb/layer2d.png",
            "slayTheMikoResources/images/char/yoimiya/orb/layer3d.png",
            "slayTheMikoResources/images/char/yoimiya/orb/layer4d.png",
            "slayTheMikoResources/images/char/yoimiya/orb/layer5d.png",
        )

        private val LAYER_SPEED = floatArrayOf(-64.0F, -32.0F, 0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
    }

    object Enums {
        @SpireEnum
        var YOIMIYA: PlayerClass? = null

        @SpireEnum(name = "SLAY_THE_MIKO_ORANGE_COLOR")
        var COLOR_ORANGE: CardColor? = null

        @SpireEnum(name = "SLAY_THE_MIKO_ORANGE_COLOR")
        @Suppress("unused")
        var LIBRARY_COLOR: LibraryType? = null
    }

    init {
        initializeClass(
            SlayTheMikoMod.YOIMIYA_STANDING_PAINTING,
            SlayTheMikoMod.YOIMIYA_SHOULDER_1,
            SlayTheMikoMod.YOIMIYA_SHOULDER_2,
            SlayTheMikoMod.YOIMIYA_CORPSE,
            loadout,
            20.0F,
            -10.0F,
            220.0F,
            290.0F,
            EnergyManager(ENERGY_PER_TURN)
        )

        dialogX = (drawX + 0.0F * Settings.scale)
        dialogY = (drawY + 220.0F * Settings.scale)
    }

    override fun getLoadout(): CharSelectInfo {
        return CharSelectInfo(
            NAMES[0],
            TEXT[0],
            STARTING_HP,
            MAX_HP,
            ORB_SLOTS,
            STARTING_GOLD,
            CARD_DRAW,
            this,
            startingRelics,
            startingDeck,
            false
        )
    }

    override fun getStartingDeck(): ArrayList<String> {
        val retVal = ArrayList<String>()

        logger.info("Begin loading starter Deck Strings")

        retVal.add(YoimiyaAttack.ID)
        retVal.add(DefaultUncommonAttack.ID)
        retVal.add(DefaultRareAttack.ID)

        retVal.add(YoimiyaBlock.ID)
        retVal.add(DefaultUncommonSkill.ID)
        retVal.add(DefaultRareSkill.ID)

        retVal.add(YoimiyaAsyncPower.ID)
        retVal.add(DefaultUncommonPower.ID)
        retVal.add(DefaultRarePower.ID)

        retVal.add(DefaultAttackWithVariable.ID)
        retVal.add(DefaultSecondMagicNumberSkill.ID)
        retVal.add(OrbSkill.ID)
        return retVal
    }

    override fun getStartingRelics(): ArrayList<String> {
        val retVal = ArrayList<String>()

        retVal.add(YoimiyaEye.ID)
        retVal.add(PlaceholderRelic2.ID)
        retVal.add(DefaultClickableRelic.ID)

        // Mark relics as seen - makes it visible in the compendium immediately
        // If you don't have this it won't be visible in the compendium until you see them in game
        UnlockTracker.markRelicAsSeen(YoimiyaEye.ID)
        UnlockTracker.markRelicAsSeen(PlaceholderRelic2.ID)
        UnlockTracker.markRelicAsSeen(DefaultClickableRelic.ID)

        return retVal
    }

    // character Select screen effect
    override fun doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.25f) // Sound Effect
        CardCrawlGame.screenShake.shake(
            ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false
        ) // Screen Effect
    }

    // character Select on-button-press sound effect
    override fun getCustomModeCharacterButtonSoundKey(): String {
        return "ATTACH_DAGGER_1"
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    override fun getAscensionMaxHPLoss(): Int {
        return 0
    }

    // Should return the card color enum to be associated with your character.
    override fun getCardColor(): CardColor {
        return Enums.COLOR_ORANGE!!
    }

    // Should return a color object to be used to color the trail of moving cards
    override fun getCardTrailColor(): Color {
        return SlayTheMikoMod.ORANGE
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    override fun getEnergyNumFont(): BitmapFont {
        return FontHelper.energyNumFontRed
    }

    // Should return class name as it appears in run history screen.
    override fun getLocalizedCharacterName(): String {
        return NAMES[0]
    }

    //Which card should be obtainable from the Match and Keep event?
    override fun getStartCardForEvent(): AbstractCard {
        return YoimiyaAttack()
    }

    // The class name as it appears next to your player name in-game
    override fun getTitle(playerClass: PlayerClass?): String {
        return NAMES[0]
    }

    // Should return a new instance of your character, sending name as its name parameter.
    override fun newInstance(): AbstractPlayer {
        return Yoimiya(name, chosenClass)
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    override fun getCardRenderColor(): Color {
        return SlayTheMikoMod.ORANGE
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    override fun getSlashAttackColor(): Color {
        return SlayTheMikoMod.ORANGE
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    override fun getSpireHeartSlashEffect(): Array<AbstractGameAction.AttackEffect> {
        return arrayOf(
            AbstractGameAction.AttackEffect.BLUNT_HEAVY,
            AbstractGameAction.AttackEffect.BLUNT_HEAVY,
            AbstractGameAction.AttackEffect.BLUNT_HEAVY,
        )
    }


    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    override fun getSpireHeartText(): String {
        return TEXT[1]
    }


    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    override fun getVampireText(): String {
        return Vampires.DESCRIPTIONS[0]
    }
}
