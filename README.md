# ClearScreenLayout [![](https://jitpack.io/v/wang642357/ClearScreenLayout.svg)](https://jitpack.io/#wang642357/ClearScreenLayout)

一个支持左右拖动清屏的Android控件ClearScreenLayout，仿抖音直播的清屏效果，通过`ViewDragHelper`处理遮罩层滑动事件，扩展`EdgeSize`属性实现空白区域拖动功能。<br />
支持ViewPager2，目前只支持从往右滑出
## 效果图

<img src="https://github.com/wang642357/ClearScreenLayout/blob/main/image/Video_20210507_052300_845.gif" height="650"/>

## 导入
### 在你的项目Project下的build.gradle中添加：

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
### 添加依赖：
```
dependencies {
    implementation 'com.github.wang642357:ClearScreenLayout:v1.1.0'
}
```
## 使用方法
### xml布局文件
在需要拖动的布局中添加`layout_dragEnable`属性
```xml
<com.wjx.android.clearscreen.ClearScreenLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:id="@+id/clear_screen"
    android:layout_height="match_parent">

    <RelativeLayout
        android:id="@+id/container1"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#55000000">

    </RelativeLayout>


    <RelativeLayout
        android:id="@+id/container2"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_dragEnable="true">

    </RelativeLayout>

</com.wjx.android.clearscreen.ClearScreenLayout>
```
### Java方法
```java
  //打开
  clearScreenLayout.open();
  //关闭
  clearScreenLayout.close();
  //添加监听事件
  clearScreenLayout.addDragListener(new ClearScreenLayout.DragListener() {
            @Override
            public void onDragging(@NonNull View dragView, float slideOffset) {
               //正在拖动中
            }

            @Override
            public void onDragToOut(@NonNull View dragView) {
               //当遮罩层被拖出去时
            }

            @Override
            public void onDragToIn(@NonNull View dragView) {
                //当遮罩层被拖入时
            }

            @Override
            public void onDragStateChanged(int newState) {
               //当拖动状态改变时
            }
        });
```


