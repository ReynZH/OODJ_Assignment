**Assessment Feedback System (AFS)**

About the Project

The Assessment Feedback System (AFS) is a comprehensive Java desktop application developed to streamline academic assessments, grading, and feedback delivery. It replaces manual record-keeping with a digital solution, ensuring data accuracy, timely feedback, and structured grading based on pre-defined mark allocations. This project was developed as part of the Object-Oriented Development with Java (OODJ) coursework at Asia Pacific University (A.P.U.).

Key Features:

The system supports four distinct user roles, each with specific access rights and functionalities:

Students: Register for classes, attempt interactive quizzes (with real-time auto-grading and one-attempt limit), view color-coded performance histories, and submit feedback.
Lecturers: Manage their profiles, design assessment types, key-in assessment marks, and provide detailed feedback for student submissions.
Academic Leaders: Oversee modules, assign lecturers to specific subjects, and generate analyzed academic reports.
Administrative Staff: Handle system configuration, manage user registration (Create, Read, Update, Delete), and define the university grading system.

Tech Stack
Language: Java 
UI Framework: JavaSwing & JavaAWT
Storage: Text Files
IDE:NetBeans


Object-Oriented Programming Concepts Applied

This project heavily emphasizes software engineering best practices through the application of core OOP principles:

Encapsulation: Protecting sensitive user data using private modifiers and public getter/setter methods.

Inheritance: Reducing code duplication by extending base UI classes for specific user roles.

Polymorphism: Utilizing method overriding to dynamically render role-specific interfaces and tables at runtime.

Abstraction: Implementing the Repository Pattern to hide complex File I/O logic from the front-end UI components.

Composition: Building complex dashboard views by composing them of smaller, specialized service and repository objects.

How to Run

1. Clone this repository to your local machine.

2. Open the project in your preferred Java IDE.

3. Ensure the `src` folder is set as your sources root.

4. Run the main application file.

5. Note: Ensure the text files are located in the correct root directory so the system can read/write data successfully.
