# Visual Swing for Eclipse #

> ## 简介: ##

> Visual Swing是用Eclipse开发的 一款免费的、开源的、功能上与Mattise Project相近的界面设计工具插件. 他可以简化您开发应用程序的步骤,缩短开Swing于VE(Eclipse的另一款插件)，每个组件用一个getXXX返回，代码可读性很强，而且开发者可以自由修改代码,保存后保持和界面同步更新.Designer 发周期.它具有很多Mattise 所不具有的功能. 生成的代码也没有NetBeans 生成的代码那样复杂, 这个工具不需要form文件，生成的代码也是类似


开发工具 : Eclipse3.4

JDK版本 : JDK 1.6 (NimbusLookAndFeel 需要JDK1.6 u10)或更高

开发平台 : Windows XP SP2
开发动机:(为什么是基于Eclipse的Visual Swing?)

> 一直以来非常喜欢Swing，对Swing的研究也有很长一段时间了，一直希望Swing在Java桌面开发中扮演重要的角色。但是很久以来Java桌面开发始终是个弱项，其中很大一部分原因是缺乏比较好用的界面设计工具。


> NetBeans的界面设计工具(以前叫Matisse Project)的出现给Java的Gui开发尤其是Swing开发带来了希望，但遗憾的是当前Java集成开发环境仍然是Eclipse的天下，据统计，目前市场上至少2/3的份额是被eclipse占有。虽然netbeans已经获得长足的进步，并进一步吸引了更多的developer，但是由于历史习惯和遗留系统等等问题，这个市场份额在最近几年肯定还是eclipse的天下。


> 当然eclipse也有很多很优秀的界面工具插件，可惜的是免费的插件其质量难以与netbeans的匹敌，好用的插件又都是收费的。因此一直想为eclipse开发一款免费的、开源的、功能上与Mattise Project相近的界面设计工具，算是为Swing的发展做一点贡献。


> 从今年1月份开始，我来到一家新公司，主要做基于eclipse插件平台的开发，得以学习了eclipse的插件开发知识。以前曾经使用 swing做过一个swing的界面设计工具，但由于是一款独立的工具，没有集成到任何IDE中，因此几乎没有实用价值。因此决定在闲暇时间将这个工具重新用eclipse插件技术进行开发。


**开发这款工具的目标计划**


  1. 界面布局模仿Netbeans界面设计工具，操作和netbeans设计工具基本一模一样，容易直观的实现布局。这是第一目标。目前已经实现。


> 2. 代码的生成和解析不需要辅助form文件，直接从源代码文件进行解析生成。生成代码可读性要强，要可以编辑并且同步。目前已经基本实现，有些折中处理，采用约定代替配置的办法提高代码解析速度。


> 3. 直接支持树和表的界面设计，不需要写代码，直观的采用界面操作，便可以直接生成表和树的数据模型代码。此功能已经实现。而且目前的框架可以很容易扩展，实现类似其他复杂组件的界面设计。


> 4. 工具的性能良好，界面设计功能流畅，代码解析/生成速度快。目前来看，速度和性能还不错，初步的打算是将速度放在最后处理优化。


> 5.支持在设计时切换LookAndFeel,并能生成所设定LookAndFeel的代码。这样就能做到设计时和运行时完全一致。而NetBeans的设计工具只能以NetBeans自身相同的LookAndFeel设计，然后使用另外的LookAndFeel预览。


> 6.更多的特性 将在以后的版本中添加.

**代码位置**

http://code.google.com/p/visualswing4eclipse/

**授权方式:**

本工具使用的是EPL v1.0授权方式。照顾商业利用和开源改进而采用此授权。

目前支持的Eclipse平台是3.4，JDK请使用1.6
联系方式:

Msn : rehte @ hotmail . com


**常见问题解答:**

  1. 变量命名为什么是$nameLabel这样的，是为了读取方便么？

> Re：这儿是netbeans form解析法和VE的AST语法数解析法的一个折中，提高解析速度，降低解析复杂度，同时有抛弃了form文件的一个折中办法，被设计的界面组件的field名称需要以$作为识别，当然这个$是可以替换的。目前还没有做这一步。

> 2、方法名字为什么是changeBtn\_action\_actionPerformed这样的，不符合java的规范，有配置可以改么？

> Re：这个以后会增加配置进行配置，但目前太多其他的细化工作要做，本人精力实在有限。


> 3、我这里用的英文版的eclipse3.4，改变变量时，确认和取消按钮为乱码，没有国际化支持么？


> Re：目前只是出于功能实现阶段，国际化还没有考虑，不过eclipse的国际化是很简单的事情，打算把它作为最后阶段解决的问题。


> 4、netbeans中双击button可以直接生成action调用方法，希望也提供这个功能。


> Re：这个没有问题。只不过这儿双击是调出in-place editor来实地编辑组件值。比如双击Table和tree能够调出直接编辑表格和树的设计器。


> 5、怎么没有直接切换到代码的视图，还有到了代码后我直接 运行 怎么出来不了东西。。？


> Re：目前不好用是因为功能还没有完全完成。很正常，因此我把版本定在0.9.0，甚至应该更低，但是目前大部分功能框架和实现已经完成，缺少只是细化，所以我希望对于Swing和SWT以及Eclipse RCP开发功底比较深的人能够加入进来，帮助完善。


## 开发步骤: ##

**1、新建一个Java工程**

![http://gml520.javaeye.com/upload/picture/pic/21295/a4bc471a-ffd1-3758-9e68-5b8bd7fa7cda.jpg](http://gml520.javaeye.com/upload/picture/pic/21295/a4bc471a-ffd1-3758-9e68-5b8bd7fa7cda.jpg)

**2、安装插件以后，在New Class Wizard中可以发现一个Visual Swing Class，选择该项以生成可视化JPanel，目前只支持JPanel，以后可以很容易扩展,会有更多的支持。**
![http://lh3.ggpht.com/guanminglin/SNOYuvXxhnI/AAAAAAAABis/3QCrqd03J_Y/s400/ecx.jpg](http://lh3.ggpht.com/guanminglin/SNOYuvXxhnI/AAAAAAAABis/3QCrqd03J_Y/s400/ecx.jpg)


**初始化界面**

![http://gml520.javaeye.com/upload/picture/pic/21293/340905b5-2567-3d1a-9b88-784d9a7137f3.jpg](http://gml520.javaeye.com/upload/picture/pic/21293/340905b5-2567-3d1a-9b88-784d9a7137f3.jpg)

**目前完成的特性截图:**

> 下面是一个典型的设计界面的场景，红框标出的是该插件提供的视图和按钮，支持LookAndFeel切换：

![http://rehte.javaeye.com/upload/picture/pic/16789/7a5fe012-7311-3053-b09b-2798312d597d.png](http://rehte.javaeye.com/upload/picture/pic/16789/7a5fe012-7311-3053-b09b-2798312d597d.png)

**这儿是拖拽组件时显示布局提示，和netbeans的界面布局设计类似：**

![http://rehte.javaeye.com/upload/picture/pic/16781/e1840351-46d6-3e2f-87e6-6639593dfcaa.png](http://rehte.javaeye.com/upload/picture/pic/16781/e1840351-46d6-3e2f-87e6-6639593dfcaa.png)

**这是生成的源代码，可以直接修改源代码，保存后，会自动同步的到设计界面上去：**

![http://rehte.javaeye.com/upload/picture/pic/16783/69be9aa8-cee4-3a94-ab1f-877a961d5f6d.png](http://rehte.javaeye.com/upload/picture/pic/16783/69be9aa8-cee4-3a94-ab1f-877a961d5f6d.png)

**这是一个表格的设计器，你可以直接在界面上拖拽，添加删除编辑表格：**

![http://rehte.javaeye.com/upload/picture/pic/16785/5322d0bc-2bdd-3803-aeb6-645bf26ef9fe.png](http://rehte.javaeye.com/upload/picture/pic/16785/5322d0bc-2bdd-3803-aeb6-645bf26ef9fe.png)

**这是一个树的设计器，你可以直观的添加删除编辑树的节点：**

![http://rehte.javaeye.com/upload/picture/pic/16787/06d76029-433f-34eb-8d20-91a01b75f0b3.png](http://rehte.javaeye.com/upload/picture/pic/16787/06d76029-433f-34eb-8d20-91a01b75f0b3.png)

**多LookAndFeel同时设计的抓图，含有NimbusLookAndFeel
NimbusLookAndFeel显得得体大方，字体也很好看，图标精致，组件显得既圆润而又不过分华丽。不知你注意没有，新的Java2D的字体渲染是使用本地渲染库渲染的，已经完全和本地程序的字体一模一样的。的确不错:)**

![http://rehte.javaeye.com/upload/picture/pic/17117/fe470ef2-50b3-3499-ac23-92824e1d694c.png](http://rehte.javaeye.com/upload/picture/pic/17117/fe470ef2-50b3-3499-ac23-92824e1d694c.png)


