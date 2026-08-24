import { useState } from "react";
import "./App.css";

const API_URL = "http://localhost:8080/api/review";

const languageSamples = {
  Java: `public class Main {
    public static void main(String[] args) {
        int x = 10;
        System.out.println(x);
    }
}`,

  Python: `def main():
    x = 10
    print(x)

if __name__ == "__main__":
    main()`,

  "C++": `#include <iostream>
using namespace std;

int main() {
    int x = 10;
    cout << x << endl;
    return 0;
}`,

  C: `#include <stdio.h>

int main() {
    int x = 10;
    printf("%d\\n", x);
    return 0;
}`,

  JavaScript: `function main() {
    const x = 10;
    console.log(x);
}

main();`,

  "C#": `using System;

class Program {
    static void Main() {
        int x = 10;
        Console.WriteLine(x);
    }
}`
};

const languages = Object.keys(languageSamples);

function App() {
  const [language, setLanguage] = useState("Java");
  const [code, setCode] = useState(languageSamples.Java);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleLanguageChange = (newLanguage) => {
    setLanguage(newLanguage);

    // Change editor code to the selected language's sample code.
    setCode(languageSamples[newLanguage]);

    // Clear previous review because it belongs to the previous language/code.
    setResult(null);
    setError("");
  };

  const reviewCode = async () => {
    if (!code.trim()) {
      setError("Please enter some code before reviewing.");
      return;
    }

    setLoading(true);
    setError("");
    setResult(null);

    try {
      const response = await fetch(API_URL, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          code: code,
          language: language
        })
      });

      if (!response.ok) {
        throw new Error(`Server returned ${response.status}`);
      }

      const data = await response.json();

      setResult(data);
    } catch (err) {
      console.error(err);

      setError(
        "Could not connect to the AI Code Reviewer backend. Make sure Spring Boot is running on port 8080."
      );
    } finally {
      setLoading(false);
    }
  };

  const formatImprovedCode = (value) => {
    if (!value) return "";

    let formatted = value;

    // Handle cases where Gemini returns literal \n characters.
    formatted = formatted.replace(/\\n/g, "\n");
    formatted = formatted.replace(/\\t/g, "\t");
    formatted = formatted.replace(/\\"/g, '"');

    // Remove markdown code fences if Gemini returns them.
    formatted = formatted.replace(/^```[a-zA-Z0-9+#-]*\s*/i, "");
    formatted = formatted.replace(/\s*```$/i, "");

    return formatted.trim();
  };

  const renderList = (items, emptyMessage) => {
    if (!items || items.length === 0) {
      return (
        <div className="empty-message">
          <span className="success-icon">✓</span>
          {emptyMessage}
        </div>
      );
    }

    return (
      <ul className="review-list">
        {items.map((item, index) => (
          <li key={index}>{item}</li>
        ))}
      </ul>
    );
  };

  return (
    <div className="app">

      {/* NAVBAR */}
      <header className="navbar">
        <div className="brand">
          <div className="brand-icon">&lt;/&gt;</div>
          <div>
            <div className="brand-name">AI Code Reviewer</div>
            <div className="brand-tagline">Write better. Fix faster.</div>
          </div>
        </div>

        <div className="status">
          <span className="status-dot"></span>
          AI Powered
        </div>
      </header>

      {/* HERO */}
      <section className="hero-section">
        <div className="hero-badge">
          <span>✦</span>
          Intelligent Code Analysis
        </div>

        <h1>
          Write better code.
          <br />
          <span>Improve faster.</span>
        </h1>

        <p className="hero-description">
          Get instant AI-powered code reviews for bugs, security,
          performance, quality, and improvements.
        </p>
      </section>

      {/* MAIN REVIEW AREA */}
      <main className="main-container">

        {/* CODE INPUT CARD */}
        <section className="card code-card">

          <div className="section-header">
            <div>
              <h2>Code Review</h2>
              <p>Paste your code and let AI analyze it.</p>
            </div>

            <div className="language-count">
              {languages.length} languages
            </div>
          </div>

          {/* LANGUAGE BUTTONS */}
          <div className="language-tabs">
            {languages.map((item) => (
              <button
                key={item}
                className={`language-tab ${
                  language === item ? "active" : ""
                }`}
                onClick={() => handleLanguageChange(item)}
              >
                {item}
              </button>
            ))}
          </div>

          {/* CODE EDITOR */}
          <div className="editor-wrapper">
            <div className="editor-header">
              <div className="editor-dots">
                <span></span>
                <span></span>
                <span></span>
              </div>

              <span className="editor-language">{language}</span>

              <span className="character-count">
                {code.length} characters
              </span>
            </div>

            <textarea
              value={code}
              onChange={(e) => setCode(e.target.value)}
              className="code-editor"
              spellCheck="false"
              placeholder={`Write your ${language} code here...`}
            />
          </div>

          {/* ERROR */}
          {error && (
            <div className="error-box">
              <span>⚠</span>
              {error}
            </div>
          )}

          {/* REVIEW BUTTON */}
          <button
            className="review-button"
            onClick={reviewCode}
            disabled={loading}
          >
            {loading ? (
              <>
                <span className="spinner"></span>
                Analyzing Code...
              </>
            ) : (
              <>
                Review Code
                <span>→</span>
              </>
            )}
          </button>
        </section>

        {/* RESULT */}
        {result && (
          <section className="results-section">

            <div className="results-title">
              <div>
                <div className="ai-badge">✦ AI ANALYSIS COMPLETE</div>
                <h2>AI Review</h2>
                <p>
                  Here's what our AI found in your{" "}
                  <strong>{language}</strong> code.
                </p>
              </div>

              <div className="review-language">
                {language}
              </div>
            </div>

            {/* SUMMARY */}
            <div className="review-card summary-card">
              <div className="review-icon blue">💡</div>

              <div className="review-content">
                <h3>Summary</h3>

                <p className="summary-text">
                  {result.summary || "No summary was provided."}
                </p>
              </div>
            </div>

            {/* BUGS */}
            <div className="review-card">
              <div className="review-icon red">🐛</div>

              <div className="review-content">
                <h3>Bugs & Runtime Issues</h3>

                {renderList(
                  result.bugs,
                  "No major bugs detected."
                )}

                {result.bugs && result.bugs.length > 0 && (
                  <div className="why-box">
                    <strong>Why does this matter?</strong>
                    <p>
                      These issues can cause incorrect behavior,
                      runtime failures, crashes, or unexpected results.
                    </p>
                  </div>
                )}
              </div>
            </div>

            {/* SECURITY */}
            <div className="review-card">
              <div className="review-icon orange">🔐</div>

              <div className="review-content">
                <h3>Security</h3>

                {renderList(
                  result.securityIssues,
                  "No major security issues detected."
                )}
              </div>
            </div>

            {/* CODE QUALITY */}
            <div className="review-card">
              <div className="review-icon purple">✨</div>

              <div className="review-content">
                <h3>Code Quality</h3>

                {renderList(
                  result.codeQuality,
                  "Code quality looks good."
                )}

                {result.codeQuality &&
                  result.codeQuality.length > 0 && (
                    <div className="why-box">
                      <strong>Why improve this?</strong>
                      <p>
                        Improving readability and maintainability makes
                        the code easier to understand, debug, test,
                        and modify later.
                      </p>
                    </div>
                  )}
              </div>
            </div>

            {/* PERFORMANCE */}
            <div className="review-card">
              <div className="review-icon green">⚡</div>

              <div className="review-content">
                <h3>Performance</h3>

                {renderList(
                  result.performance,
                  "No major performance issues detected."
                )}
              </div>
            </div>

            {/* IMPROVED CODE */}
            <div className="improved-section">

              <div className="improved-header">
                <div>
                  <div className="ai-badge">✦ AI SUGGESTION</div>
                  <h2>Improved Code</h2>
                  <p>
                    A cleaner version of your code with suggested
                    improvements.
                  </p>
                </div>

                <div className="language-pill">
                  {language}
                </div>
              </div>

              <div className="code-window">

                <div className="code-window-header">
                  <div className="window-dots">
                    <span></span>
                    <span></span>
                    <span></span>
                  </div>

                  <span>{language}</span>

                  <button
                    className="copy-button"
                    onClick={() => {
                      navigator.clipboard.writeText(
                        formatImprovedCode(result.improvedCode)
                      );
                    }}
                  >
                    Copy
                  </button>
                </div>

                <pre className="improved-code">
                  <code>
                    {formatImprovedCode(result.improvedCode)}
                  </code>
                </pre>

              </div>

              {/* WHY IMPROVED */}
              <div className="improvement-explanation">

                <div className="explanation-icon">
                  💎
                </div>

                <div>
                  <h3>Why is this code improved?</h3>

                  <p>
                    The AI-generated version focuses on readability,
                    maintainability, consistency, and cleaner coding
                    practices.
                  </p>

                  <div className="improvement-points">

                    <div>
                      <span>✓</span>
                      <p>
                        <strong>Better readability</strong>
                        <br />
                        The code is formatted and structured so it is
                        easier to understand.
                      </p>
                    </div>

                    <div>
                      <span>✓</span>
                      <p>
                        <strong>Clearer naming</strong>
                        <br />
                        Variables and methods should clearly describe
                        their purpose.
                      </p>
                    </div>

                    <div>
                      <span>✓</span>
                      <p>
                        <strong>Easier maintenance</strong>
                        <br />
                        Cleaner code is easier to modify and debug in
                        the future.
                      </p>
                    </div>

                  </div>
                </div>

              </div>

            </div>

          </section>
        )}

      </main>

      {/* FOOTER */}
      <footer>
        <div>
          <strong>&lt;/&gt; AI Code Reviewer</strong>
          <span>Built with React + Spring Boot + Gemini AI</span>
        </div>

        <span>AI-powered code analysis</span>
      </footer>

    </div>
  );
}

export default App;