package scproj.chesskit.core.communication

import org.http4k.core.Status

// Server echos for movement post
val INVALID_PLAYER_SIDE = Status.NOT_FOUND.description("Invalid player side")
val UNRECOGNIZED_MOVEMENT = Status.UNSATISFIABLE_PARAMETERS.description("Unrecognized movement")
val INVALID_CHESS_MOVEMENT = Status.FORBIDDEN.description("This movement is against the chess game rule")
val MOVEMENT_SUCCESS = Status.OK.description("Chess moved successfully")

// Server echos for observe get
val NOT_STARTED = Status.NO_CONTENT.description("No game started")
val RED_IN_ACTION = Status.OK.description("Red in action")
val BLACK_IN_ACTION = Status.OK.description("Black in action")
val GAME_OVER = Status.OK.description("Game over")

// Game registration
val PARING_COMPLETE = Status.OK.description("Pairing complete")
val WAITING_FOR_OPPONENT = Status.ACCEPTED.description("Registered, waiting for opponent")

infix fun Status.means(status: Status) =
    status == this && status.description == this.description