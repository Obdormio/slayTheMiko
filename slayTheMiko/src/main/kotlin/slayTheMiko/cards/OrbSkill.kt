package slayTheMiko.cards

import com.megacrit.cardcrawl.actions.defect.ChannelAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import slayTheMiko.SlayTheMikoMod.Companion.makeCardPath
import slayTheMiko.SlayTheMikoMod.Companion.makeID
import slayTheMiko.characters.Yoimiya
import slayTheMiko.orbs.DefaultOrb

class OrbSkill  // /STAT DECLARATION/
    : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    // Actions the card should do.
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        AbstractDungeon.actionManager.addToBottom(ChannelAction(DefaultOrb())) // Channel a Default Orb.
    }

    // Upgraded stats.
    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            initializeDescription()
        }
    }

    companion object {
        /*
     * Orb time.
     *
     * Channel 1 Default Orb.
     */
        // TEXT DECLARATION
        val ID = makeID(OrbSkill::class.java.simpleName)
        private val cardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
        val IMG = makeCardPath("Skill.png")
        val NAME = cardStrings.NAME
        val DESCRIPTION = cardStrings.DESCRIPTION

        // /TEXT DECLARATION/
        // STAT DECLARATION
        private val RARITY = CardRarity.UNCOMMON
        private val TARGET = CardTarget.SELF
        private val TYPE = CardType.SKILL
        val COLOR = Yoimiya.Enums.COLOR_ORANGE
        private const val COST = 1
    }
}