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

    合法性判断该放在Client还是放在Server呢？

    - 瘦客户端方案：客户端在走棋之后将走棋动作提交给服务器，服务器进行合法性审查、
    - 胖客户端方案：服务器只是公共的数据平台，客户端自行判断

    不过不管什么方案都有一个问题无法绕开：如何让服务器在一个客户端修改走棋之后通知另外一个客户端？

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



## 前端

左边棋盘右边操作盘，（个人趣味：操作盘上方三盏灯，绿色走棋、黄色等待、红色游戏结束）

IDLE灭灯，PARING和WAITING黄灯，ACTION绿灯，GAMEOVER红灯

也许下方可以弄个状态条Banner



学习列表

1. Kotlin
2. HTTP4K

3. A effective way to build GUI using Kotlin
4. (Optional) 3D Graphics and animation (Maybe we can use material design)



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

### 同时运行同一目标的多个实例

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
