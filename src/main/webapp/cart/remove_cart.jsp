<%@page import="java.net.URLEncoder"%>
<%@page import="cart.CartItemDTO"%>
<%@page import="cart.CartService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8" info=""%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
	request.setCharacterEncoding("UTF-8");
	Integer userId = (Integer)session.getAttribute("userId");
	CartService cs = new CartService();
	int cartId = cs.searchCartId(userId);
	if(cartId ==0){
	cartId = cs.makeCartId(userId);
	}
	
	CartItemDTO ciDTO = new CartItemDTO();
	
	ciDTO.setCartId(cartId);
	String [] selectedItem = request.getParameterValues("checkCart");
	
	if(selectedItem != null && selectedItem.length>0){
	for(String item : selectedItem){	
		int items = Integer.parseInt(item);
		cs.deleteCartItem(cartId, items);
		}
		session.setAttribute("toast", "선택한 항목이 삭제되었습니다");
		response.sendRedirect("cart.jsp");
	}else{
		session.setAttribute("toast", "삭제할 항목을 선택하세요.");
		response.sendRedirect("cart.jsp");
	}
%>
