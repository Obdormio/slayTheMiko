package slayTheMiko.relics

import basemod.abstracts.CustomRelic
import com.badlogic.gdx.graphics.Texture
import slayTheMiko.SlayTheMikoMod
import slayTheMiko.util.TextureLoader

class YoimiyaEye : CustomRelic(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL) {
    companion object {
        val ID = SlayTheMikoMod.makeID("YoimiyaEye")
        val IMG: Texture = TextureLoader.getTexture(SlayTheMikoMod.makeRelicPath(("YoimiyaEye.png")))
        val OUTLINE: Texture = TextureLoader.getTexture(SlayTheMikoMod.makeRelicOutlinePath("YoimiyaEye.png"))
    }
}
