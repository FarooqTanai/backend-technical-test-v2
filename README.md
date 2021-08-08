## Summary

TUI DX Backend technical Test v2

The base project uses lombok, so you have to install it. You can use the following guide https://www.baeldung.com/lombok-ide

## Project implemented as follow:

1. Lombok is used as asked.
2. H2 in-memory database is used.
3. Spring-security is used for authentication
	3.1 As per the requirement only the (GET: /orders) and (GET: /clients) api are authenticated
	3.2 the rest of the apis are left open
4. For authentication the username and password are configured in BasicConfiguration.class, can be used for the GET apis
	41. username/password are admin/admin
5. Junits covers all parts of controllers and services classes