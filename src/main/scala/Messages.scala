import java.util.Date

class Messages {
  abstract class OutputMessage {
//    def getTimestamp: Date
  }
  abstract class InputMessage

  case class ActionMessage(hero: Int, skill: Int, target: Int, `override`: Boolean) extends InputMessage
  case class RegisterMessage(name: String, email: String, password: String) extends InputMessage

  case class WelcomeMessage(timestamp: Date) extends OutputMessage
  case class AcceptMessage(timestamp: Date) extends OutputMessage
  case class ErrorMessage(message: String, timestamp: Date) extends OutputMessage
  case class StatusMessage(you: List[Hero], opponent: List[Hero], status: FightStatus.Value, result: FightResult.Value, timestamp: Date) extends OutputMessage {
    override def toString: String = {
      return "StatusMessage{" + "you=" + you + ", opponent=" + opponent + ", status=" + status + ", result=" + result + ", timestamp=" + timestamp + '}'
    }
  }

  object FightStatus extends Enumeration {
    type FightStatus = Value
    val idle, ready, fighting, overtime, finished = Value
  }
  object FightResult extends Enumeration {
    type FightResult = Value
    val win, loss = Value
  }
}
