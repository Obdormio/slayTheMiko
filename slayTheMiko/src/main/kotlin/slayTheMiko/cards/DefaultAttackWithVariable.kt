package slayTheMiko.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.ui.panels.EnergyPanel
import slayTheMiko.SlayTheMikoMod.Companion.makeCardPath
import slayTheMiko.SlayTheMikoMod.Companion.makeID
import slayTheMiko.characters.Yoimiya

class DefaultAttackWithVariable : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    // /STAT DECLARATION/
    init {
        baseDamage = DAMAGE
        isMultiDamage = true
    }

    // Actions the card should do.
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        // Create an int which equals to your current energy.
        val effect = EnergyPanel.totalCount

        // For each energy, create 1 damage action.
        for (i in 0 until effect) {
            AbstractDungeon.actionManager.addToBottom(
                DamageAction(
                    m, DamageInfo(p, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.FIRE
                )
            )
        }
    }

    // Upgraded stats.
    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeDamage(UPGRADE_PLUS_DMG)
            initializeDescription()
        }
    }

    companion object {
        /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Special Strike: Deal 7 (*) damage times the energy you currently have.
     */
        // TEXT DECLARATION
        val ID = makeID(DefaultAttackWithVariable::class.java.simpleName)
        val IMG = makeCardPath("Attack.png")

        // /TEXT DECLARATION/
        // STAT DECLARATION
        private val RARITY = CardRarity.COMMON
        private val TARGET = CardTarget.ENEMY
        private val TYPE = CardType.ATTACK
        val COLOR = Yoimiya.Enums.COLOR_ORANGE
        private const val COST = 1
        private const val DAMAGE = 7
        private const val UPGRADE_PLUS_DMG = 1
    }
}