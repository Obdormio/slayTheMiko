package slayTheMiko.cards

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import slayTheMiko.SlayTheMikoMod.Companion.makeCardPath
import slayTheMiko.SlayTheMikoMod.Companion.makeID
import slayTheMiko.actions.UncommonPowerAction
import slayTheMiko.characters.Yoimiya

class DefaultUncommonPower : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    // /STAT DECLARATION/
    init {
        baseMagicNumber = MAGIC
        magicNumber = baseMagicNumber
    }

    // Actions the card should do.
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        AbstractDungeon.actionManager.addToBottom(
            UncommonPowerAction(
                p, m, magicNumber,
                upgraded, damageTypeForTurn, freeToPlayOnce, energyOnUse
            )
        )
    }

    //Upgraded stats.
    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            rawDescription = UPGRADE_DESCRIPTION
            initializeDescription()
        }
    }

    companion object {
        /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Weirdness Apply X (+1) keywords to yourself.
     */
        // TEXT DECLARATION 
        val ID = makeID(DefaultUncommonPower::class.java.simpleName)
        val IMG = makeCardPath("Power.png")
        private val cardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
        val UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION

        // /TEXT DECLARATION/
        // STAT DECLARATION 	
        private val RARITY = CardRarity.UNCOMMON
        private val TARGET = CardTarget.SELF
        private val TYPE = CardType.POWER
        val COLOR = Yoimiya.Enums.COLOR_ORANGE
        private const val COST = -1
        private const val MAGIC = 1
    }
}