import Hero.Skill
import Hero.Skill.AllowedTarget
import Messages.FightStatus.{fighting, overtime, ready}
import Messages.{ActionMessage, StatusMessage}

import scala.util.Random

object Fight {
  def randomFromList[T](l: List[T]): Option[T] = if (l.isEmpty) None else Some(l(Random.nextInt(l.length)))

  def chooseTarget(h: Hero, s: Skill, sm: StatusMessage): Option[Hero] =
    s.allowedTarget match {
      case AllowedTarget.self                    => Some(h)
      case AllowedTarget.others if s.isPleasant  => randomFromList(sm.you.filter(_ != h))
      case AllowedTarget.others if !s.isPleasant => randomFromList(sm.opponents)
      case AllowedTarget.all if s.isPleasant     => randomFromList(sm.you)
      case AllowedTarget.all if !s.isPleasant    => randomFromList(sm.opponents)
    }

  def actionForHero(h: Hero, sm: StatusMessage): Option[ActionMessage] =
    for {
      skill <- randomFromList(h.skills)
      target <- chooseTarget(h, skill, sm)
    } yield ActionMessage(h.id, skill.id, target.id, false)

  val canFight = List(fighting, overtime)
  def actionForStatus(sm: StatusMessage): List[ActionMessage] = {
    if (canFight.contains(sm.status)) {
      sm.you.flatMap(h => actionForHero(h, sm))
    } else {
      List()
    }
  }

}
