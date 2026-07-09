# Task Management API Documentation

## Table of Contents

- [Authentication](#authentication)
- [Tasks](#tasks)
- [Notifications](#notifications)
- [Error Handling](#error-handling)

---

## Authentication Endpoints

### Register User

**POST** `/auth/register`

Creates a new user account and returns JWT tokens.

**Request Body:**
```json
{
  "username": "string (required, unique)",
  "email": "string (required, valid email)",
  "password": "string (required, min 6 chars)"
}
```

**Success Response (200 OK):**
```json
{
  "accessToken": "string",
  "refreshToken": "string",
  "user": {
    "id": "number",
    "username": "string",
    "email": "string",
    "role": "string"
  }
}
```

**Error Responses:**
- `400 Bad Request` - Validation error
- `409 Conflict` - Username/email already exists

---

### Login

**POST** `/auth/login`

Authenticates user and returns JWT tokens.

**Request Body:**
```json
{
  "username": "string (required)",
  "password": "string (required)"
}
```

**Success Response (200 OK):**
```json
{
  "accessToken": "string",
  "refreshToken": "string",
  "user": {
    "id": "number",
    "username": "string",
    "email": "string"
  }
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid credentials

---

### Refresh Token

**POST** `/auth/refresh`

Generates new access token using refresh token.

**Request Body:**
```json
{
  "refreshToken": "string (required)"
}
```

**Success Response (200 OK):**
```json
{
  "accessToken": "string"
}
```

---

### Logout

**POST** `/auth/logout`

Invalidates the refresh token.

**Request Body:**
```json
{
  "refreshToken": "string (required)"
}
```

**Success Response (200 OK):**
```json
{
  "message": "Logged out successfully"
}
```

---

## Tasks Endpoints

### Get All Tasks

**GET** `/tasks`

Retrieves all tasks for the authenticated user.

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 0 | Page number |
| size | number | 10 | Items per page |

**Success Response (200 OK):**
```json
{
  "tasks": [
    {
      "id": "number",
      "title": "string",
      "description": "string",
      "priority": "LOW|MEDIUM|HIGH|CRITICAL",
      "status": "TODO|IN_PROGRESS|DONE",
      "dueDate": "string | null",
      "assignee": {
        "id": "number",
        "username": "string"
      } | null,
      "creator": {
        "id": "number",
        "username": "string"
      },
      "createdAt": "string",
      "updatedAt": "string",
      "commentCount": "number"
    }
  ],
  "totalPages": "number",
  "totalElements": "number",
  "currentPage": "number"
}
```

---

### Create Task

**POST** `/tasks`

Creates a new task.

**Request Body:**
```json
{
  "title": "string (required)",
  "description": "string",
  "priority": "LOW|MEDIUM|HIGH|CRITICAL",
  "dueDate": "string (ISO 8601 datetime)"
}
```

**Success Response (201 Created):**
```json
{
  "id": "number",
  "title": "string",
  "description": "string",
  "priority": "string",
  "status": "TODO",
  "dueDate": "string | null",
  "creator": {
    "id": "number",
    "username": "string"
  },
  "createdAt": "string"
}
```

---

### Get Task by ID

**GET** `/tasks/{id}`

Retrieves a specific task.

**Success Response (200 OK):**
```json
{
  "id": "number",
  "title": "string",
  "description": "string",
  "priority": "string",
  "status": "string",
  "dueDate": "string | null",
  "assignee": {
    "id": "number",
    "username": "string"
  } | null,
  "creator": {
    "id": "number",
    "username": "string"
  },
  "createdAt": "string",
  "commentCount": "number"
}
```

---

### Update Task

**PUT** `/tasks/{id}`

Updates a task's properties.

**Request Body:**
```json
{
  "title": "string",
  "description": "string",
  "priority": "LOW|MEDIUM|HIGH|CRITICAL",
  "status": "TODO|IN_PROGRESS|DONE",
  "dueDate": "string"
}
```

**Success Response (200 OK):**
Updated task object

---

### Delete Task

**DELETE** `/tasks/{id}`

Deletes a task.

**Success Response (204 No Content)**

---

### Get Activity

**GET** `/tasks/activity`

Retrieves recent task activity.

**Success Response (200 OK):**
```json
{
  "activities": [
    "string"
  ]
}
```

---

## Notifications Endpoints

### Get Notifications

**GET** `/notifications`

Retrieves all notifications for the user.

**Success Response (200 OK):**
```json
{
  "notifications": [
    {
      "id": "number",
      "message": "string",
      "type": "TASK_ASSIGNED|TASK_COMPLETED|REMINDER",
      "read": "boolean",
      "createdAt": "string"
    }
  ]
}
```

---

### Mark All Read

**PUT** `/notifications/read`

Marks all notifications as read.

**Success Response (200 OK)**

---

## Error Handling

All error responses follow this format:

```json
{
  "error": "string",
  "message": "string",
  "status": "number",
  "timestamp": "string"
}
```

### Common Error Codes

| Status | Description |
|--------|-------------|
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Missing/invalid token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource doesn't exist |
| 500 | Internal Server Error |

### Validation Errors

```json
{
  "error": "Validation Failed",
  "message": "title: Title is required",
  "status": 400
}
```