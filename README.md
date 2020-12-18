# discuz-ucenter-api-for-java

最完美的Discuz UCenter的JAVA API接口，简单易用，完全免费！

### 说明

此项目移植自[https://code.google.com/archive/p/discuz-ucenter-api-for-java](https://code.google.com/archive/p/discuz-ucenter-api-for-java)，感谢原作者```梁平 (no_ten@163.com)```将他的代码开源。

由于原有项目已经非常古老了，看时间应该是2009年的产物，但现在的项目都使用```Maven```和```SpringBoot```进行构建，里面直接配置```servlet```的方式不太适合现代项目集成，我就拿来进行了一些修改，让使用```Maven```和```SpringBoot```的程序更容易集成进去。

## 安装

我使用的是```Maven```和```SpringBoot```进行演示。在```pom.xml```文件中添加依赖：
```xml
<dependency>
  <groupId>net.renfei</groupId>
  <artifactId>discuz-ucenter-api-for-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

如果你使用的是传统方式，需要下载```Jar```包，放入```lib```文件夹，下载地址：[discuz-ucenter-api-for-java-1.0.0.jar
](https://github.com/renfei/discuz-ucenter-api-for-java/releases/download/1.0.0/discuz-ucenter-api-for-java-1.0.0.jar)

