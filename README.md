# MonitoringBackend (pet project)

## Описание
Сервис для мониторинга игровых серверов [minecraft](https://www.minecraft.net/ru-ru) (далее просто - игровой сервер). Он позволяет владельцам игровых серверов опубликовывать их творения, а игрокам просматривать самые новые игровые сервера. При этом сервис регулярно проводит проверку состояний всех новых опубликованных игровых серверов в списке, чтобы отфильтровывать недоступные (выключеннные) игровые сервера.

Целевая аудитория:
* Игроки minecraft
* Владельцы игровых серверов minecraft

Демонстрация: https://new-servers.ru

## Архитектура
Полное приложение имеет микросервисную архитектуру и состоит из 2 REST сервисов:
* Backend - сервис для хранения списка серверов, регистрации пользователей (текущий репозиторий)
* Pinger - сервис для проверки статусов серверов (https://github.com/GreenpixDev/MonitoringPinger)

![Dependency Visualizer-Страница 5 drawio (1)](https://user-images.githubusercontent.com/58062046/227698060-295aa234-1ede-459f-bfea-478a72627a7c.png)

## Стек и технологии
При разработке использовались данные технологии:
* [Kotlin](https://kotlinlang.org/)
* [Gradle (Kotlim DSL)](https://docs.gradle.org/current/userguide/kotlin_dsl.html)
* [Spring boot 3](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Release-Notes)
* [Spring webflux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
* [Spring security](https://docs.spring.io/spring-security/reference/index.html)
* [Spring mail](https://docs.spring.io/spring-framework/docs/3.0.x/spring-framework-reference/html/mail.html)
* [SpringDoc OpenAPI](https://springdoc.org/)
* [JWT (jjwt)](https://github.com/jwtk/jjwt)
* [PostgreSQL](https://www.postgresql.org/)
* [R2dbc](https://r2dbc.io/)
* [Flyway](https://flywaydb.org/)
* [Docker](https://www.docker.com/)

## Функционал
* Возможность пользователю просматривать список доступных игровых серверов, их версию
* Возможность пользователю регистрироваться в системе
* Возможность зарегистрированному пользователю добавлять в список новый игровой сервер по его IP адресу
* Регулярная проверка состояний (статус включен/выключен, количество игроков на сервере) игровых серверов с помощью другого микросервиса
* Поддержка ReCaptcha

## Артефакты
UML диаграмма классов

![Dependency Visualizer-Страница 3 drawio](https://user-images.githubusercontent.com/58062046/227695916-39e0210e-77fb-46a3-bcc7-484c2c64f14b.svg)

UML use cases

![Dependency Visualizer-Страница 4 drawio (2)](https://user-images.githubusercontent.com/58062046/227697080-96359a0e-ecb6-46b4-91d6-aa8a50fc7ced.svg)

## Ссылки
* Ссылка на демонстрацию: https://new-servers.ru
* Ссылка на репозиторий "Сервиса проверки статусов серверов": https://github.com/GreenpixDev/MonitoringPinger
