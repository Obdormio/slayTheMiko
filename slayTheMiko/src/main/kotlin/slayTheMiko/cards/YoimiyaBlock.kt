package slayTheMiko.cards

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import slayTheMiko.SlayTheMikoMod.Companion.makeCardPath
import slayTheMiko.SlayTheMikoMod.Companion.makeID
import slayTheMiko.characters.Yoimiya

class YoimiyaBlock : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    init {
        baseBlock = BLOCK
        tags.add(CardTags.STARTER_DEFEND)
    }

    // Actions the card should do.
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        AbstractDungeon.actionManager.addToBottom(GainBlockAction(p, p, block))
    }

    //Upgraded stats.
    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeBlock(UPGRADE_PLUS_BLOCK)
            initializeDescription()
        }
    }

    companion object {
        val ID = makeID(YoimiyaBlock::class.java.simpleName)
        val IMG = makeCardPath("Block.png")
        private val RARITY = CardRarity.BASIC
        private val TARGET = CardTarget.SELF
        private val TYPE = CardType.SKILL
        val COLOR = Yoimiya.Enums.COLOR_ORANGE
        private const val COST = 1
        private const val BLOCK = 5
        private const val UPGRADE_PLUS_BLOCK = 3
    }
}