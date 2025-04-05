<!DOCTYPE html>
<html>
<head>
    <title>Авторизация</title>
</head>
<body>
<h2>Авторизация</h2>
<form action="/login" method="post">
    <label for="username">Имя пользователя:</label>
    <input type="text" id="username" name="username" required><br>

    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" required><br>

    <button type="submit">Войти</button>
</form>
<p>Нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
</body>
</html>