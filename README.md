> 此仓库为中文翻译仓库，并加入了一些自己的理解。
> 原作者：Luis G. Valle
> 原Git仓库地址：https://github.com/lgvalle/Material-Animations
> 翻译作者：Woolsen
> 中文翻译Git仓库地址：https://github.com/woolsen/Material-Animations


> No maintainance is intended. 
> The content is still valid as a reference but it won't contain the latest new stuff
>
> ​				       ---- Luis G. Valle
>
> 无需维护。
> 该内容作为引用仍然有效，但不会包含最新的内容

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Material--Animations-brightgreen.svg?style=flat)](http://android-arsenal.com/details/3/1880)

[Android Transition Framework][transition-framework] 主要可用于**三个**方面:

1. 从一个Activity跳转到另一个Acitivity时设置Acitivity布局内容的动画.
2. 当Activity跳转时，为Activity之间的共享元素设置动画.
3. 设置同一Activity中View改变的动画.


## 1. Activity之间的过渡动画（Transition）

为已存在的布局**内容**设置动画

![A Start B][transition_a_to_b]

当从`Activity A`跳转到`Activity B`时，内容布局将根据定义的变换(Transition)设置过渡动画。在`android.transition.Transition`中，有三个预设的过渡动画可以使用：**Explode**, **Slide** 和 **Fade**. 

所有这些过渡动画都跟踪活动布局中目标视图的可见性更改，并为这些视图设置动画以遵循过渡规则。

[Explode][explode_link] | [Slide][slide_link] | [Fade][fade_link]
--- | --- | ---
![transition_explode] | ![transition_slide] | ![transition_fade]

可以使用**XML声明**或者通过**代码编写**。比如Fade过渡动画，可以通过以下方式声明：

### XML
过渡动画的XML文件放在 `res/transition`

> res/transition/activity_fade.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<fade xmlns:android="http://schemas.android.com/apk/res/"
    android:duration="1000"/>

```

> res/transition/activity_slide.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<slide xmlns:android="http://schemas.android.com/apk/res/"
    android:duration="1000"/>

```

然后使用 `TransitionInflater` 来加载这些Transition

> MainActivity.java

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        setupWindowAnimations();
    }

    private void setupWindowAnimations() {
        Slide slide = TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);
    }

```

> TransitionActivity.java

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        setupWindowAnimations();
    }

    private void setupWindowAnimations() {
        Fade fade = TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        getWindow().setEnterTransition(fade);
    }

```

### 通过代码编写 

> MainActivity.java

```java
	  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        setupWindowAnimations();
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

```

> TransitionActivity.java

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        setupWindowAnimations();
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

```

#### 效果:

![transition_fade]


### 一步一步发生了什么:

1. Activity A 启动 Activity B

2. Transition Framework 找到 A Exit Transition (slide) 并将其应用于所有可见的View。
3. Transition Framework 找到 B Enter Transition (fade) 并将其应用于所有可见的View。
4. 按下返回时，Transition Framework 分别执行 Enter 和 Exit 的反向动画 (如果我们已经定义了`returnTransition` 和 `reenterTransition`, 那么它们将被执行) 

### ReturnTransition 和 ReenterTransition

Return Transitions 和 Reenter Transitions 分别是Enter和Exit的反向动画.

  * EnterTransition <--> ReturnTransition
  * ExitTransition <--> ReenterTransition

如果没有定义Return或Reenter，系统将执行反向的 Enter Transition 和 Exit Transition 。但如果定义了它们，则可以使用不同的转换来进入和退出Acitivity。

![b back a][transition_b_to_a]

我们可以修改之前的 Fade 样例，为`TransitionActivity`定义一个`ReturnTransition`。在当前样例中，定义了一个 滑出（**Slide**） 过渡动画。此时，当我们从B返回A，原本是一个淡出（Fade）的过渡动画（反向的Enter Transition），现在变成了滑出（**Slide**）的过渡动画

> TransitionActivity.java

```java
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        setupWindowAnimations();
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
        
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setReturnTransition(slide);        
    }

```

注意，如果没有定义Return Transition，则执行反向的Enter Transition。
如果定义了Return Transition，则执行定义的Return Transition。

没有 Return Transition | 有 Return Transition 
--- | --- 
Enter: `Fade In` | Enter: `Fade In`
Exit: `Fade Out` | Exit: `Slide out`
![transition_fade] | ![transition_fade2] 


## 2. Activity之间的共享元素

这主要用在两个不同布局中有两个不同的试图，并以某种方式用动画将他们链接起来。

Transition framework 会自动为场景切换添加动画效果，向用户显示从一个视图到另一个视图的过渡。

记住：视图**并不是**真正从一个布局移动到另一个布局。它们是两种独立的View，通过属性设置，为另一个布局的View构造一个与原布局的View相同的效果，然后对他执行过渡动画。


![A Start B with shared][shared_element]


### a) 开启 Window Content Transition

需要在 `styles.xml` 中设置.

> values/styles.xml

```xml
<style name="MaterialAnimations" parent="@style/Theme.AppCompat.Light.NoActionBar">
    ...
    <item name="android:windowContentTransitions">true</item
    ...
</style>
```

你也可以为APP指定默认的enter、exit和共享元素的过渡动画。

```xml
<style name="MaterialAnimations" parent="@style/Theme.AppCompat.Light.NoActionBar">
    ...
    <!-- 指定 enter过渡 和 exit过渡 -->
    <item name="android:windowEnterTransition">@transition/explode</item>
    <item name="android:windowExitTransition">@transition/explode</item>

    <!-- 指定共享元素过渡 -->
    <item name="android:windowSharedElementEnterTransition">@transition/changebounds</item>
    <item name="android:windowSharedElementExitTransition">@transition/changebounds</item>
    ...
</style>
```



### b) 定义一个transition name

要实现过渡，你需要为原View和目标View设置相同的 **`android:transitionName`** 。它们可以有不同的ID和属性，但`android:transitionName`必须相同。

> layout/activity_a.xml

```xml
<ImageView
        android:id="@+id/small_blue_icon"
        style="@style/MaterialAnimations.Icon.Small"
        android:src="@drawable/circle"
        android:transitionName="@string/blue_name" />
```

> layout/activity_b.xml

```xml
<ImageView
        android:id="@+id/big_blue_icon"
        style="@style/MaterialAnimations.Icon.Big"
        android:src="@drawable/circle"
        android:transitionName="@string/blue_name" />
```

### c) 启动带有共享元素的Activity 

使用 `ActivityOptions.makeSceneTransitionAnimation()` 方法定义共享元素和transition name。

> MainActivity.java

```java

blueIconImageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(MainActivity.this, SharedElementActivity.class);

        View sharedView = blueIconImageView;
        String transitionName = getString(R.string.blue_name);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        startActivity(i, transitionActivityOptions.toBundle());
    }
});

```


这段代码将产生优美的过渡动画：

![a to b with shared element][shared_element_anim]

如图，过渡框架正在创建和执行一个动画，让人产生视图从一个Activity移动到另一个Activity并改变形状的假象。

## fragment之间的共享元素

共享元素过渡动画在framgnet中的的方式与Activity的方式非常相似。

**a)** 和 **b)** 都**完全**相同. 只有 **c)** 需要修改			

### a) 开启Window Content Transition

> values/styles.xml

```xml
<style name="MaterialAnimations" parent="@style/Theme.AppCompat.Light.NoActionBar">
    ...
    <item name="android:windowContentTransitions">true</item>
    ...
</style>
```

### b) 定义一个统一的transition name

> layout/fragment_a.xml

```xml
<ImageView
        android:id="@+id/small_blue_icon"
        style="@style/MaterialAnimations.Icon.Small"
        android:src="@drawable/circle"
        android:transitionName="@string/blue_name" />
```

> layout/fragment_b.xml

```xml
<ImageView
        android:id="@+id/big_blue_icon"
        style="@style/MaterialAnimations.Icon.Big"
        android:src="@drawable/circle"
        android:transitionName="@string/blue_name" />
```

###  c) 开启一个带有共享元素的fragment

为此，您需要将共享元素转换信息作为FragmentTransaction过程的一部分设置进来。

```java
FragmentB fragmentB = FragmentB.newInstance(sample);

// 为fragment的所有View定义enter transition
Slide slideTransition = new Slide(Gravity.RIGHT);
slideTransition.setDuration(1000);
sharedElementFragment2.setEnterTransition(slideTransition);

// 仅为共享元素定义enter transition
ChangeBounds changeBoundsTransition = TransitionInflater.from(this).inflateTransition(R.transition.change_bounds);
fragmentB.setSharedElementEnterTransition(changeBoundsTransition);

getFragmentManager().beginTransaction()
        .replace(R.id.content, fragmentB)
        .addSharedElement(blueView, getString(R.string.blue_name))
        .commit();
```

这是最后的结果:

![shared_element_no_overlap]

## 允许过渡重叠

您可以定义进入和退出过渡动画是否可以相互重叠. 

来自 [Android documentation](http://developer.android.com/intl/ko/reference/android/app/Fragment.html#getAllowEnterTransitionOverlap()):
> 当为 **true** 时, enter transition 立马开始. 
> 
> 当为 **false** 时, enter transition 会等待 exit transition 结束再开始.

这个设置对Fragment和Activitiy的共享元素过渡动画都有效.

```java
FragmentB fragmentB = FragmentB.newInstance(sample);

// 为fragment的所有View定义enter transition
Slide slideTransition = new Slide(Gravity.RIGHT);
slideTransition.setDuration(1000);
sharedElementFragment2.setEnterTransition(slideTransition);

// 仅为共享元素定义enter transition
ChangeBounds changeBoundsTransition = TransitionInflater.from(this).inflateTransition(R.transition.change_bounds);
fragmentB.setSharedElementEnterTransition(changeBoundsTransition);

// 防止重叠过渡
fragmentB.setAllowEnterTransitionOverlap(overlap);
fragmentB.setAllowReturnTransitionOverlap(overlap);

getFragmentManager().beginTransaction()
        .replace(R.id.content, fragmentB)
        .addSharedElement(blueView, getString(R.string.blue_name))
        .commit();
```

可以很容易地在下面例子中看出区别：

Overlap True | Overlap False
--- | --- 
Fragment_2 出现在 Fragment_1的顶部 | Fragment_2 等待 Fragment_1 消失 
![shared_element_overlap] | ![shared_element_no_overlap]



## 3. 设置视图布局元素的动画

### 场景（Scenes）
Transition Framework 也可以用于当前Activity布局中的元素变化的动画

场景之间会发生过渡动画。一个场景（Scene）只是**定义了UI的静态状态**的一个规则的布局。你可以从一个场景过渡到另一个场景,Transition Framework会为两个场景之间设置动画。

```java
scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.activity_animations_scene1, this);
scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.activity_animations_scene2, this);
scene3 = Scene.getSceneForLayout(sceneRoot, R.layout.activity_animations_scene3, this);
scene4 = Scene.getSceneForLayout(sceneRoot, R.layout.activity_animations_scene4, this);

(...)

@Override
public void onClick(View v) {
    switch (v.getId()) {
        case R.id.button1:
            TransitionManager.go(scene1, new ChangeBounds());
            break;
        case R.id.button2:
            TransitionManager.go(scene2, TransitionInflater.from(this).inflateTransition(R.transition.slide_and_changebounds));
            break;
        case R.id.button3:
            TransitionManager.go(scene3, TransitionInflater.from(this).inflateTransition(R.transition.slide_and_changebounds_sequential));
            break;
        case R.id.button4:
            TransitionManager.go(scene4, TransitionInflater.from(this).inflateTransition(R.transition.slide_and_changebounds_sequential_with_interpolators));
            break;  
    }
}
```

该代码将在同一Activity中的四个场景之间使用过渡动画。每个过渡都定义了不同的动画。

过渡框架将获取当前场景中的所有可见视图，并根据下一个场景计算所需的动画来排列这些视图。

![scenes_anim]


### 布局更改

Transition Framework 还可以用于设置视图中布局属性更改的动画。你只需要做任何你想要的改变，它会为你执行必要的动画。

#### a) 开始延迟过渡

通过这行代码，我们告诉框架我们将执行一些UI更改，这些更改将需要动画化。

```java
TransitionManager.beginDelayedTransition(sceneRoot);
```
#### b) 更改视图布局属性


```java
ViewGroup.LayoutParams params = greenIconView.getLayoutParams();
params.width = 200;
greenIconView.setLayoutParams(params);
```

Changing view width attribute to make it smaller will trigger a `layoutMeasure`. At that point the Transition framework will record start and ending values and will create an animation to transition from one to another.


![view layout animation][view_layout_anim]


## 4. (额外) 共享元素 + Circular Reveal

Circular Reveal是一种用来显示和隐藏一组UI元素的动画，视觉效果类似于涟漪。可以在API 21起的`ViewAnimationUtils`类中获取到。

Circular Reveal动画可以结合使用共享元素转换来创建有意义的动画，平滑地为用户展示正在发生的事情。

![reveal_shared_anim]

在这个例子中逐步发生的是：

* 橙色圆圈是一个共享元素，从`MainActivity`过渡到`RevealActivity`。
* 在`RevealActivity`上，有一个侦听器侦听共享元素转换结束。当这种情况发生时，它会做两件事：
  * 为Toolbar执行 Circular Reveal动画
  * 使用普通的`ViewPropertyAnimator`在`RevealActivity`视图上执行放大动画

> 侦听共享元素enter过渡结束

```java
Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion);
getWindow().setSharedElementEnterTransition(transition);
transition.addListener(new Transition.TransitionListener() {
    @Override
    public void onTransitionEnd(Transition transition) {
        animateRevealShow(toolbar);
        animateButtonsIn();
    }
    
    (...)

});
        
```

> Reveal Toolbar

```java
private void animateRevealShow(View viewRoot) {
    int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
    int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
    int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

    Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
    viewRoot.setVisibility(View.VISIBLE);
    anim.setDuration(1000);
    anim.setInterpolator(new AccelerateInterpolator());
    anim.start();
}
```

> 放大activity布局中的view

```java
private void animateButtonsIn() {
    for (int i = 0; i < bgViewGroup.getChildCount(); i++) {
        View child = bgViewGroup.getChildAt(i);
        child.animate()
                .setStartDelay(100 + i * DELAY)
                .setInterpolator(interpolator)
                .alpha(1)
                .scaleX(1)
                .scaleY(1);
    }
}
```

### 更多的circular reveal动画

有许多不同的方法可以创建显示动画。重要的是使用动画来帮助用户理解应用程序中正在发生的事情。

#### 从目标视图的中间显示Circular Reveal

![reveal_green]

```java
int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
int cy = viewRoot.getTop();
int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
viewRoot.setBackgroundColor(color);
anim.start();
```

#### 从目标视图的顶部显示Circular Reveal + 动画

![reveal_blue]

```java
int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
viewRoot.setBackgroundColor(color);
anim.addListener(new AnimatorListenerAdapter() {
    @Override
    public void onAnimationEnd(Animator animation) {
        animateButtonsIn();
    }
});
anim.start();
```


#### 从触摸点显示Circular Reveal

![reveal_yellow]

```java
@Override
public boolean onTouch(View view, MotionEvent motionEvent) {
    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
        if (view.getId() == R.id.square_yellow) {
            revealFromCoordinates(motionEvent.getRawX(), motionEvent.getRawY());
        }
    }
    return false;
}
```

```java 
private Animator animateRevealColorFromCoordinates(int x, int y) {
    float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());

    Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
    viewRoot.setBackgroundColor(color);
    anim.start();
}
```

#### 动画和Reveal

![reveal_red]

```java
Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion);
transition.addListener(new Transition.TransitionListener() {
    @Override
    public void onTransitionEnd(Transition transition) {
        animateRevealColor(bgViewGroup, R.color.red);
    }
    (...)
   
});
TransitionManager.beginDelayedTransition(bgViewGroup, transition);
RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
btnRed.setLayoutParams(layoutParams);
```


# 样例代码

原作者地址：**[https://github.com/lgvalle/Material-Animations](https://github.com/lgvalle/Material-Animations/)**

中文翻译地址：**[https://github.com/woolsen/Material-Animations](https://github.com/woolsen/Material-Animations)**


# 更多信息

  * Alex Lockwood posts about Transition Framework. A great in deep into this topic: [http://www.androiddesignpatterns.com/2014/12/activity-fragment-transitions-in-android-lollipop-part1.html](http://www.androiddesignpatterns.com/2014/12/activity-fragment-transitions-in-android-lollipop-part1.html)
  * Amazing repository with lot of Material Design samples by Saul Molinero: [https://github.com/saulmm/Android-Material-Examples](https://github.com/saulmm/Android-Material-Examples)
  * Chet Hasse video explaining Transition framework: [https://www.youtube.com/watch?v=S3H7nJ4QaD8](https://www.youtube.com/watch?v=S3H7nJ4QaD8)



[transition-framework]: https://developer.android.com/training/transitions/overview.html

[explode_link]: https://developer.android.com/reference/android/transition/Explode.html
[fade_link]: https://developer.android.com/reference/android/transition/Fade.html
[slide_link]: https://developer.android.com/reference/android/transition/Slide.html

[transition_explode]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/transition_explode.gif
[transition_slide]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/transition_slide.gif
[transition_fade]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/transition_fade.gif
[transition_fade2]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/transition_fade2.gif
[transition_a_to_b]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/transition_A_to_B.png
[transition_b_to_a]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/transition_B_to_A.png

[shared_element]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/shared_element.png
[shared_element_anim]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/shared_element_anim.gif
[shared_element_no_overlap]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/shared_element_no_overlap.gif
[shared_element_overlap]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/shared_element_overlap.gif

[scenes_anim]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/scenes_anim.gif
[view_layout_anim]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/view_layout_anim.gif

[reveal_blue]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/reveal_blue.gif
[reveal_red]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/reveal_red.gif
[reveal_green]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/reveal_green.gif
[reveal_yellow]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/reveal_yellow.gif
[reveal_shared_anim]: https://raw.githubusercontent.com/lgvalle/Material-Animations/master/screenshots/shared_reveal_anim.gif
