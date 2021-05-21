import Hero.Skill
import Hero.Skill.AllowedTarget
import Messages.FightStatus.{fighting, overtime}
import Messages.{ActionMessage, StatusMessage}

import scala.util.Random

object Fight {
  def randomFromList[T](l: List[T]): Option[T] = if (l.isEmpty) None else Some(l(Random.nextInt(l.length)))

  def chooseSkill(h: Hero, sm: StatusMessage): Option[Skill] = {
    val allowed = h.skills.filter(_.cooldown == 0).filter(_.power + h.power >= 0)
    randomFromList(allowed)
  }

  def killTheHipster(sm: StatusMessage): Option[Hero] = sm.opponents.filter(_.isAlive).minByOption(h => h.health + h.armor + h.resistance)

  def saveTheHipster(sm: StatusMessage, heroes: List[Hero]): Option[Hero] = heroes.filter(_.isAlive).minByOption(h => h.health + h.armor + h.resistance)

  def preventOverHeal(s: Skill, targets: List[Hero]) =
    s.`type` match {
      case Hero.Skill.EffectType.health => targets.filter(h => s.effect <= h.missingHealth)
      case _                            => targets
    }

  def chooseTarget(h: Hero, s: Skill, sm: StatusMessage): Option[Hero] =
    s.allowedTarget match {
      case AllowedTarget.self                    => Some(h)
      case AllowedTarget.others if s.isPleasant  => saveTheHipster(sm, preventOverHeal(s, sm.you.filter(_ != h)))
      case AllowedTarget.all if s.isPleasant     => saveTheHipster(sm, preventOverHeal(s, sm.you))
      case AllowedTarget.others if !s.isPleasant => killTheHipster(sm)
      case AllowedTarget.all if !s.isPleasant    => killTheHipster(sm)
    }

  def actionForHero(h: Hero, sm: StatusMessage): Option[ActionMessage] = {
    if (!h.isAlive || h.isBusy) None
    else {
      for {
        skill <- chooseSkill(h, sm)
        target <- chooseTarget(h, skill, sm)
      } yield ActionMessage(h.id, skill.id, target.id, false)
    }
  }

  val canFight = List(fighting, overtime)
  def actionForStatus(sm: StatusMessage): List[ActionMessage] = {
    if (canFight.contains(sm.status)) {
      sm.you.flatMap(h => actionForHero(h, sm))
    } else {
      List()
    }
  }

}
