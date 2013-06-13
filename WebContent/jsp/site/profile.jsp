<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h3>My Information</h3>
<table class="table">
	<tr>
		<td>Customer ID</td>
		<td>${customer.customer_id}</td>
	</tr>
	<tr>
		<td>NRIC Number</td>
		<td>${customer.nric}</td>
	</tr>
	<tr>
		<td>Username</td>
		<td>${customer.username}</td>
	</tr>
	<tr>
		<td>Given Name</td>
		<td>${customer.givenname}</td>
	</tr>
	<tr>
		<td>Address</td>
		<td>${customer.address}</td>
	</tr>
	<tr>
		<td>Gender</td>
		<td>${customer.gender}</td>
	</tr>
	<tr>
		<td>Nationality</td>
		<td>${customer.nationality}
	</tr>
	<tr>
		<td>Date of Birth</td>
		<td>${customer.date_of_birth}</td>
	</tr>
	<tr>
		<td>Date of Join</td>
		<td>${customer.date_of_join}</td>
	</tr>
</table>