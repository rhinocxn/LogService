<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page import="LogService.*" %>
<%@ page import="com.aliyun.openservices.log.*" %>
<%@ page import="org.jfree.chart.*,org.jfree.chart.plot.*,org.jfree.chart.labels.*,  
org.jfree.data.category.*,java.awt.*,org.jfree.ui.*,org.jfree.chart.renderer.category.BarRenderer3D,  
org.jfree.chart.servlet.*,org.jfree.chart.plot.PlotOrientation,org.jfree.data.general.*"%> 
<%@ page import="java.awt.Font" %>
<%@ page import="org.jfree.chart.StandardChartTheme" %>
<%@ page import="java.text.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>test</title>
    
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
  	    
  	
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
	DefaultPieDataset datapie = new DefaultPieDataset(); 
	LogServiceImpl service = new LogServiceImpl();
	SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
	int from = (int) (new Date().getTime() / 1000 - 86400);
	int to = (int) (new Date().getTime() / 1000);
	Map<String,Integer> map = new HashMap<String,Integer>();
	
	map = service.getMethod(from, to);
	
	for(String str: map.keySet()){
		dataset.addValue(map.get(str),"",str);
		datapie.setValue(str, map.get(str));
	} 
	
	//创建主题样式  
	StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
	//设置标题字体  
	standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));  
	//设置图例的字体  
	standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));  
	//设置轴向的字体  
	standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));  
	//应用主题样式  
	ChartFactory.setChartTheme(standardChartTheme);
	
	JFreeChart chart = ChartFactory.createBarChart3D("天方法统计",   
	                  "时间",  
	                  "PV",  
	                  dataset,  
	                  PlotOrientation.VERTICAL,  
	                  false,  
	                  false,  
	                  false);  
	 
    JFreeChart chartpie = ChartFactory.createPieChart("天方法分布",  
        datapie,  
        true,  
        false,  
        false  
        ); 
	                  
	CategoryPlot plot = chart.getCategoryPlot();  
	BarRenderer3D renderer = new BarRenderer3D();    
	renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());  
	renderer.setBaseItemLabelsVisible(true);                 
	renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));  
	renderer.setItemLabelAnchorOffset(10D);  
	plot.setRenderer(renderer);                  
	                  
	String filename = ServletUtilities.saveChartAsPNG(chart, 620, 400, null, session);  
	String graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;  
	String filenamepie = ServletUtilities.saveChartAsPNG(chartpie, 500, 400, null, session);  
	String graphURLpie = request.getContextPath() + "/servlet/DisplayChart?filename=" + filenamepie;  
%>  
<img src="<%= graphURL %>" width=620 height=400 border=0> 
<img src="<%= graphURLpie %>" width=500 height=400 border=0> 
</html>
