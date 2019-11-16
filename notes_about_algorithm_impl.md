# 算法函数接口文档

## 完整示例

```java
public static Movement getNextStep(ChessGrid chessGrid, PlayerSide playerSide) {
  // 这样获取点阵
  ChessGridElement[][] grid = chessGrid.getGrid();
  
  // ......解决问题
  
  // 如何返回结果：new一个Movement对象出来，填入谁走的，从哪里移动到哪里，是否是悔棋
  // 关于如何调用构造函数：
  // Movement(
  //  	PlayerSide 谁走的,
  //  	Integer 从哪个x坐标拿棋子,
  //  	Integer 从哪个y坐标拿棋子,
  //  	Integer 把拿起来的棋子放到哪个x坐标,
  //  	Integer 把拿起来的棋子放到哪个y坐标,
  //  	Boolean 是否是悔棋
  // )
  //
  // 下面是一个例子：
  // 结果是"将位于(1, 1)的棋子移动到(2, 2)"
  return new Movement(playerSide, 1, 1, 2, 2, false);
}
```

## 棋盘的刻画

用一个枚举量表示一个棋子。

```kotlin
enum class ChessGridElement {
    EMPTY,      // 空位

    // Red
    RED_GENERAL,    // 将
    RED_MINISTER,   // 相
    RED_SERVANT,    // 士
    RED_VEHICLE,    // 车
    RED_CANNON,     // 炮
    RED_RIDER,      // 马
    RED_SOLDIER,     // 兵

    // Black
    BLACK_GENERAL,    // 将
    BLACK_MINISTER,   // 相
    BLACK_SERVANT,    // 士
    BLACK_VEHICLE,    // 车
    BLACK_CANNON,     // 炮
    BLACK_RIDER,      // 马
    BLACK_SOLDIER     // 兵
}
```

用一个`ChessGridElement`类型的二元数组表示棋盘状态，这个二位数组封装在`ChessGrid`类中，可以通过调用`getGrid()`来获得。

```kotlin
class ChessGrid {
    val grid: Array<Array<ChessElement>> = // 这里填写着棋盘的初始摆放
    //...其余部分
}
```

## 关于坐标约定

棋盘是竖着摆，楚河汉界是水平。原点的位置是：当棋盘的红方在下面的时候，棋盘的左上角。向下走是x轴正方向，向右走是y轴正方向。
访问点阵中的成员：`grid[x轴坐标][y轴坐标]`


