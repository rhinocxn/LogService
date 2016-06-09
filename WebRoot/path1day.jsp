<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="LogService.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>path1day</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <% 
  LogServiceImpl service = new LogServiceImpl();
  String[] pathparm = new String[2];
  int[] count = new int[2];
  service.getPath1day(pathparm, count);
  %>
  <body>
    <table>
    	<tr>
    		<td>最多访问路径</td>
    		<td><%=pathparm[0] %></td>
    		<td>访问量</td>
    		<td><%=count[0]%></td>
    	</tr>
    	<tr>
    		<td>最多访问请求参数</td>
    		<td><%=pathparm[1]%></td>
    		<td>访问量</td>
    		<td><%=count[1]%></td>
    	</tr>
    </table>
  </body>
</html>
