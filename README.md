# Entity Level Security

## Overview

This project implements a **data access module library** for Java Spring, designed to manage user roles and permissions for database operations. The module is compatible with Object-Relational (O-R) mapping systems, allowing the creation and assignment of roles with specific permissions such as:

- **READ**
- **WRITE**
- **MODIFY**
- **NO PERMISSION**

Users can have multiple roles connected to multiple databases. The project leverages **Spring Boot** and employs several design patterns for improved structure and maintainability.

## Project Structure

The project is organized into the following components:

- **DemoApplication**: Example code demonstrating the library's usage.
- **Logger**: A singleton class for managing application logs.
- **Permission**: An enumeration of available permissions.
- **Role**: Represents user roles, containing permission and database associations.
- **RoleBuilder**: A builder class for constructing `Role` objects.
- **User**: Represents a user and their assigned roles.
- **UserBuilder**: A builder class for constructing `User` objects.
- **DatabaseOperations**: An interface defining possible database operations.
- **Database**: A base implementation of `DatabaseOperations` containing core database methods.
- **ProxyDatabase**: A proxy implementation of `DatabaseOperations`, enforcing permission checks for database operations.
- **Command**: An interface for defining command objects.
- **SelectCommand, InsertCommand, UpdateCommand, DeleteCommand**: Commands representing specific database actions.
- **CommandInvoker**: A class implementing a queue for executing commands.
- **Tests Directory**: Contains unit tests for all the above components.
