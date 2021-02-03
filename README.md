# discuz-ucenter-api-for-java

<a href="https://search.maven.org/search?q=g:%22net.renfei%22%20AND%20a:%22discuz-ucenter-api-for-java%22" target="_blank"><img src="https://img.shields.io/maven-central/v/net.renfei/discuz-ucenter-api-for-java.svg?label=Maven%20Central" alt="Latest Stable Version"/></a>[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Frenfei%2Fdiscuz-ucenter-api-for-java.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Frenfei%2Fdiscuz-ucenter-api-for-java?ref=badge_shield)

最完美的Discuz UCenter的JAVA API接口，简单易用，完全免费！

相关博文：[https://www.renfei.net/posts/1003429](https://www.renfei.net/posts/1003429)

社区论坛：[https://bbs.renfei.net/forum-44-1.html](https://bbs.renfei.net/forum-44-1.html)

## 说明

此项目移植自[https://code.google.com/archive/p/discuz-ucenter-api-for-java](https://code.google.com/archive/p/discuz-ucenter-api-for-java) / [https://github.com/liangping/dzclient4j](https://github.com/liangping/dzclient4j)，感谢原作者[梁平 (no_ten@163.com)](https://github.com/liangping)将他的代码开源。

由于原有项目已经非常古老了，看时间应该是2009年的产物，但现在的项目都使用```Maven```和```SpringBoot```进行构建，里面直接配置```servlet```的方式不太适合现代项目集成，我就拿来进行了一些修改，让使用```Maven```和```SpringBoot```的程序更容易集成进去。

#### 关于 GPL-2.0 License

GPLv2具有很强的传染性，选用这个开源协议并不是我定的，而是原项目作者在开源时就选用了 GPLv2，我也只能跟随原开源协议继续传染下去。

当然，如果您并不注重什么版权协议，那可以无视。

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Frenfei%2Fdiscuz-ucenter-api-for-java.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Frenfei%2Fdiscuz-ucenter-api-for-java?ref=badge_large)

#### 关于程序包名修改

也许您注意到源程序的程序包名由```com.fivestars```被我改为了```net.renfei.discuz.ucenter```，这是因为要发布到Maven中央仓库，在注册Maven中央仓库的时候会要求验证域名所有权，并且数字签名，所以我只能发布```net.renfei```的库，为了在项目中不产生混淆，我就直接将源码中的包名也一起统一修改了。

#### 必要条件

您的项目环境需要是```JDK1.8```以上。其中```Base64```使用了```Java8```自带的库实现，移除了原作者自己写的```Base64```算法。同时我的编译环境也是在```Java8```中编译发布的。

## 安装

我使用的是```Maven```和```SpringBoot```进行演示。在```pom.xml```文件中添加依赖：
```xml
<dependency>
  <groupId>net.renfei</groupId>
  <artifactId>discuz-ucenter-api-for-java</artifactId>
  <version>1.0.3</version>
</dependency>
```

如果你使用的是传统方式，需要下载```Jar```包，放入```lib```文件夹，下载地址：[discuz-ucenter-api-for-java-1.0.3.jar
](https://github.com/renfei/discuz-ucenter-api-for-java/releases/download/1.0.3/discuz-ucenter-api-for-java-1.0.3.jar)

## 使用

为了跟```SpringBoot```更好的集成，我对原项目进行了修改，原项目是修改配置文件，我修改为了实例化时传递参数的方式

### 实例化

实例化一个客户端```net.renfei.discuz.ucenter.client.Client```，参数依次是：UCenter接口地址、IP地址、通讯Key、APPID、Connect。

```java
Client client = new Client("http://localhost/uc_server", null, "key", "2","");
```

### 客户端

旧的源代码中是通过配置文件注册一个```servlet```，我改造的是用于```SpringBoot```项目的，所以通过配置文件注册```servlet```不是很方便，而且为了尽量降低代码入侵性，我就改为自己使用```Controller```处理```HttpServletRequest```和```HttpServletResponse```的方式。

先创建一个```Controller```，然后创建一个处理```HttpServletRequest```和```HttpServletResponse```的方法，给一个```UCenter```请求的地址```@RequestMapping("/api/uc.php")```，实例化一个客户端```net.renfei.discuz.ucenter.api.UCClient```和```net.renfei.discuz.ucenter.client.Client```，然后把```HttpServletRequest```交给```net.renfei.discuz.ucenter.api.UCClient.doAnswer()```去处理，最后将结果写入```HttpServletResponse```，如果```UCenter```配置正确，就应该可以在```UCenter```看到通讯正常了。具体使用如下案例：

```Java
@Controller
public class UCenterController {

    @ResponseBody
    @RequestMapping("/api/uc.php")
    public void uc(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UCClient ucClient = new UCClient();
        Client client = new Client("http://localhost/uc_server", null, "key", "2","");
        String result = ucClient.doAnswer(client, request, response);
        response.getWriter().print(result);
    }
}
```

如果您想自己处理一些动作的逻辑，只需要继承```net.renfei.discuz.ucenter.api.UCClient```然后重写覆盖里面的```doAnswer(Client client, HttpServletRequest request, HttpServletResponse response)```方法即可。

#### 注册
```java
Client client = new Client("http://localhost/uc_server", null, "key", "2","");
String string = client.ucUserRegister("username","password","email");
```

#### 登陆
```java
Client client = new Client("http://localhost/uc_server", null, "key", "2","");
String string = client.ucUserLogin("username","password");
```

#### 同步登陆
```java
Client client = new Client("http://localhost/uc_server", null, "key", "2","");
int UID = 21; //此处是用户的UID
String string = client.ucUserSynlogin(uid);
```

#### 先登陆再同步登陆
```java
Client client = new Client("http://localhost/uc_server", null, "key", "2","");
// 登陆
String result = client.ucUserLogin(uid);
LinkedList<String> rs = XMLHelper.ucUnserialize(result);
if(rs.size() > 0){
	int uid = Integer.parseInt(rs.get(0));
	String username = rs.get(1);
	String password = rs.get(2);
	String email = rs.get(3);
	if(uid > 0) {
		//同步登陆
		String string = client.ucUserSynlogin(uid);
		//本地登陆代码
		//TODO ... ....
	} else if(uid == -1) {
		System.out.println("用户不存在,或者被删除");
	} else if(uid == -2) {
		System.out.println("密码错");
	} else {
		System.out.println("未定义");
	}
}else{
	System.out.println("Login failed");
	System.out.println(result);
}
```

## 常见问题
在网友使用中出现问题，和我们一起讨论交流的结果将在 [https://www.renfei.net/posts/1003429](https://www.renfei.net/posts/1003429) 文章末尾常见问题中更新补充。

## 更多信息

更多信息请阅读源代码，此处不再一一演示。您可以提出[issues](https://github.com/renfei/discuz-ucenter-api-for-java/issues)或者到我的社区论坛一起讨论：[https://bbs.renfei.net/forum-44-1.html](https://bbs.renfei.net/forum-44-1.html)