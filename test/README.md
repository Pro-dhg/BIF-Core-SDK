# 智能合约开发
 - 大坑
 - 开发流程
 - 目录简介

[星火.链网文档](https://bif-doc.readthedocs.io/zh_CN/latest/brief.html)
### 大坑
```doce
    -- let 在同一函数中使用的时候，不可以起一样的名字，不同作用域也不可
    -- 注意 不能使用++ ——
    -- 查询函数用contractQuery （查询类的就用这个，还可以打印出来）
    -- 调用函数用contractCall  （插入类的就用这个，不可以打印，会出现找不到方法的问题）
    -- 在写完一个函数之后，要把接口暴露在main方法中。
    -- 调用的时候，一般会有个method 和params，所以在使用的时候，最好json串的格式为{'method':'init','params':'......'}
```

### 开发流程
```doce
  1.写合约
  2.部署合约，获取合约hash值
  3.根据合约hash值获取合约地址
  4.测试合约
```
### 目录简介
```doce
  [data]    javascript智能合约
  [deploy]  智能合约部署方法
  [test]    智能合约测试
  
  

```