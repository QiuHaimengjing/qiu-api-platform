# 邱海梦旌的API平台

## 项目介绍
该项目是一个 API 平台，一共包含后端服务、网关服务、接口服务三个模块，采用 Spring Cloud + Nacos + Gateway + Dubbo 的微服务技术架构，项目主要功能有：调用开放 API、接口管理、用户个人中心、接口权限校验、统计分析。

## 项目结构
```
qiu-api-platform
├── qiu-api-frontend -- 前端
├── qiu-api-backend -- 后端
    ├── api_backend -- 后端服务模块
    ├── api_client-sdk -- 客户端SDK模块
    ├── api_common -- 公共模块
    ├── api_gateway -- 网关模块
    ├── api_interface -- 接口服务模块
    ├── sql -- 数据库脚本
```
* **后端服务模块**：提供项目的核心业务功能，采用 Dubbo 提供服务给网关模块调用；
* **客户端SDK模块**：仅作为 SDK 开发工具，提供统一的接口调用方法，其中包括实现请求的封装和转发的方法，接收接口服务的响应结果并进行处理的方法，主要作用是提供了一种开发规范；
* **公共模块**：是后端服务和网关服务通信的公共模块，包含了远程服务的接口、实体类，主要作用是把后端服务和网关服务都需要使用的东西抽离为一个公共模块；
* **网关模块**：项目调用 API 的流程为` 后端服务->网关服务->接口服务 `，网关的主要作用是：转发请求到接口服务、统一的 AK、SK 鉴权、统一业务处理（比如统计分析）、访问控制、流量染色、统一日志等；
* **接口服务模块**：提供接口服务。

>其中网关需要使用后端服务的方法，所以采用 Dubbo 作为 RPC 框架，后端服务作为服务提供者，网关作为服务调用者，实现后端服务和网关服务的通信，比如用户鉴权、统计分析。

## 项目技术
### 后端
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-007396?logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9.9-C71A36?logo=apachemaven&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.13-6DB33F?logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2021.0.5-6DB33F?logo=spring&logoColor=white)
![Nacos](https://img.shields.io/badge/Nacos-2.1.0-4285F4?logo=alibabacloud&logoColor=white)
![Gateway](https://img.shields.io/badge/Gateway-3.1.5-6DB33F?logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.2-blue)
![Knife4j](https://img.shields.io/badge/Knife4j-4.3.0-009688?logo=swagger&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?logo=redis&logoColor=white)
![Dubbo](https://img.shields.io/badge/Dubbo-3.0.9-26B72B?logo=apachedubbo&logoColor=white)

项目采用`Redis Session`进行分布式单点登录，后端服务和接口服务自带`Knife4j`接口文档，提供了阿里云 OSS 上传用户头像功能。

### 前端
![Node.js](https://img.shields.io/badge/Node.js-%3E%3D18-339933?logo=nodedotjs&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-5.4.0-3178C6?logo=typescript&logoColor=white)
![Vue.js](https://img.shields.io/badge/Vue.js-3.4.21-4FC08D?logo=vue.js&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-5.1.5-646CFF?logo=vite&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-1.6.7-5A29E4?logo=axios&logoColor=white)  
![ECharts](https://img.shields.io/badge/ECharts-5.5.0-AA344D?logo=apacheecharts&logoColor=white)
![Pinia](https://img.shields.io/badge/Pinia-2.1.7-FFD85E?logo=vue.js&logoColor=white)
![ESLint](https://img.shields.io/badge/ESLint-8.49.0-4B32C3?logo=eslint&logoColor=white)
![Prettier](https://img.shields.io/badge/Prettier-3.0.3-F7B93E?logo=prettier&logoColor=white)

项目组件库采用：![Arco Design](https://img.shields.io/badge/Arco%20Design-2.54.6-2080FF?logo=arco&logoColor=white)

使用`umijs/openapi`配合后端接口文档能够一键生成接口请求代码。

## 项目启动
### 后端
1. 使用 idea 打开`qiu-api-backend`;
2. 创建数据库，导入数据库脚本`/sql/dump.sql`，并修改`application.yml`中的数据库配置；
3. 修改`application.yml`中的 Redis 配置；
4. 在`api_client-sdk`模块执行`Maven install`把`api_client-sdk`打包构建在本地仓库；
5. 使用 Maven 安装依赖；
6. 启动 Nacos `startup.cmd -m standalone`，新建命名空间`dubbo`，将命名空间 id 复制到`application.yml`配置文件中的`dubbo`配置；
7. 先启动后端服务，然后再启动网关服务，接口服务不受影响。

### 前端
1. 进入`qiu-api-frontend`目录，执行`pnpm install`安装依赖，执行`pnpm run dev`启动项目。或者使用`npm`，执行`npm install`安装依赖，执行`npm run dev`启动项目，但墙裂建议使用`pnpm`，项目默认使用`pnpm`且速度更快；
2. 数据库默认导入的用户登录密码都是 123456；
3. 开发建议：如果要一键生成接口请求代码，执行`pnpm run openapi`，生成的代码在`src/services`目录下。

## 部署
后端修改`application-prod.yml`中的数据库和 dubbo 配置，前端修改根目录`.env.production`文件中的接口前缀地址。
### 传统部署
后端使用`Maven`将后端服务、网关服务、接口服务都打包，前端使用`Vite`打包，打包后的文件上传服务器启动即可。

启动后端项目例子：
```shell
java -jar example-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

前端部署，以 Nginx 为例，依据实际情况更改：
```nginx
server {
    listen 9002;
    server_name localhost;
    try_files $uri $uri/ /index.html; # history路由支持刷新后不报错
    index index.php index.html index.htm default.php default.htm default.html;

    location ^~ /api/ {
        rewrite ^/api/(.*)$ /$1 break;
        proxy_pass http://服务器地址:8081;
    }
}
```

### Docker 部署
非常推荐使用 Docker 部署，但该项目未提供 Docker 部署文件，有心的可以自己尝试。

## 项目预览
<div style="display: flex; overflow-x: auto; gap: 10px;">
  <img src="https://github.com/QiuHaimengjing/ImageStorage/blob/main/repositories/qiu-api-platform/api01.png" alt="预览图1" width="300">
  <img src="https://github.com/QiuHaimengjing/ImageStorage/blob/main/repositories/qiu-api-platform/api02.png" alt="预览图2" width="300">
  <img src="https://github.com/QiuHaimengjing/ImageStorage/blob/main/repositories/qiu-api-platform/api03.png" alt="预览图3" width="300">
  <img src="https://github.com/QiuHaimengjing/ImageStorage/blob/main/repositories/qiu-api-platform/api04.png" alt="预览图4" width="300">
  <img src="https://github.com/QiuHaimengjing/ImageStorage/blob/main/repositories/qiu-api-platform/api05.png" alt="预览图5" width="300">
</div>

## 🌟 支持项目
这个项目是为了学习交流而生，功能还待完善，期待你的奇思妙想和二次开发！  
如果它对你有所帮助，请别吝啬，给我点个小星星 ⭐ 支持吧！  
你的支持是我持续更新的动力！非常感谢，喵呜～ 🐾
