<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
</head>
<body>
<div>
    <h2>Welcome, <span th:text="${username}">Guest</span>!</h2>
</div>
<form action="time" method="post">
    <label for="username">Enter your name: </label>
    <input type="text" id="username" name="username" required>
    <button type="submit">Submit</button>
</form>
</body>
</html>
