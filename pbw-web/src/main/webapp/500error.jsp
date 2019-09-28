!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
-->
<%@page isErrorPage="true" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Error - /promo.jsf - No saved view state could be found for the view identifier: /promo.jsf</title>
<style type="text/css">
body, div, span, td, th, caption { font-family: 'Trebuchet MS', Verdana, Arial, Sans-Serif; font-size: small; }
ul, li, pre { padding: 0; margin: 0; }
h1 { color: #900; }
h2, h2 span { font-size: large; color: #339; }
h2 a { text-decoration: none; color: #339; }
.grayBox { padding: 8px; margin: 10px 0; border: 1px solid #CCC; background-color: #f9f9f9;  }
#error { color: #900; font-weight: bold; font-size: medium; }
#trace, #tree, #vars { display: none; }
code { font-size: medium; }
#tree dl { color: #006; }
#tree dd { margin-top: 2px; margin-bottom: 2px; }
#tree dt { border: 1px solid #DDD; padding: 4px; border-left: 2px solid #666; font-family: "Courier New", Courier, mono; font-size: small; }
.uicText { color: #999;  }
.highlightComponent { color: #FFFFFF; background-color: #FF0000;  }
table { border: 1px solid #CCC; border-collapse: collapse; border-spacing: 0px; width: 100%; text-align: left; }
td { border: 1px solid #CCC; }
thead tr th { padding: 2px; color: #030; background-color: #F9F9F9; }
tbody tr td { padding: 10px 6px; }
table caption { text-align: left; padding: 10px 0; font-size: large; }
</style>
<style type="text/css" media="print">
#trace, #tree, #vars { display: block; }
</style>
<script language="javascript" type="text/javascript">
function toggle(id) {
	var style = document.getElementById(id).style;
	if ("block" == style.display) {
		style.display = "none";
		document.getElementById(id+"Off").style.display = "inline";
		document.getElementById(id+"On").style.display = "none";
	} else {
		style.display = "block";
		document.getElementById(id+"Off").style.display = "none";
		document.getElementById(id+"On").style.display = "inline";
	}
}
</script>
</head>
<body>
<h1>An Error Occurred: ${exception.message}</h1>
<div class="grayBox" style="text-align: right; color: #666;">
<%
String serverIpAddress = null;
try {
   java.net.InetAddress localhost = java.net.InetAddress.getLocalHost();
   serverIpAddress = localhost.getHostAddress().trim();
}
catch (java.net.UnknownHostException e) {
  System.err.println("Fatal error: cannot get  IP Address from InfoBean : " + e.getMessage());
}
%>
Server IP address is: <%= serverIpAddress %>
</div>
</body>
</html>
