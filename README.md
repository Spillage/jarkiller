# jarkiller
jenkins插件, 用于构建时检查依赖的jar包

## 安装
用maven打包，将target中的hpi文件导入jenkins即可完成安装.
ps: 项目中的某些缺失的类需要通过编译生成.

## 配置
配置有两项
1. jenkins的安装目录
2. 需要禁用的jar包

## 效果
在构建的时候会去扫描项目的maven依赖, 如果出现黑名单中的依赖则放弃构建.
![start](https://github.com/ColorfulWolf/resource/blob/master/images/2D58AB0D-FB69-4a19-9C59-9D7FC1BACA5B.png)
![end](https://github.com/ColorfulWolf/resource/blob/master/images/317686F5-F1BF-423c-B169-513EBF810EB1.png)
