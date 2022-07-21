package slayTheMiko.cards

import basemod.abstracts.CustomCard
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import slayTheMiko.SlayTheMikoMod.Companion.makeCardPath
import slayTheMiko.SlayTheMikoMod.Companion.makeID
import slayTheMiko.characters.Yoimiya

class YoimiyaAttack : CustomCard(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET) {
    // IMPORTANT NOTE: If you add parameters to your constructor, you'll crash the auto-add cards with a
    // `NoSuchMethodException` because it except a constructor with no params.
    // You have two options:
    // 1. Create a new constructor with empty parameters call your custom one with default params in it
    // 2. Mark the card with @AutoAdd.NotSeen (https://github.com/daviscook477/BaseMod/wiki/AutoAdd) to prevent it from
    // being auto-add it, and then load it manually with
    // BaseMod.addCard(new DefaultCommonAttack());
    // UnlockTracker.unlockCard(DefaultCommonAttack.ID);
    // in your main class, in the receiveEditCards() method
    init {
        baseDamage = DAMAGE
        tags.add(CardTags.STARTER_STRIKE)
        tags.add(CardTags.STRIKE)
    }

    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        AbstractDungeon.actionManager.addToBottom(
            DamageAction(
                m,
                DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            )
        )
    }

    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeDamage(UPGRADE_PLUS_DMG)
            initializeDescription()
        }
    }

    companion object {
        val ID = makeID(YoimiyaAttack::class.java.simpleName)
        private val cardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
        val IMG = makeCardPath("Attack.png")
        val NAME = cardStrings.NAME
        val DESCRIPTION = cardStrings.DESCRIPTION

        // STAT DECLARATION
        private val RARITY = CardRarity.BASIC
        private val TARGET = CardTarget.ENEMY
        private val TYPE = CardType.ATTACK
        val COLOR = Yoimiya.Enums.COLOR_ORANGE
        private const val COST = 1
        private const val DAMAGE = 6
        private const val UPGRADE_PLUS_DMG = 3
    }
}