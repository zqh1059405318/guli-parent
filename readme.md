# 在线学习项目

[toc]

## 项目结构

![image-20201221162337960](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201221162337960.png)

- guli-parent（**在线教学根目录（父工程），管理四个子模块**）
  - **common**：公共模块父节点
    - common_utils：工具类模块，现阶段有统一返回结果的R类
    - service_base：service服务的base包，包含service服务的公共配置类，所有service模块依赖于它，现在有swagger-UI配置类，mp自动配置的元对象处理器接口和统一异常处理器。
  - **service**:api接口服务父节点
    - service_edu:教学相关API接口服务，目前只有教师管理相关的接口。





