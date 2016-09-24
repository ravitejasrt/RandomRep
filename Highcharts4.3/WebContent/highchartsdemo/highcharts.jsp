<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<script src="http://code.highcharts.com/stock/highstock.js"></script>
<script src="http://highcharts.github.io/export-csv/export-csv.js"></script>
<body>
   <div id="container" style="height: 300px; margin-top: 2em"></div>
   <button id="getcsv">Alert CSV</button>
</body>
<script>
var chart = new Highcharts.StockChart({

    chart: {
        renderTo: 'container'
    },

    navigator: {
        series: {
            includeInCSVExport: false
        }
    },
    series: [{
        data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4],
        pointStart: Date.UTC(2013, 0, 1),
        pointInterval: 24 * 36e5
    }],
    
    exporting: {
        csv: {
            dateFormat: '%Y-%m-%d'
        }
    }

});

$('#getcsv').click(function () {
    alert(chart.getCSV());
});

</script>
</html>