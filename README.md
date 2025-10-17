# Interview-Management-System
---

# Interview Management System

**Overview:**
This is a **Java console-based application** to manage candidates and interviews for a recruitment process.
It demonstrates **Object-Oriented Programming (OOP) concepts**, **list management**, and **console-based user interaction**. The system is designed for easy scalability and future enhancements.

**Features:**

1. **Add Candidate** – Register new candidates with details like name, role, and experience.
2. **Schedule Interview** – Assign an interviewer and a date for a candidate.
3. **Update Candidate Status** – Mark candidates as Pending, Selected, or Rejected.
4. **View All Candidates** – Display all registered candidates with details.
5. **Search Candidate** – Find a candidate by ID or name.
6. **View All Interviews** – List all scheduled interviews with candidate and interviewer info.
7. **Filter Candidates by Status** – Display candidates filtered by status (Selected/Rejected/Pending).
8. **Delete Candidate** – Remove a candidate from the system.
9. **Show Summary Report** – Overview of total candidates, interviews, and status counts.
10. **Exit** – Exit the application.

## **Menu & Walkthrough**

```
===== Interview Management System =====
1. Add Candidate
2. Schedule Interview
3. Update Candidate Status
4. View All Candidates
5. Search Candidate
6. View All Interviews
7. Filter Candidates by Status
8. Delete Candidate
9. Show Summary Report
10. Exit
Enter choice:
```

---

### **1. Add Candidate**

* User selects **1**
* System asks for:

  * Candidate Name
  * Position (SDE, SDET, SWE, SE)
  * Experience (in years)
* Output:

```
Candidate added successfully!
ID: C1, Name: John Doe, Position: SDE, Experience: 0 years, Status: Pending
```

---

### **2. Schedule Interview**

* User selects **2**
* System asks for:

  * Candidate ID
  * Interviewer Name
  * Interview Date (dd-mm-yyyy)
* Output:

```
Interview scheduled successfully!
Candidate: John Doe
Interviewer: Jane Smith
Date: xx-xx-xxxx
```

---

### **3. Update Candidate Status**

* User selects **3**
* System asks for:

  * Candidate ID
  * New Status (Pending / Selected / Rejected)
* Output:

```
Status updated successfully!
Candidate: John Doe
New Status: Selected
```

---

### **4. View All Candidates**

* User selects **4**
* Output (formatted table):

```
-------------------------------------------------------------
ID    Name            Position   Experience  Status
-------------------------------------------------------------
C1    John Doe        SDE        0           Selected
C2    Alice Smith     SDET       0           Pending
```

---

### **5. Search Candidate**

* User selects **5**
* System asks for **Candidate ID or Name**
* Output:

```
Candidate Found:
ID: C1, Name: John Doe, Position: SDE, Experience: 0, Status: Selected
```

---

### **6. View All Interviews**

* User selects **6**
* Output (formatted table):

```
-------------------------------------------------------------
Candidate       Role    Interviewer     Date
-------------------------------------------------------------
John Doe        SDE     Jane Smith      xx-xx-xxxx
Alice Smith     SDET    Mark Brown      xx-xx-xxxx
```

---

### **7. Filter Candidates by Status**

* User selects **7**
* System asks for status (Pending/Selected/Rejected)
* Output:

```
Filtered Candidates (Status: Pending):
ID: C2, Name: Alice Smith, Position: SDET, Status: Pending
```

---

### **8. Delete Candidate**

* User selects **8**
* System asks for Candidate ID
* Output:

```
Candidate C2 deleted successfully!
```

---

### **9. Show Summary Report**

* User selects **9**
* Output:

```
Summary Report:
Total Candidates: 3
Pending: 1
Selected: 1
Rejected: 1
Total Interviews Scheduled: 2
```

---

### **10. Exit**

* User selects **10**
* Output:

```
Goodbye!
```

---

**Technologies Used:**

* Java SE (Core Java)
* OOP Concepts: Classes, Objects, Encapsulation
* Collections: ArrayList
* Console Input/Output (Scanner)
---
