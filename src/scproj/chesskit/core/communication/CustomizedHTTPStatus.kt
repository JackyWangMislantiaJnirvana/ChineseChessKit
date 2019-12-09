package scproj.chesskit.core.communication

import org.http4k.core.Status

// Server echos for movement post
val INVALID_PLAYER_SIDE = Status.OK.description("Invalid player side")
val UNRECOGNIZED_MOVEMENT = Status.OK.description("Unrecognized movement")
val INVALID_CHESS_MOVEMENT = Status.OK.description("This movement is against the chess game rule")
val MOVEMENT_SUCCESS = Status.OK.description("Chess moved successfully")

// Server echos for observe get
val NOT_STARTED = Status.OK.description("No game started")
val RED_IN_ACTION = Status.OK.description("Red in action")
val BLACK_IN_ACTION = Status.OK.description("Black in action")
//val GAME_OVER = Status.OK.description("Game over")
val RED_WON = Status.OK.description("Red won")
val BLACK_WON = Status.OK.description("Black won")

// Game registration
val PARING_COMPLETE = Status.OK.description("Pairing complete")
val WAITING_FOR_OPPONENT = Status.OK.description("Registered, waiting for opponent")
val SIDE_OCCUPIED = Status.OK.description("Side already occupied")

infix fun Status.means(status: Status) =
    status == this && status.description == this.description