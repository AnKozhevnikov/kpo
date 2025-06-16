# Домашнее задание 3

## Как включать

Необходимо убедиться, что можно пользоватся командой python, иногда нужно поменять на python3 в самом скрипте

```bash
./start.sh
```

## Как выключать

```bash
./stop.sh
```

## Как взаимодействовать

### Фронт

http://localhost:8000

### Swagger

http://localhost:8080/webjars/swagger-ui/index.html

### Pgadmin

http://localhost:5050/login?next=/browser/

### Kafka UI

http://localhost:8085/

## Тесты

Для orders и payments микросервисов написаны unit тесты. Можно их прогнать и получить покрытие, запустив

```bash
mvn clean test
```

в соответствующих директориях