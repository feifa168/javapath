# javapath
java调用路径问题，jar包中与本地文件调用方法不同
> 参考内容
* [java项目里classpath具体指哪儿个路径](https://blog.csdn.net/u011095110/article/details/76152952)
* [classpath的理解及其使用方式](https://blog.csdn.net/wk1134314305/article/details/77940147)
* [Setting the class path](https://docs.oracle.com/javase/7/docs/technotes/tools/windows/classpath.html)

## classpath路径指什么
>只知道把配置文件如：mybatis.xml、spring-web.xml、applicationContext.xml等放到src目录（就是存放代码.java文件的目录），然后使用“classpath：xxx.xml”来读取，都放到src目录准没错，那么到底classpath到底指的什么位置呢？
```text
src路径下的文件在编译后会放到WEB-INF/classes路径下吧。默认的classpath是在这里。直接放到WEB-INF下的话，是不在classpath下的。用ClassPathXmlApplicationContext当然获取不到。
如果单元测试的话，可以在启动或者运行的选项里指定classpath的路径的。

用maven构建项目时候resource目录就是默认的classpath
classPath即为java文件编译之后的class文件的编译目录一般为web-inf/classes，src下的xml在编译时也会复制到classPath下
ApplicationContext ctx = new ClassPathXmlApplicationContext("xxxx.xml");  //读取classPath下的spring.xml配置文件
ApplicationContext ctx = new FileSystemXmlApplicationContext("WebRoot/WEB-INF/xxxx.xml");   //读取WEB-INF 下的spring.xml文件
```

## getClass().getResource(name)
> 得到当前类的路径，读取文件需要相对于该class所在路径的相对位置
>> IDEA调试和jar包中查找路径不同，IDEA调试下相对路径可以使用，但jar包中只能使用根路径/开头
* name=""，得到的是当前类的路径
* name="/"，得到的是根路径
* name="./"，得到的是当前类的路径
* name="../"，得到的是当前类的路径的上一层路径
* name="file.txt"，得到的是该文件的路径
* filename
* ./filename
* /filename

## getClass().getResourceAsStream(name)
同 getClass().getResource(name)

## getClass().getClassLoader().getResource(name)
> 得到的是根路径，IDEA中可以使用"filename"和"./filename"，但jar中只能使用"filename"，不能使用/符号
* name=""，得到的是根路径
* name="/"，错误，null
* name="./"，错误，null
* name="../"，错误，null
* name="file.txt"，得到的是根路径下的文件
* filename，IDEA和jar包中访问正常
* ./filename，尽在IDEA中可以使用
* /filename，都不能访问

## getClass().getClassLoader().getResourceAsStream(name)
同 getClass().getResource(name)

## 本地文件，文件在包外面，可以使用相对路径
> 前两种可以用，第三种不可用
* filename，与jar在同级目录
* ./filename，与jar在同级目录
* ../filename，相对于jar包的上一级目录
* /filename，错误，找不到文件

## System.getProperty("user.dir")组装的本地文件，
> 三种都可以用，System.getProperty("user.dir")得到当前jar包所在路径
* filename，与jar在同级目录
* ./filename，与jar在同级目录
* ../filename，相对于jar包的上一级目录
* /filename，与jar在同级目录
