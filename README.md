# javapath
java调用路径问题，jar包中与本地文件调用方法不同

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
