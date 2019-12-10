import javafx.geometry.Point2D
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.util.Duration
import mu.KotlinLogging
import org.http4k.client.JavaHttpClient
import org.http4k.core.Method
import org.http4k.core.Request
import tornadofx.*

val klogger = KotlinLogging.logger { }

class DemoCanvas : View() {
    val testBackgroundURI = DemoCanvas::class.java.getResource("test_bg.png")
    val testBackground = Image(testBackgroundURI.toExternalForm())
    val testForeground = Image(DemoCanvas::class.java.getResource("Missing_Texture_Block.png").toExternalForm())
    override val root = borderpane {
        center {
            canvas {
                width = 1000.0
                height = 500.0

                val gc = graphicsContext2D
                gc.drawImage(testBackground, 0.0, 0.0)
                gc.drawImage(testForeground, 50.0, 50.0)
                gc.strokeText("233", 100.0, 100.0)
            }
        }
    }
}

class DemoImageView : View() {
    val img = Image(DemoImageView::class.java.getResource("Missing_Texture_Block.png").toExternalForm())
    var distance: Point2D? = null
    override val root = hbox {
        imageview(img) {
            setOnKeyTyped {
                println(it.character)
                move(Duration(1000.0), javafx.geometry.Point2D(20.0, 20.0))
            }
            setOnMousePressed {
                distance = Point2D(it.getSceneX(), it.getSceneY());
                distance = distance?.subtract(localToScene(Point2D(layoutX, layoutY)));
            }
            setOnMouseDragged {
                if (it.isPrimaryButtonDown) {
                    var p = javafx.geometry.Point2D(it.sceneX, it.sceneY)
                    p = sceneToLocal(p.subtract(distance))
                    layoutX = p.x
                    layoutY = p.y
                }
            }
        }
    }
}

class AnchoredImageView : View() {
    val bg = Image(DemoImageView::class.java.getResource("test_bg.png").toExternalForm())
    val fg = Image(DemoImageView::class.java.getResource("Missing_Texture_Block.png").toExternalForm())
    override val root = anchorpane {
        minWidth = 100.0
        minHeight = 100.0
        setOnMouseClicked {
            println("x=${it.x}, screenX=${it.screenX}, sceneX=${it.sceneX}")
        }
    }
}

class PaneImageMovingView : View() {
    val fg1 = Image(DemoImageView::class.java.getResource("Missing_Texture_Block.png").toExternalForm())
    val fg2 = Image(DemoImageView::class.java.getResource("Missing_Texture_Block.png").toExternalForm())
    val bg = Image(DemoImageView::class.java.getResource("test_bg.png").toExternalForm())
    override val root = pane {
        minHeight = 500.0
        minWidth = 990.0
        var selected: ImageView? = null
        imageview(bg) {
            setOnMouseClicked {
                selected!!.move(Duration(5000.0), Point2D(it.x - 150.0, it.y - 150.0))
                klogger.debug { "$selected moved to space" }
                selected = null
                klogger.debug { "selection released" }
            }
            toBack()
        }
        imageview(fg1) {
            setOnMouseClicked {
                print("")
                if (selected == null) {
                    toFront()
                    selected = this
                    klogger.debug { "fg1 selected" }
                } else {
                    selected!!.move(Duration(1000.0), Point2D(it.x - 150.0, it.y - 150.0))
                    klogger.debug { "$selected moved to fg1" }
                    selected = null
                    klogger.debug { "selection released" }
                }
            }
        }
        imageview(fg2) {
            setOnMouseClicked {
                print("")
                if (selected == null) {
                    toFront()
                    selected = this
                    klogger.debug { "fg2 selected" }
                } else {
                    selected!!.move(Duration(1000.0), Point2D(it.x - 150.0, it.y - 150.0))
                    klogger.debug { "$selected moved to fg2" }
                    selected = null
                    klogger.debug { "selection released" }
                }
            }
        }
        button("move1") {
            var moveCount = 1
            action {
                selected!!.x += 10.0
                selected!!.y += 10.0
                selected!!.move(Duration(1000.0), Point2D(moveCount * 20.0, moveCount * 20.0))
                moveCount++
            }
        }
    }
}

class ImageMovingView : View() {
    val fg1 = Image(DemoImageView::class.java.getResource("Missing_Texture_Block.png").toExternalForm())
    override val root = pane {
        val i = imageview(fg1) {
            layoutX = -300.0
            layoutY = -300.0
            setOnMouseClicked {
                klogger.debug { "x=$x, y=$y" }
//                layoutX = 300.0
//                layoutY = 0.0
                move(Duration(1000.0), Point2D(321.0, 0.0))
//                x = 321.0
//                y = 0.0
//                relocate(321.0, 0.0)
                klogger.debug { "x=$x, y=$y" }
            }
        }
        button("move in") {
            action {
                i.move(Duration(1000.0), Point2D(300.0, 300.0))
                klogger.debug { "x=${i.x}, y=${i.y}" }
            }
        }
    }
}

// 棋子移动，吃子
class ProperImageMovingView : View() {
    val foregroundImg1 = Image(DemoImageView::class.java.getResource("Missing_Texture_Block.png").toExternalForm())
    val foregroundImg2 = Image(DemoImageView::class.java.getResource("Missing_Texture_Block_2.png").toExternalForm())
    val backgroundImg = Image(DemoImageView::class.java.getResource("test_bg.png").toExternalForm())
    var selected: ImageView? = null
    override val root = pane {
        imageview(backgroundImg) {
            setOnMouseClicked { event ->
                if (selected != null) {
                    println("moving to blank")
                    selected!!.move(Duration(500.0), Point2D(event.sceneX - 150, event.sceneY - 150))
                    selected = null
                } else {
                    println("not selected")
                }
            }
        }
        imageview(foregroundImg1) {
            setOnMouseClicked { event ->
                if (selected == null) {
                    println("fg1 selected")
                    toFront()
                    selected = this
                } else {
                    println("eating to fg1")
                    selected!!.move(Duration(1000.0), Point2D(translateX, translateY))
                    isVisible = false
                    selected = null
                }
            }
        }
        imageview(foregroundImg2) {
            setOnMouseClicked { event ->
                if (selected == null) {
                    println("fg2 selected")
                    toFront()
                    selected = this
                } else {
                    println("eating to fg2")
                    selected!!.move(Duration(1000.0), Point2D(translateX, translateY))
                    isVisible = false
                    selected = null
                }
            }
        }
    }
}

// 通过轮询改变棋盘状态
class AsyncPollingView : View() {
    val img = Image(DemoImageView::class.java.getResource("Missing_Texture_Block.png").toExternalForm())
    val controller = AsyncController()
    override val root = pane {
        imageview(img) {
            //            x = 0.0
//            y = 0.0
            move(Duration(5000.0), Point2D(0.0, 0.0))
            runAsync {
                while (true) {
                    when (controller.queryState()) {
                        "true" -> move(Duration(1000.0), Point2D(300.0, 300.0))
                        "false" -> move(Duration(1000.0), Point2D(0.0, 300.0))
                        else -> println("fuck you")
                    }
                    Thread.sleep(1000)
                }
            }
        }
    }
}

class AsyncController {
    val client = JavaHttpClient()
    fun queryState() = client(Request(Method.GET, "http://localhost:9000/")).bodyString()
}