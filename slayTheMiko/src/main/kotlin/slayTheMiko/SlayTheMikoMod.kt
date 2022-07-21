package slayTheMiko

import basemod.*
import basemod.devcommands.ConsoleCommand
import basemod.eventUtil.AddEventParams
import basemod.helpers.RelicType
import basemod.interfaces.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.mod.stslib.Keyword
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.google.gson.Gson
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.TheCity
import com.megacrit.cardcrawl.helpers.CardHelper
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.localization.*
import com.megacrit.cardcrawl.unlock.UnlockTracker
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import slayTheMiko.cards.AbstractDefaultCard
import slayTheMiko.characters.Yoimiya
import slayTheMiko.events.IdentityCrisisEvent
import slayTheMiko.patches.MikoCommand
import slayTheMiko.potions.PlaceholderPotion
import slayTheMiko.relics.*
import slayTheMiko.util.IDCheckDontTouchPls
import slayTheMiko.util.TextureLoader
import slayTheMiko.variables.DefaultCustomVariable
import slayTheMiko.variables.DefaultSecondMagicNumber
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

@SpireInitializer
class SlayTheMikoMod : EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber,
    EditCharactersSubscriber, PostInitializeSubscriber {
    // =============== /MAKE IMAGE PATHS/ =================
    // =============== /INPUT TEXTURE LOCATION/ =================
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    init {
        logger.info("Subscribe to BaseMod hooks")
        BaseMod.subscribe(this)
        modID = "slayTheMiko"
        logger.info("Done subscribing")
        logger.info("Creating the color " + Yoimiya.Enums.COLOR_ORANGE.toString())
        BaseMod.addColor(
            Yoimiya.Enums.COLOR_ORANGE,
            ORANGE,
            ORANGE,
            ORANGE,
            ORANGE,
            ORANGE,
            ORANGE,
            ORANGE,
            ATTACK_YOIMIYA,
            SKILL_YOIMIYA,
            POWER_YOIMIYA,
            ENERGY_ORB_YOIMIYA,
            ATTACK_YOIMIYA_PORTRAIT,
            SKILL_YOIMIYA_PORTRAIT,
            POWER_YOIMIYA_PORTRAIT,
            ENERGY_ORB_YOIMIYA_PORTRAIT,
            CARD_ENERGY_ORB
        )
        logger.info("Done creating the color")
        logger.info("Adding mod settings")
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theDefaultDefaultSettings.setProperty(
            ENABLE_PLACEHOLDER_SETTINGS, "FALSE"
        ) // This is the default setting. It's actually set...
        try {
            val config = SpireConfig("slayTheMikoMod", "slayTheMikoConfig", theDefaultDefaultSettings) // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load() // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        logger.info("Done adding mod settings")
    }

    // =============== LOAD THE CHARACTER =================
    override fun receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + Yoimiya.Enums.YOIMIYA.toString())
        BaseMod.addCharacter(
            Yoimiya("Yoimiya", Yoimiya.Enums.YOIMIYA!!),
            SELECT_YOIMIYA_BUTTON,
            SELECT_YOIMIYA_PORTRAIT_BG,
            Yoimiya.Enums.YOIMIYA
        )
        receiveEditPotions()
        logger.info("Added " + Yoimiya.Enums.YOIMIYA.toString())
    }

    // =============== /LOAD THE CHARACTER/ =================
    // =============== POST-INITIALIZE =================
    override fun receivePostInitialize() {
        logger.info("Loading badge image and mod options")
        ConsoleCommand.addCommand("miko", MikoCommand::class.java)

        // Load the Mod Badge
        val badgeTexture = TextureLoader.getTexture(BADGE_IMAGE)

        // Create the Mod Menu
        val settingsPanel = ModPanel()

        // Create the on/off button:
        val enableNormalsButton = ModLabeledToggleButton("This is the text which goes next to the checkbox.",
            350.0f,
            700.0f,
            Settings.CREAM_COLOR,
            FontHelper.charDescFont,  // Position (trial and error it), color, font
            enablePlaceholder,  // Boolean it uses
            settingsPanel,  // The mod panel in which this button will be in
            {}) { button: ModToggleButton ->  // The actual button:
            enablePlaceholder = button.enabled // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                val config = SpireConfig("slayTheMikoMod", "slayTheMikoConfig", theDefaultDefaultSettings)
                config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder)
                config.save()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        settingsPanel.addUIElement(enableNormalsButton) // Add the button to the settings panel. Button is a go.
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel)


        // =============== EVENTS =================
        // https://github.com/daviscook477/BaseMod/wiki/Custom-Events

        // You can add the event like so:
        // BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);
        // Then, this event will be exclusive to the City (act 2), and will show up for all characters.
        // If you want an event that's present at any part of the game, simply don't include the dungeon ID

        // If you want to have more specific event spawning (e.g. character-specific or so)
        // deffo take a look at that basemod wiki link as well, as it explains things very in-depth
        // btw if you don't provide event type, normal is assumed by default

        // Create a new event builder
        // Since this is a builder these method calls (outside of create()) can be skipped/added as necessary
        val eventParams =
            AddEventParams.Builder(IdentityCrisisEvent.ID, IdentityCrisisEvent::class.java) // for this specific event
                .dungeonID(TheCity.ID) // The dungeon (act) this event will appear in
                .playerClass(Yoimiya.Enums.YOIMIYA) // Character specific event
                .create()

        // Add the event
        BaseMod.addEvent(eventParams)

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options")
    }

    // =============== / POST-INITIALIZE/ =================
    // ================ ADD POTIONS ===================
    private fun receiveEditPotions() {
        logger.info("Beginning to edit potions")

        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        BaseMod.addPotion(
            PlaceholderPotion::class.java,
            PLACEHOLDER_POTION_LIQUID,
            PLACEHOLDER_POTION_HYBRID,
            PLACEHOLDER_POTION_SPOTS,
            PlaceholderPotion.POTION_ID,
            Yoimiya.Enums.YOIMIYA
        )
        logger.info("Done editing potions")
    }

    // ================ /ADD POTIONS/ ===================
    // ================ ADD RELICS ===================
    override fun receiveEditRelics() {
        logger.info("Adding relics")

        // Take a look at https://github.com/daviscook477/BaseMod/wiki/AutoAdd
        // as well as
        // https://github.com/kiooeht/Bard/blob/e023c4089cc347c60331c78c6415f489d19b6eb9/src/main/java/com/evacipated/cardcrawl/mod/bard/BardMod.java#L319
        // for reference as to how to turn this into an "Auto-Add" rather than having to list every relic individually.
        // Of note is that the bard mod uses it's own custom relic class (not dissimilar to our AbstractDefaultCard class for cards) that adds the 'color' field,
        // in order to automatically differentiate which pool to add the relic too.

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(YoimiyaEye(), Yoimiya.Enums.COLOR_ORANGE)
        BaseMod.addRelicToCustomPool(BottledPlaceholderRelic(), Yoimiya.Enums.COLOR_ORANGE)
        BaseMod.addRelicToCustomPool(DefaultClickableRelic(), Yoimiya.Enums.COLOR_ORANGE)

        // This adds a relic to the Shared pool. Every character can find this relic.
        BaseMod.addRelic(PlaceholderRelic2(), RelicType.SHARED)

        // Mark relics as seen - makes it visible in the compendium immediately
        // If you don't have this it won't be visible in the compendium until you see them in game
        // (the others are all starters so they're marked as seen in the character file)
        UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID)
        logger.info("Done adding relics!")
    }

    // ================ /ADD RELICS/ ===================
    // ================ ADD CARDS ===================
    override fun receiveEditCards() {
        logger.info("Adding variables")
        //Ignore this
        pathCheck()
        // Add the Custom Dynamic Variables
        logger.info("Add variables")
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(DefaultCustomVariable())
        BaseMod.addDynamicVariable(DefaultSecondMagicNumber())
        logger.info("Adding cards")
        // Add the cards
        // Don't delete these default cards yet. You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        // This method automatically adds any cards so you don't have to manually load them 1 by 1
        // For more specific info, including how to exclude cards from being added:
        // https://github.com/daviscook477/BaseMod/wiki/AutoAdd

        // The ID for this function isn't actually your modid as used for prefixes/by the getModID() method.
        // It's the mod id you give MTS in ModTheSpire.json - by default your artifact ID in your pom.xml

        AutoAdd("SlayTheMiko") // ${project.artifactId}
            .packageFilter(AbstractDefaultCard::class.java) // filters to any class in the same package as AbstractDefaultCard, nested packages included
            .setDefaultSeen(true).cards()

        // .setDefaultSeen(true) unlocks the cards
        // This is so that they are all "seen" in the library,
        // for people who like to look at the card list before playing your mod
        logger.info("Done adding cards!")
    }

    // ================ /ADD CARDS/ ===================
    // ================ LOAD THE TEXT ===================
    override fun receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: $modID")

        // CardStrings
        BaseMod.loadCustomStringsFile(
            CardStrings::class.java, modID + "Resources/localization/zhs/SlayTheMikoMod-Card-Strings.json"
        )

        // PowerStrings
        BaseMod.loadCustomStringsFile(
            PowerStrings::class.java, modID + "Resources/localization/zhs/SlayTheMikoMod-Power-Strings.json"
        )

        // RelicStrings
        BaseMod.loadCustomStringsFile(
            RelicStrings::class.java, modID + "Resources/localization/zhs/SlayTheMikoMod-Relic-Strings.json"
        )

        // Event Strings
        BaseMod.loadCustomStringsFile(
            EventStrings::class.java, modID + "Resources/localization/zhs/SlayTheMikoMod-Event-Strings.json"
        )

        // PotionStrings
        BaseMod.loadCustomStringsFile(
            PotionStrings::class.java, modID + "Resources/localization/zhs/SlayTheMikoMod-Potion-Strings.json"
        )

        // CharacterStrings
        BaseMod.loadCustomStringsFile(
            CharacterStrings::class.java, modID + "Resources/localization/zhs/SlayTheMikoMod-Character-Strings.json"
        )

        // OrbStrings
        BaseMod.loadCustomStringsFile(
            OrbStrings::class.java, modID + "Resources/localization/zhs/SlayTheMikoMod-Orb-Strings.json"
        )
        logger.info("Done editing strings")
    }

    // ================ /LOAD THE TEXT/ ===================
    // ================ LOAD THE KEYWORDS ===================
    override fun receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        val gson = Gson()
        val json =
            Gdx.files.internal(modID + "Resources/localization/zhs/SlayTheMikoMod-Keyword-Strings.json").readString(
                StandardCharsets.UTF_8.toString()
            )
        val keywords = gson.fromJson(json, Array<Keyword>::class.java)
        if (keywords != null) {
            for (keyword in keywords) {
                BaseMod.addKeyword(
                    modID!!.lowercase(Locale.getDefault()), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION
                )
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }

    companion object {
        // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
        // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
        val logger: Logger = LogManager.getLogger(SlayTheMikoMod::class.java.name)

        // NO
        // DOUBLE NO
        // NU-UH
        var modID: String? = null
            private set(ID) {
                val coolG = Gson()
                val inputStream = SlayTheMikoMod::class.java.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json")
                    ?: throw RuntimeException("Can not find /IDCheckStringsDONT-EDIT-AT-ALL.json") // DON'T EDIT THIS ETHER
                val exceptionStrings = coolG.fromJson(
                    InputStreamReader(inputStream, StandardCharsets.UTF_8), IDCheckDontTouchPls::class.java
                ) // OR THIS, DON'T EDIT IT
                logger.info("You are attempting to set your mod ID as: $ID")
                field = when (ID) {
                    exceptionStrings.DEFAULTID -> {
                        throw RuntimeException(exceptionStrings.EXCEPTION)
                    }
                    exceptionStrings.DEVID -> {
                        exceptionStrings.DEFAULTID
                    }
                    else -> {
                        ID
                    }
                }
                logger.info("Success! ID is $modID")
            }

        // Mod-settings settings. This is if you want an on/off savable button
        var theDefaultDefaultSettings = Properties()
        const val ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder"
        var enablePlaceholder = true // The boolean we'll be setting on/off (true/false)

        //This is for the in-game mod settings panel.
        private const val MODNAME = "SlayTheMiko"
        private const val AUTHOR = "Lidafan" // And pretty soon - You!
        private const val DESCRIPTION = "Slay the miko"

        // =============== INPUT TEXTURE LOCATION =================
        // Colors (RGB)
        // Character Color
        @JvmField
        val ORANGE: Color = CardHelper.getColor(255.0f, 90.0f, 0.0f)

        // Potion Colors in RGB
        val PLACEHOLDER_POTION_LIQUID: Color = CardHelper.getColor(209.0f, 53.0f, 18.0f) // Orange-ish Red
        val PLACEHOLDER_POTION_HYBRID: Color = CardHelper.getColor(255.0f, 230.0f, 230.0f) // Near White
        val PLACEHOLDER_POTION_SPOTS: Color = CardHelper.getColor(100.0f, 25.0f, 10.0f) // Super Dark Red/Brown

        // Card backgrounds - The actual rectangular card.
        private const val ATTACK_YOIMIYA = "slayTheMikoResources/images/512/bg_attack_yoimiya.png"
        private const val SKILL_YOIMIYA = "slayTheMikoResources/images/512/bg_skill_yoimiya.png"
        private const val POWER_YOIMIYA = "slayTheMikoResources/images/512/bg_power_yoimiya.png"
        private const val ENERGY_ORB_YOIMIYA = "slayTheMikoResources/images/512/card_yoimiya_orb.png"
        private const val CARD_ENERGY_ORB = "slayTheMikoResources/images/512/card_small_orb.png"
        private const val ATTACK_YOIMIYA_PORTRAIT = "slayTheMikoResources/images/1024/bg_attack_yoimiya.png"
        private const val SKILL_YOIMIYA_PORTRAIT = "slayTheMikoResources/images/1024/bg_skill_yoimiya.png"
        private const val POWER_YOIMIYA_PORTRAIT = "slayTheMikoResources/images/1024/bg_power_yoimiya.png"
        private const val ENERGY_ORB_YOIMIYA_PORTRAIT = "slayTheMikoResources/images/1024/card_yoimiya_orb.png"

        // Character assets
        private const val SELECT_YOIMIYA_BUTTON = "slayTheMikoResources/images/charSelect/YoimiyaButton.png"
        private const val SELECT_YOIMIYA_PORTRAIT_BG = "slayTheMikoResources/images/charSelect/YoimiyaPortraitBG.png"
        const val YOIMIYA_STANDING_PAINTING = "slayTheMikoResources/images/char/yoimiya/standing_painting.png"
        const val YOIMIYA_SHOULDER_1 = "slayTheMikoResources/images/char/yoimiya/shoulder.png"
        const val YOIMIYA_SHOULDER_2 = "slayTheMikoResources/images/char/yoimiya/shoulder2.png"
        const val YOIMIYA_CORPSE = "slayTheMikoResources/images/char/yoimiya/corpse.png"

        //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
        const val BADGE_IMAGE = "slayTheMikoResources/images/Badge.png"

        // Atlas and JSON files for the Animations
//        const val THE_DEFAULT_SKELETON_ATLAS = "slayTheMikoResources/images/char/yoimiya/skeleton.atlas"
//        const val THE_DEFAULT_SKELETON_JSON = "slayTheMikoResources/images/char/yoimiya/skeleton.json"

        // =============== MAKE IMAGE PATHS =================
        @JvmStatic
        fun makeCardPath(resourcePath: String): String {
            return modID + "Resources/images/cards/" + resourcePath
        }

        @JvmStatic
        fun makeRelicPath(resourcePath: String): String {
            return modID + "Resources/images/relics/" + resourcePath
        }

        @JvmStatic
        fun makeRelicOutlinePath(resourcePath: String): String {
            return modID + "Resources/images/relics/outline/" + resourcePath
        }

        @JvmStatic
        fun makeOrbPath(resourcePath: String): String {
            return modID + "Resources/images/orbs/" + resourcePath
        }

        @JvmStatic
        fun makePowerPath(resourcePath: String): String {
            return modID + "Resources/images/powers/" + resourcePath
        }

        @JvmStatic
        fun makeEventPath(resourcePath: String): String {
            return modID + "Resources/images/events/" + resourcePath
        }

        private fun pathCheck() {
            val coolG = Gson()
            val inputStream = SlayTheMikoMod::class.java.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json")
                ?: throw RuntimeException("Can not find /IDCheckStringsDONT-EDIT-AT-ALL.json")
            val exceptionStrings = coolG.fromJson(
                InputStreamReader(inputStream, StandardCharsets.UTF_8), IDCheckDontTouchPls::class.java
            )
            val packageName = SlayTheMikoMod::class.java.getPackage().name
            val resourcePathExists = Gdx.files.internal(modID + "Resources")
            if (modID != exceptionStrings.DEVID) {
                if (packageName != modID) {
                    throw RuntimeException(exceptionStrings.PACKAGE_EXCEPTION + modID)
                }
                if (!resourcePathExists.exists()) {
                    throw RuntimeException(exceptionStrings.RESOURCE_FOLDER_EXCEPTION + modID + "Resources")
                }
            }
        }

        @JvmStatic
        @Suppress("unused")
        fun initialize() {
            logger.info("========================= Initializing SlayTheMikoMod. =========================")
            SlayTheMikoMod()
            logger.info("========================= /SlayTheMikoMod Initialized./ =========================")
        }

        // ================ /LOAD THE KEYWORDS/ ===================
        // this adds "ModName:" before the ID of any card/relic/power etc.
        // in order to avoid conflicts if any other mod uses the same ID.
        @JvmStatic
        fun makeID(idText: String): String {
            return "$modID:$idText"
        }
    }
}