package slayTheMiko.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import slayTheMiko.SlayTheMikoMod;
import slayTheMiko.characters.Yoimiya;

import static slayTheMiko.SlayTheMikoMod.makeCardPath;

public class YoimiyaBlock extends AbstractDynamicCard {
    public static final String ID = SlayTheMikoMod.makeID(YoimiyaBlock.class.getSimpleName());
    public static final String IMG = makeCardPath("Block.png");
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Yoimiya.Enums.COLOR_ORANGE;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public YoimiyaBlock() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;

        this.tags.add(CardTags.STARTER_DEFEND);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
