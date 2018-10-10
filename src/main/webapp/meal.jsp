<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://topjava.javawebinar.ru/functions" prefix="f" %>

<html>
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="meals">back</a></h3>

<form action="meals" method="post">
    Meal ID :   <input type="hidden" readonly="readonly" name="id"
                     value="<c:out value="${meal.id}" />"/> <br/>
    Date/Timee: <input type="datetime-local" name="datetime"
                       value="<c:out value="${f:formatLocalDateTime(meal.dateTime, 'yyyy-MM-dd')}T${f:formatLocalDateTime(meal.dateTime, 'HH:mm')}" />"/> <br/>
    Description:<input type="text" name="description"
                        value="<c:out value="${meal.description}" />"/> <br/>
    Calories:   <input type="text" name="calories"
                     value="<c:out value="${meal.calories}" />"/> <br/>
    <input type="submit" value="Submit"/>
</form>

</body>
</html>