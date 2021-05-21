import Hero.{Buff, Skill}
import Hero.Skill.{AllowedTarget, EffectType}
import Hero.Skill.EffectType.EffectType

import java.util.Date
//import sttp.client3._
//import sttp.client3.sprayJson._
import spray.json._
import spray.json.DefaultJsonProtocol._

object Messages {
  abstract class OutputMessage {
//    def getTimestamp: Date
  }
  abstract class InputMessage

  case class ActionMessage(hero: Int, skill: Int, target: Int, `override`: Boolean, `type`: String = "action") extends InputMessage
  case class RegisterMessage(name: String, email: String, password: String, `type`: String = "register") extends InputMessage

  case class WelcomeMessage(timestamp: Long) extends OutputMessage
  case class AcceptMessage(timestamp: Long) extends OutputMessage
  case class ErrorMessage(message: String, timestamp: Long) extends OutputMessage

  case class StatusMessage(
      you: List[Hero],
      opponent: Option[List[Hero]],
      status: FightStatus.Value,
      result: Option[FightResult.Value],
      timestamp: Long
  ) extends OutputMessage {
    override def toString: String = {
      "StatusMessage{" + "you=" + you + ", opponent=" + opponent + ", status=" + status + ", result=" + result + ", timestamp=" + timestamp + '}'
    }

    def opponents: List[Hero] = opponent.getOrElse(List())
  }

  object FightStatus extends Enumeration {
    type FightStatus = Value
    val idle, ready, fighting, overtime, finished = Value
  }
  object FightResult extends Enumeration {
    type FightResult = Value
    val win, loss = Value
  }

  implicit def enumFormat[T <: Enumeration](implicit enu: T): RootJsonFormat[T#Value] = {
    new RootJsonFormat[T#Value] {
      def write(obj: T#Value): JsValue = JsString(obj.toString)
      def read(json: JsValue): T#Value = {
        json match {
          case JsString(txt) => enu.withName(txt)
          case somethingElse => throw DeserializationException(s"Expected a value from enum $enu instead of $somethingElse")
        }
      }
    }
  }
  implicit def jsonEffectType = enumFormat(EffectType)
  implicit def jsonAllowedTarget = enumFormat(AllowedTarget)
  implicit def jsonFightStatus = enumFormat(FightStatus)
  implicit def jsonFightResult = enumFormat(FightResult)

  implicit val jsonBuff: JsonFormat[Buff] = jsonFormat4(Buff.apply)
  implicit val jsonSkill: JsonFormat[Skill] = jsonFormat10(Skill.apply)
  implicit val jsonHero: JsonFormat[Hero] = jsonFormat15(Hero.apply)
  implicit val jsonWelcome: JsonFormat[WelcomeMessage] = jsonFormat1(WelcomeMessage)
  implicit val jsonRegister: JsonFormat[RegisterMessage] = jsonFormat4(RegisterMessage)
  implicit val jsonAccept: JsonFormat[AcceptMessage] = jsonFormat1(AcceptMessage)
  implicit val jsonStatus: JsonFormat[StatusMessage] = jsonFormat5(StatusMessage)
  implicit val jsonError: JsonFormat[ErrorMessage] = jsonFormat2(ErrorMessage)
  implicit val jsonAction: JsonFormat[ActionMessage] = jsonFormat5(ActionMessage)
}
