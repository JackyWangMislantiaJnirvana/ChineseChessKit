#  Project Plan

- Main entry of the program must be written in Java

- Glue code, logic, serialization, networking and GUI will be implemented by Kotlin.
- Algorithms will be implemented mainly in Java, maybe be optimized by parallel Clojure.



## 架构

- 核心数据结构：走棋动作

  - 棋局状态用走棋动作的有序集合表示（这样就支持悔棋了）（还可以棋局回放）

  - 每条走棋动作都可以序列化以用网络传输
  - 每当一方走一步棋，就将走棋动作添加到状态集合

  - 整个棋局状态集合可以整体序列化，也可以被（高效地？）转化成当前状态矩阵，供搜索算法使用

  - 走棋动作的结构：

    ```json
    {
      "player":"RED",
      "timestamp":"2016-01-07T00:00+0800",
      "from": [1, 2],
      "to": [2, 2],
      "is_undo": false
    }
    ```

    不区分移动和吃子。如果棋子移动合法，而且目的地有对方棋子，就认为吃掉。

    （这里有个问题？一条走棋动作记录并不能保证走棋动作的合法性，如：

    1. 尝试把空气四处移动
    2. 动了别人的子
    3. 到处乱飞
    4. 吃了自己的子

    合法性判断放在客户端的Controller里，相关算法分离成纯函数放在core里。这样可以保证高响应性。（服务端仍然不可避免地在使用ChessGrid结构来判断胜负）

    客户端在等待对方走棋的状态时，定时轮询。轮询吧轮询吧轮询吧
  
- 架构：C/S

  服务端，本地客户端，远程客户端

- 我将HTTP作为局域网联机的通讯协议，这个馊主意行么？（就这么定了

- 关于悔棋：通过动作序列来刻画棋盘状态时，重建棋盘会丢失关于“那些棋子被吃掉”的信息。所以只能通过再次从头重建棋盘来回到上一个状态。如何代表悔棋？

  控制权在自己手里时可以悔棋，直接把自己的上一步和对手的上一步全部去掉。（去掉现有列表的最新两个元素。）（SerialNumber - 1）（如果列表长度为1，则悔棋失败。）回放的时候不提供悔棋的信息。


### 利用HTTP进行游戏通讯

#### 客户端的状态

- Idle （刚打开，还没开始登陆服务器）
- Paring （成功登陆服务器并选择颜色，正在等待另一个颜色的玩家参与）
- Wating （等待另一个玩家走棋）
- Action （等待本地玩家走棋）
- GameOver （决出胜负）

#### 通信时序

1. 一方玩家登入服务器：
   1. 客户端GET，服务器返回`status`
   2. 客户端POST`join(color)`，服务端返回`ok(color)`或者`fail(reason=color_occupied)`
   3. 如果`ok`，客户端进入`paring`状态，定时发送GET，询问配对是否结束
2. 另一方玩家登入服务器：
   1. 客户端GET，服务器返回`status`
   2. 客户端POST`join(color)`,服务端返回`ok(color)`或者`fail(reason=color_occupied)`
   3. 如果`ok`，配对结束。服务器通过`ok`中的报文通知第二个进入游戏的玩家配对结束，通过GET的内容通知第一个进入游戏的玩家配对结束
3. 对战循环：
   1. 初始状态：红方玩家进入`action`状态。黑方玩家进入`waiting`状态，通过GET定时轮询
   2. A方玩家将走棋动作POST给服务器，如果合法，服务器回复`ok`和动作后的棋局，否则返回`fail(reason=invalid_movement)`。A方反复POST直到第一次收到`ok`，进入`waiting`。B方得知变化，进入`action`。
   3. 反复以上过程，直到一次movement使得服务器判定棋局将死。服务器分别通过POST response和GET response通知双方胜负情况和棋局状况。
4. 双方客户端转为`gameover`，显示胜负。可以通过棋局状况进行复盘分析。

#### 数据包结构

服务器与客户端通信使用plan old post method，即MIME: x-www-form-encoded

#### 状态通信

C/S通信中，以自定义的一套HTTP状态码实现一状态通信。这些状态码衍生自语义不冲突的标准HTTP状态码，对Descrition字段进行了修改。如：

```kotlin
val MY_CUSTOMIZED_STATE = State.OK.description("WOW! It works!")
```

由于http4k的Status类提供的`==`运算符只比较状态码的Code不比较Description，易引起混淆，于是自定义了一个infix函数用于判断两个状态是否这两个属性都相等

```kotlin
// 假设Server返回了MY_CUSTOMIZED_STATE
// 库重载的运算符`==`只判断code是否相同，无法区分标准State和这个自定义State
networkResponse.status == Status.OK // => true
networkResponse.status == MY_CUSTOMIZED_STATE // => true

// 用自定义的infix函数即可区分
networkResponse.status means Status.OK // => true
networkResponse.status means MY_CUSTOMIZED_STATE // => false
```

HTTP状态码被我这么玩其实是不对的，因为标准客户端可能会误解HTTP状态代码的含义。（比如说原定当游戏未开始时返回204 No content，结果客户端真的就不接受content了= =也许这是应为我对状态码有什么误解。状态码应该是用来在标准服务器和标准客户端之间通讯的，我这个建立在HTTP层之上的应用不应该去干扰HTTP通信。）但是再自己弄一个状态传递很烦人，于是折衷，所有状态通讯使用的状态码都统一成200（表示OK），但是用description传递来自应用的消息。虽然这样有点脏，但是时间要紧。

## 前端

左边棋盘右边操作盘，（个人趣味：操作盘上方三盏灯，绿色走棋、黄色等待、红色游戏结束）

IDLE灭灯，PARING和WAITING黄灯，ACTION绿灯，GAMEOVER红灯

也许下方可以弄个状态条Banner

### 棋盘绘图解决方案

利用pane面板作为基底，上面放一张背景图作棋盘。每个棋子用一个imageview表示。所有棋子的imageview都堆在视野外的同一个点上。开局时，所有棋子移动到相应位置。

view持有一个用来保存当前选中棋子的nullable的句柄。选中的棋子发光。选中之后，如果背景图接受点击，它就把棋子移动到相应的格点上（合法移动判断）（坐标计算）。如果棋子接受点击，它就（在判断合法之后）将选中的棋子移动到自己身上，自己变成invisible。

### 动画解决方案

TornadoFX在所有的builder闭包里埋入了move函数，封装了一个javafx的translation，可以实现带动画的平移变换。（注意，这个动画是和translation绑定的，实际上控件的layout位置没有丝毫变化）动画是预设好的，以后考虑加入自定义的贝塞尔曲线动画。

### 异步轮询解决方案（有滥用API之嫌疑）

通过tornadoFX提供的runAsync函数（背后封装了一个独立线程），在后台运行一个轮询死循环。在此死循环内，通过view类持有的controller对象（网络通讯、状态保存等被封装在Controller中，controller在运行时通过依赖注入注入），对服务器进行定时轮询。

（可能是滥用API的报应吧，如果这个后台线程休眠时间太短，会干扰到动画的放映（一卡一卡的）。所以死循环的休眠时间得长一点（500ms真的会死），动画播放得快一点）

##技术栈概览

1. Kotlin
2. HTTP4K：服务器
3. TornadoFX/JavaFX：GUI
4. Moshi JSON：序列化
5. KotlinLogging：日志

## 开发记录

### Logging

- 使用Kotlin-logging
- Kotlin-logging封装了slf4j
- slf4j需要一个日志框架作为实际实现。相对应，需要安装一个对应的slf4j binding
- 选择log4j作为框架
- 安装`slf4j-log4j12-1.7.28`，maven附带安装了log4j
- 在src（反正是被class loader认为是resource路径了，魔法）底下创建`log4j.properties`作为配置文件，主要用来配置日志pattern、输出位置和日志等级。https://blog.csdn.net/eagleuniversityeye/article/details/80582140

```properties
log4j.rootLogger = TRACE, console

log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern =  %d{yyy-MM-dd HH:mm:ss} [%-5p] %c{1}:%L - %m%n

# 效果：2019-11-09 00:05:10 [TRACE] DemoLogger:7 - wow
```

### 调教HTTP Server

#### 怎么架服务器？

```kotlin
// 定义服务器的行为
val app: HttpHandler = routes(
  "/ping" bind GET to { _: Request -> Response(OK).body("pong!")},
  "/greet/{name}" bind GET to { req: Request ->
   val name: String? = req.path("name")
   Response(OK).body("hello ${name ?: "anon!"}")}
)

// 架起来！
app.asServer(Jetty(9000)).start()
```

（目前Filter好像还用不上，先不管它）

#### Client从Server Get信息

```kotlin
val networkResponse: Response = client(Request(GET, "http://localhost:9000/greet/Bob"))

// 通过这种方式获取get中搭载的字符串
val payload: String = networkResponse.bodyString()
```

#### Server回复Client的Get

```kotlin
"/ping" bind GET to { _: Request -> Response(OK).body("pong!")}
```

#### Client向Server Post信息

```kotlin
val client: HttpHandler = JavaHttpClient()
val networkResponse: Response = client(
  Request(POST, "http://localhost:9000/play/RED")
  .query("q1", "not wow")
)
```

#### Server接收Client Post的信息 

```kotlin
val server: HttpHandler = routes(
  "/play/{side}" bind POST to { req: Request ->
  	Response(OK).body(req.query("q1")?:"wow")
  },
  "/observe" bind GET to { Response(OK).body("") }
)


```

#### Filter

```kotlin
filter1.then(filter2).then(filter3).then(app)
```

filter会流水线处理输入的`HttpHandler`

###同时运行同一目标的多个实例

https://blog.csdn.net/wait_for_eva/article/details/86557930

![](https://img-blog.csdnimg.cn/20190119223708925.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dhaXRfZm9yX2V2YQ==,size_16,color_FFFFFF,t_70)

勾选Allow parallel run即可启动一堆实例

![Final](https://img-blog.csdnimg.cn/20190119223725150.png)

### JSON序列化/反序列化

### 需求

需要对棋局的状态进行序列化和反序列化。

```json
{
  "uid" : "",
  "previous_uid" : "",
  "movement_sequnce": [
    {
      "player":"RED",
      "timestamp":"2016-01-07T00:00+0800",
      "from": [1, 2],
      "to": [2, 2],
      "is_undo": false
    }
    //...
  ]
}
```

### 解析built-in类型

没有自定义处理器，直接将json串中的数据对应到data class中的Kotlin自带类型

```kotlin
fun main() {
    val jsonString = """
        {
        "url": "https://api.github.com/repos/square/okio/issues/156",
        "id": 91393390,
        "number": 156,
        "title": "ByteString CharSequence idea",
        "State": "open",
        "created_at": "2015-06-27T00:49:40.000Z",
        "body": "Let's make CharSequence that's backed by bytes.\n"
        }
    """
    println(System.currentTimeMillis())
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val issueAdapter = moshi.adapter(Issue::class.java)
    println(System.currentTimeMillis())
  	// 自动创建Adapter用了Kotlin Reflection，用时370ms...
  	// 这慢的，都是十万微秒的级别了
  	// 如果用上了自定义Adapter，可能就不会这么慢了

    val issue = issueAdapter.fromJson(jsonString)
  	// 转换用时3ms

    println(issue)
}

data class Issue(
    val url: String,
    val id: Long,
    val number: Long,
    val title: String,
    val State: String,
    val created_at: String,
    val body: String
)
```

### 解析自定义类型

待解析的json串中有些字段的类型不是moshi自动支持的类型，为了支持这些类型需要自己实现该类型对应的Adapter。比如要支持如下的枚举类：

```kotlin
enum class State {
    OPEN, CLOSED
}
```

只需写这么一个Adapter：

```kotlin
class StateAdapter() {
    @ToJson
    fun toJson(state: State): String = state.toString()

    @FromJson
    fun fromJson(state: String): State = when (state) {
        "OPEN" -> State.OPEN
        "CLOSED" -> State.CLOSED
        else -> throw JsonDataException("unknown state: $state")
    }
}
```

然后注册该Adapter：

```kotlin
val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
            // Ahhhh, reflect magic!!!!
        .add(StateAdapter())
        .build()
```

连继承都省了？？？反射和注解真是魔法。另外，如果有嵌套的data class的话，什么也不用做...反射是真的方便。

### 客户端GUI开发（TornadoFX)

#### 耗时长的操作一定要Async

```kotlin
class AsyncView: View() {
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
                }
            }
        }
    }
}

class SleepingController: Controller() {
    fun loadText(): String {
        Thread.sleep(5000)
        return "Loaded text"
    }
}
```

如果直接在`action` builder里执行`loadText()`，程序就无响应了（会被OS鄙视

#### TornadoFX的MVC架构：View, Controller, Fragment

View和Controller是用action块粘在一起的，在action块中的事件处理lambda调用controller的相应方法以处理输入。将输出反映到View上有两种方法：

1. action块中代码修改view所绑定的property，view上的数据就更新了（推荐）
2. 也可以通过extension lambda的this指针拿到控件本身的引用，通过该引用操作控件。例子：

```kotlin
class StupidButton: View() {
    override val root = button("Click me") {
        action {
            this.isVisible = false
        }
    }
}
```

#### 如何创建新窗口时隐藏旧窗口，在新窗口关闭之后重新显示旧窗口

关闭窗口有两种途径：OS的关闭按钮、某个控件自己调用`close()`把自己关掉。

```kotlin
class ViewWithFragment : View() {
    override val root = vbox {
        minHeight = 160.0
        minWidth = 320.0
        button("Press Me") {
            action {
                primaryStage.hide()
                find<MyFragment>().openModal()!!
              			// 这里保证按叉关闭之后主窗口弹出
                    .setOnCloseRequest { primaryStage.show() }
            }
        }
    }
}

class MyFragment: Fragment() {
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
                  	// 这里保证自主关闭之后主窗口弹出
                    primaryStage.show()
                }
            }
        }
    }
}
```

#### 组件的layout坐标

`x`和`sceneX`是一个东西，指相对于窗体坐标系的坐标

`screenX`指相对于屏幕坐标系的坐标

#### 区分几种造成ImageView中图片移动的方法

1. 设定x和y、设定layoutX和layoutY

这两种方法都是在改imageView在它的parent中的布局位置（layout）。（只有在parent允许它移动的时候才会移动！如在pane中可以通过直接设置它们俩来实现把东西移动到指定坐标，但是在一个vbox中则即使修改，也动不了）

注意！这两种坐标是独立的，作用会叠加（代我向javafx类库设计者的家人问好？）

2. 利用函数relocate和move

这两者都是在对图片进行仿射变换，ImageView本身的布局位置丝毫未变。两种方式达成的效果一致：在该对象的`localToParentTransform`对象中记录一个仿射变换，变换的向量代表元是(xt, yt)

用node的Property：translateX和translateY可以访问目前图片漂哪里去了