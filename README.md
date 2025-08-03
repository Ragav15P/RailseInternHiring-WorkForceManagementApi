# Workforce Management API 🚀

A lightweight Spring Boot application for managing operational tasks, built as part of the assignment requirements.  
It supports **smart task fetching**, **task prioritization**, and **real-time comments & activity history tracking**.

---

## 📌 Overview
This API helps operations teams manage their daily tasks efficiently.  
It allows employees to:
- View their relevant work for a specific day (or date range).
- Get tasks that are still pending from previous days.
- Prioritize work according to urgency.
- Collaborate via task comments and view a full history of changes.

---

## ✨ Features

### **Part 0 & Part 1 – Bug Fixes**
1. **Duplicate Active Task Bug (Fixed)**  
   - When reassigning tasks by reference, old active tasks are now cancelled before assigning a new one.
2. **Cancelled Tasks in Fetch Bug (Fixed)**  
   - Date-based fetching now excludes `CANCELLED` tasks and includes active tasks from before the date range.

---

### **Part 2 – New Features**
#### 1. Smart Daily Task View
- When fetching tasks by date range:
  - Returns **all active tasks started in that range**.
  - Returns **all active tasks started before the range but still open**.

#### 2. Task Priority
- Tasks have a `priority` field: `HIGH`, `MEDIUM`, `LOW`.
- **Endpoints**:
  - Update priority of a task.
  - Fetch all tasks of a specific priority.

#### 3. Task Comments & Activity History
- Add free-text comments to tasks.
- Automatically logs important actions such as:
  - Task creation
  - Priority change
  - Comments added
- View task details including:
  - Core task information
  - Activity history
  - All comments (sorted chronologically)

---

## 🛠 Tech Stack
- **Java 17**
- **Spring Boot**
- **In-Memory Repository** (No DB for simplicity)
- **Lombok** for boilerplate reduction
- **Maven** for build management

---

## 📡 API Endpoints

### **Task Management**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/task-mgmt/create` | Create new tasks |
| `PUT`  | `/task-mgmt/update` | Update task details |
| `POST` | `/task-mgmt/assign-by-reference` | Assign tasks based on reference ID & type |
| `POST` | `/task-mgmt/fetch-by-date/v2` | Fetch smart daily task view |

### **Priority Management**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `PUT`  | `/task-mgmt/{id}/priority?priority=HIGH` | Update a task's priority |
| `GET`  | `/task-mgmt/priority/{priority}` | Get tasks by priority |

### **Comments & History**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/task-mgmt/{id}/comments?user=Ragav&text=Message` | Add comment to a task |
| `GET`  | `/task-mgmt/details/{id}` | Get full task details with history & comments |

---

## 📥 Sample Request & Response

### Create Task
**Request**
```json
POST /task-mgmt/create
{
  "requests": [
    {
      "referenceId": 101,
      "referenceType": "ORDER",
      "task": "CREATE_INVOICE",
      "assigneeId": 1,
      "priority": "HIGH",
      "taskDeadlineTime": 1754282850844
    }
  ]
}
Response:



{
  "data": [
    {
      "id": 1,
      "reference_id": 101,
      "reference_type": "ORDER",
      "task": "CREATE_INVOICE",
      "priority": "HIGH",
      "status": "ASSIGNED"
    }
  ],
  "status": { "code": 200, "message": "Success" }
}


GET /task-mgmt/details/1


{
  "data": {
    "task": {
      "id": 1,
      "reference_id": 101,
      "reference_type": "ORDER",
      "task": "CREATE_INVOICE",
      "priority": "HIGH",
      "status": "ASSIGNED"
    },
    "activityHistory": [
      "Task created for reference ID 101 assigned to user 1",
      "Comment added by Ragav"
    ],
    "comments": [
      {
        "user": "Ragav",
        "comment": "This is my first comment",
        "timestamp": 1754196462725
      }
    ]
  }
}
