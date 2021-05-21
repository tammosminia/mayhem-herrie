import java.util

object Hero {
  def freshCopy(hero: Hero): Hero = Hero(hero.name, hero.health, hero.power, hero.powerColor, hero.regeneration, hero.armor, hero.resistance, hero.skills)

  case class Buff(`type`: Skill.EffectType.Value, effect: Int, started: Long, timeout: Long)

  object Skill {
    object EffectType extends Enumeration {
      type EffectType = Value
      val health, power, armor, resistance = Value
    }

    object AllowedTarget extends Enumeration {
      type AllowedTarget = Value
      val self, others, all = Value
    }
  }

  case class Skill(id: Int, name: String, shout: String, power: Int, delay: Int, cooldown: Int, duration: Int, effect: Int, `type`: Skill.EffectType.Value, allowedTarget: Skill.AllowedTarget.Value) {
    if (duration == 0 && ((`type` eq Skill.EffectType.armor) || (`type` eq Skill.EffectType.resistance))) throw new IllegalArgumentException("(de)buffs must have a duration specified")

    override def toString: String = name
  }
}

case class Hero(name: String, health: Int, power: Int, powerColor: String, regeneration: Int, armor: Int, resistance: Int, skills: List[Hero.Skill],
                id: Int = 0, buffs: Map[String, Hero.Buff] = Map(), cooldowns: Map[Integer, Long] = null, currentSkill: Int = -1, currentStarted: Long = -1,
                maxHealth: Int = 0, maxPower: Int = 0) {
  def isAlive: Boolean = health > 0
}
