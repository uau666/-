# 图书管理系统

这是一个使用Java Swing和MariaDB开发的图书管理系统。

## 系统要求

- Java JDK 8或更高版本
- MariaDB 10.0或更高版本
- Maven（用于依赖管理）

## 数据库设置

1. 确保MariaDB已安装并运行
2. 使用以下命令登录MariaDB：
   ```bash
   mysql -u root -p
   ```
3. 输入密码：123456
4. 运行SQL脚本创建数据库和表：
   ```bash
   source library.sql
   ```

## 项目依赖

在`pom.xml`中添加以下依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.mariadb.jdbc</groupId>
        <artifactId>mariadb-java-client</artifactId>
        <version>3.1.4</version>
    </dependency>
</dependencies>
```

## 运行应用

1. 编译项目：
   ```bash
   mvn compile
   ```

2. 运行应用：
   ```bash
   mvn exec:java -Dexec.mainClass="LibraryManagementSystem"
   ```

## 功能特性

- 添加新图书
- 更新图书信息
- 删除图书
- 查看所有图书
- 搜索图书

## 使用说明

1. 添加图书：填写图书信息，点击"添加"按钮
2. 更新图书：选择要更新的图书，修改信息后点击"更新"按钮
3. 删除图书：选择要删除的图书，点击"删除"按钮
4. 清空表单：点击"清空"按钮重置所有输入字段 