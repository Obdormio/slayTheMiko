package slayTheMiko.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.PlatedArmorPower
import slayTheMiko.SlayTheMikoMod.Companion.makeCardPath
import slayTheMiko.SlayTheMikoMod.Companion.makeID
import slayTheMiko.characters.Yoimiya

class DefaultUncommonSkill : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    // /STAT DECLARATION/
    init {
        baseBlock = BLOCK
    }

    // Actions the card should do.
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        AbstractDungeon.actionManager.addToBottom(
            ApplyPowerAction(p, p, PlatedArmorPower(p, block), block)
        )
    }

    // Upgraded stats.
    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeBlock(UPGRADE_PLUS_BLOCK)
            upgradeBaseCost(UPGRADE_REDUCED_COST)
            initializeDescription()
        }
    }

    companion object {
        /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * A Better Defend Gain 1 Plated Armor. Affected by Dexterity.
     */
        // TEXT DECLARATION 
        val ID = makeID(DefaultUncommonSkill::class.java.simpleName)
        val IMG = makeCardPath("Skill.png")

        // /TEXT DECLARATION/
        // STAT DECLARATION 	
        private val RARITY = CardRarity.UNCOMMON
        private val TARGET = CardTarget.SELF
        private val TYPE = CardType.SKILL
        val COLOR = Yoimiya.Enums.COLOR_ORANGE
        private const val COST = 1
        private const val UPGRADE_REDUCED_COST = 0
        private const val BLOCK = 1
        private const val UPGRADE_PLUS_BLOCK = 2
    }
}