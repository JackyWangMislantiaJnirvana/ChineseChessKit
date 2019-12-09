import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.TextFormatter
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class MyApp : App(MyView2::class)
class MyApp2 : App(MyView2::class)

class MyView1 : View() {
    override val root = vbox {
        label("View 1")
    }
}

class MyView2 : View() {
    override val root = vbox {
        primaryStage.height = 160.0
        primaryStage.width = 160.0
        button("Press Me")
        label("View 2")
    }
}

class MasterView : View() {
    // Explicitly retrieve TopView
    val topView = find(TopView::class)
    // Create a lazy reference to BottomView
    val bottomView: BottomView by inject()

    override val root = borderpane {
        top = topView.root
        bottom = bottomView.root
    }
}

class MasterView2 : View() {
    override val root = borderpane {
        top<TopView>()
        bottom<BottomView>()
    }
}

class TopView : View() {
    override val root = label("Top View")
}

class BottomView : View() {
    override val root = label("Bottom View")
}

class MyView : View() {
    val controller: MyController by inject()
    val username = SimpleStringProperty()
    val password = SimpleStringProperty()

    override val root = form {
        fieldset {
            field("Username") {
                textfield(username)
            }
            field("Password") {
                passwordfield(password)
            }
            button("Login") {
                action {
                    controller.writeToDb(username.value, password.value)
                    username.value = ""
                    password.value = ""
                }
            }
            spacer {
                spacing = 5.0
            }
            checkbox("Remember me")
        }
    }
}

class View4 : View() {
    val controller: Controller4 by inject()
    override val root = vbox {
        textflow {
            text("Tornado") {
                fill = Color.PURPLE
                font = Font(80.0)
            }
            text("FX") {
                fill = Color.ORANGE
                font = Font(112.0)
            }
        }
        label("My items") {
            textFill = Color.BLUE
            font = Font.font(70.0)
        }
        listview(controller.values)
    }
}

class Controller4 : Controller() {
    val values = FXCollections.observableArrayList("Alpha", "Beta", "Gamma", "Delta")
}

class MyController : Controller() {
    fun writeToDb(username: String, password: String) {
        println("Writing {Username=$username, Password=$password} to database!")
    }
}

class AsyncView : View() {
    val controller: SleepingController by inject()
    val input = SimpleStringProperty()
    override val root = form {
        fieldset {
            field("Input") {
                textfield(input)
            }

            button("Commit") {
                action {
                    runAsync {
                        controller.loadText()
                    } ui { loadedText ->
                        input.value = loadedText
                    }
//                    input.value = controller.loadText()
                }
            }
        }
    }
}

class SleepingController : Controller() {
    fun loadText(): String {
        Thread.sleep(20000)
        return "Loaded text"
    }
}

class StupidButton : View() {
    override val root = button("Click me") {
        action {
            hide()
        }
    }
}


class ViewWithFragment : View() {
    override val root = vbox {
        minHeight = 160.0
        minWidth = 320.0
        button("Press Me") {
            action {
                primaryStage.hide()
                find<MyFragment>().openModal()!!
                    .setOnCloseRequest { primaryStage.show() }
            }
        }
    }
}

class MyFragment : Fragment() {
    override val root = borderpane {
        minHeight = 160.0
        minWidth = 320.0
        center {
            label("I'm a pop-up")
        }
        bottom {
            button("OK") {
                action {
                    close()
                    primaryStage.show()
                }
            }
        }
    }
}

class DemoFlowPane : View() {
    override val root = flowpane {
        for (i in 1..100) {
            button(i.toString()) {
                setOnAction { println("You pressed button $i") }
            }
        }
    }
}

class DemoHbox : View() {
    override val root = hbox {
        button("Button 1") {
            hboxConstraints {
                marginRight = 20.0
                hGrow = Priority.ALWAYS
            }
        }
        button("Button 2")
    }
}

class DemoBorderPane : View() {
    override val root = borderpane {
        top = label("TOP") {
            useMaxWidth = true
            style {
                backgroundColor += Color.RED
            }
        }

        bottom = label("BOTTOM") {
            useMaxWidth = true
            style {
                backgroundColor += Color.BLUE
            }
        }

        left = label("LEFT") {
            useMaxWidth = true
            useMaxHeight = true
            style {
                backgroundColor += Color.GREEN
            }
        }

        right = label("RIGHT") {
            useMaxWidth = true
            useMaxHeight = true
            style {
                backgroundColor += Color.PURPLE
            }
        }

        center = label("CENTER") {
            useMaxWidth = true
            useMaxHeight = true
            style {
                backgroundColor += Color.YELLOW
            }
        }
    }
}

class Person(name: String, age: Int) {
    private val nameProperty = SimpleStringProperty(name)
    private val ageProperty = SimpleIntegerProperty(age)

    var name by nameProperty
    var age by ageProperty
}

class DemoBorderPane2 : View() {
    override val root = borderpane {
        right = vbox {
            button("REFRESH")
            button("COMMIT")
        }

        center = tableview<Person> {
            items = listOf(
                Person("Joe Thompson", 33),
                Person("Sam Smith", 29),
                Person("Nancy Reams", 41)
            ).asObservable()

            column("NAME", Person::name)
            column("AGE", Person::age)
        }
    }
}

class DemoForm : View() {
    val FirstTenFilter: (TextFormatter.Change) -> Boolean = { change ->
        !change.isAdded || change.controlNewText.let {
            it.isInt() && it.toInt() in 0..10
        }
    }
    override val root = form {
        hbox(20) {
            fieldset("Left FieldSet") {
                hbox(20) {
                    vbox {
                        field("Field l1a") {
                            textfield("abc") {
                                filterInput(FirstTenFilter)
                            }
                        }
                        field("Field l2a") { textfield() }
                    }
                    vbox {
                        field("Field l1b") { textfield() }
                        field("Field l2b") { textfield() }
                    }
                }
            }
            fieldset("Right FieldSet") {
                hbox(20) {
                    vbox {
                        field("Field r1a") { textfield() }
                        field("Field r2a") { textfield() }
                    }
                    vbox {
                        field("Field r1b") { textfield() }
                        field("Field r2b") { textfield() }
                    }
                }
            }
        }
    }
}

class DemoClient : View() {
    override val root = borderpane {
        center = canvas {
            width = 800.0
            height = 600.0
            graphicsContext2D.stroke = Color.CHOCOLATE;
            graphicsContext2D.fillRect(0.0, 150.0, 50.0, 50.0);
            graphicsContext2D.strokeRect(100.0, 150.0, 50.0, 50.0);
            graphicsContext2D.save()
        }
        right = form {
            vbox {
                label("Game status: not started")
                label("Side: RED")
                button("exit")
            }
        }
    }
}