<%@ page import="shop.Product"%>

<jsp:useBean id='db'
             scope='session'
             class='shop.ShopDB' />

<%@include file="menu.html"%>
<div id="main">
<%
    String pid = request.getParameter("pid");
    Product product = db.getProduct(pid);
    if (product == null) { %>
        <%@include file="404.html"%>
    <%}
    else {
        double price = product.price / 100.00;
        %>


            <h2> <%= product.title %>  by <%= product.artist %> </h2>

            <img id="productImage" src="<%= product.fullimage %>" />
            <p id="price"> <%= "&pound;" + String.format("%.2f", price) %> </p>
            <p> <%= product.description %> </p>
            <a href = '<%="basket.jsp?pid="+product.PID%>'>Add to basket</a>
        <%
    }
%>
</div>
</body>
</html>
