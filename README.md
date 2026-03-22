# Online Course Selection System 在线学生选课系统

A web-based course selection system built with Spring Boot.  
一个基于 Spring Boot 实现的高校在线选课管理系统实践项目。

---

## 📌 Project Overview 项目简介

本项目实现了一个面向高校教学场景的在线选课系统，支持学生选课与退课、课程信息管理、角色权限控制以及基本的云端部署验证。  
系统采用典型的 B/S 架构设计，通过后端业务逻辑与前端页面交互，实现选课流程的完整功能闭环。

---

## 🚀 Main Features 核心功能

- User authentication & role-based authorization（基于 Spring Security 的登录认证与权限控制）
- Course browsing and filtering（课程浏览与条件筛选）
- Course selection and withdrawal（选课 / 退课功能）
- Course conflict checking（选课冲突检测）
- Personal timetable display（学生课表展示）
- Admin-side course management（管理员课程信息维护）
- Excel data export（选课数据导出）
- Basic teacher-side interface（教师角色页面扩展）
- Linux cloud deployment verification（Linux 云服务器部署实践）

---

## 🧩 Tech Stack 技术栈

- Java  
- Spring Boot  
- Spring MVC  
- Spring Security  
- MyBatis  
- MySQL / MariaDB  
- Thymeleaf + Semantic UI + LayUI  
- Maven  
- Redis  
- Linux

---

## 🏗️ Project Structure 项目结构

```text
src/main/java        后端业务逻辑代码
src/main/resources   配置文件、模板页面、静态资源、Mapper文件
pom.xml              Maven依赖管理
