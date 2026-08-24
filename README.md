# AI Code Reviewer

AI-powered code review platform that analyzes source code, identifies potential issues, and generates improved code using Google Gemini AI.

## Features

* AI-powered code analysis
* Supports Java, Python, C++, C, JavaScript, and C#
* Detects bugs and runtime issues
* Identifies security concerns
* Provides code-quality suggestions
* Analyzes potential performance issues
* Generates improved versions of submitted code
* Explains why the suggested code is better
* Code validation before AI analysis
* React-based web interface
* Spring Boot REST API

## Tech Stack

**Frontend:** React, JavaScript, Vite, CSS

**Backend:** Java, Spring Boot, REST API

**AI:** Google Gemini API

**Build Tool:** Maven

**Version Control:** Git, GitHub

## Project Architecture

```text
React Frontend
      │
      │ HTTP POST
      ▼
Spring Boot REST API
      │
      ├── Code Validation
      │
      └── Gemini AI Service
                │
                ▼
          Google Gemini API
                │
                ▼
        Structured AI Review
                │
                ▼
          React Frontend
```

## How It Works

1. Select a programming language.
2. Enter or paste your source code.
3. Submit the code for review.
4. The backend validates the submitted code.
5. The code is analyzed using Google Gemini AI.
6. The platform returns:

   * Summary
   * Bugs and runtime issues
   * Security issues
   * Code-quality issues
   * Performance considerations
   * Improved code
   * Explanation of improvements
7. Results are displayed in the React interface.

## Backend API

### Review Code

**Endpoint**

```text
POST /api/review
```

**Request**

```json
{
  "code": "public class Main { public static void main(String[] args) { System.out.println(\"Hello\"); } }",
  "language": "Java"
}
```

**Response**

```json
{
  "summary": "The program prints Hello to standard output.",
  "bugs": [],
  "securityIssues": [],
  "codeQuality": [],
  "performance": [],
  "improvedCode": "..."
}
```

## Getting Started

### Prerequisites

Make sure the following are installed:

* Java
* Maven
* Node.js
* npm
* Git
* Google Gemini API key

### Clone the Repository

```bash
git clone https://github.com/Nikhil-Kumar-13140/ai-code-reviewer.git

cd ai-code-reviewer
```

### Backend Setup

Set your Gemini API key as an environment variable.

**Windows PowerShell:**

```powershell
$env:GEMINI_API_KEY="YOUR_API_KEY"
```

Then start the Spring Boot backend:

```powershell
mvn spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

### Frontend Setup

Open another terminal:

```powershell
cd frontend
npm install
npm run dev
```

The frontend runs on:

```text
http://localhost:5173
```

## Security

The Gemini API key is loaded through an environment variable:

```properties
gemini.api.key=${GEMINI_API_KEY}
```

Never commit your actual API key to GitHub.

## Future Improvements

* More accurate language-specific code validation
* Line-by-line error detection
* Code execution and compilation support
* Syntax highlighting
* Review history
* Downloadable review reports
* Authentication and user accounts
* More programming languages
* Advanced AI review modes

## Author

**Nikhil Kumar**

B.Tech Computer Science & Engineering

---

Built with React, Spring Boot, and Google Gemini AI.
