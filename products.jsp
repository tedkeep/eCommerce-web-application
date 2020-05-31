<%@ page import="shop.Product" %>

<jsp:useBean id='db'
             scope='session'
             class='shop.ShopDB'/>

<%
    String title = request.getParameter("title");
    String artist = request.getParameter("artist");
%>

<%@include file="menu.html" %>
<script>
    function checkArtist() {
        if (document.getElementById("artist").value === "") {
            alert("Please enter an artists name");
            return false;
        }
    }

    function checkTitle() {
        if (document.getElementById("title1").value === "") {
            alert("Please enter a title");
            return false;
        }
    }
</script>
<div id="main">
<form action="products.jsp" onsubmit="return checkArtist()">
    <input id="artist" type="input" name="artist" placeholder="Search by artist"/>
    <input type="submit" value="Search by artist"/>
</form>
<form action="products.jsp" onsubmit="return checkTitle()">
    <input id="title1" type="input" name="title" placeholder="Search by title"/>
    <input type="submit" value="Search by title"/>
</form>
<form action="products.jsp">
    <input type="submit" value="Show all products"/>
</form>
<table>
    <tr>
        <th> Title</th>
        <th> Price</th>
        <th> Picture</th>
    </tr>
    <%
        if (artist != null) {
            for (Product product : db.getProductsByArtist(artist)) {
                double price = product.price / 100.00;
    %>
    <tr>
        <td><%= product.title %>
        </td>
        <td><%= "&pound;" + String.format("%.2f", price) %>
        </td>
        <td><a href='<%="viewProduct.jsp?pid="+product.PID%>'> <img src="<%= product.thumbnail %>"/> </a></td>
    </tr>
    <%
        }
    } else if (title != null) {
            for (Product product : db.getProductsByTitle(title)) {
                double price = product.price / 100.00;
    %>
    <tr>
        <td><%= product.title %>
        </td>
        <td><%= "&pound;" + String.format("%.2f", price) %>
        </td>
        <td><a href='<%="viewProduct.jsp?pid="+product.PID%>'> <img src="<%= product.thumbnail %>"/> </a></td>
    </tr>
    <%
        }
    } else {
        for (Product product : db.getAllProducts()) {
            double price = product.price / 100.00;
    %>
    <tr>
        <td><%= product.title %>
        </td>
        <td><%= "&pound;" + String.format("%.2f", price) %>
        </td>
        <td><a href='<%="viewProduct.jsp?pid="+product.PID%>'> <img src="<%= product.thumbnail %>"/> </a></td>
    </tr>
    <%
            }
        }
    %>
</table>
</div>
</body>
</html>
