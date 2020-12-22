# discuz-ucenter-api-for-java
<a href="https://search.maven.org/search?q=g:%22net.renfei%22%20AND%20a:%22discuz-ucenter-api-for-java%22" target="_blank"><img src="https://img.shields.io/maven-central/v/net.renfei/discuz-ucenter-api-for-java.svg?label=Maven%20Central" alt="Latest Stable Version"/></a>

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
  <version>1.0.1</version>
</dependency>
```

如果你使用的是传统方式，需要下载```Jar```包，放入```lib```文件夹，下载地址：[discuz-ucenter-api-for-java-1.0.1.jar
](https://github.com/renfei/discuz-ucenter-api-for-java/releases/download/1.0.1/discuz-ucenter-api-for-java-1.0.1.jar)

## 使用

为了跟```SpringBoot```更好的集成，我对原项目进行了修改，原项目是修改配置文件，我修改为了实例化时传递参数的方式

### 实例化

实例化一个客户端```net.renfei.discuz.ucenter.client.Client```，参数依次是：UCenter接口地址、IP地址、通讯Key、APPID、Connect。

```java
Client client = new Client("http://localhost/uc_server", null, "key", "2","");
```

#### 注册
```java
Client client = new Client("http://localhost/uc_server", null, "key", "2","");
String string = client.uc_user_register("username","password","email");
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

## 更多信息

更多信息请阅读源代码，此处不再一一演示。